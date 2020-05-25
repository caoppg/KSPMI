package halfback.cmpm.model.tce;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequences;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequence;

import halfback.cmpm.model.tce.TimeConstraintSet;
import halfback.cmpm.model.ssl.SubsequenceLattice;
import halfback.cmpm.model.ssl.ItemsetNode;

/**
 * Implementation of a time constraint extractor. <br>
 * A time constraint extractor is a {@link java.lang.Runnable} that will be fed patterns found in
 * the sequence database from a queue. It extracts a set of time constraints (a trace) for each
 * pattern, for each sequence it is in.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 * 
 * @see halfback.cmpm.model.tce.TimeConstraintSet
 * @see ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern
 * @see ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequence
 */
public class TimeConstraintExtractor implements Runnable {

    /**
     * Tasks pool for threads to treat.
     */
    private static BlockingQueue<Pattern> _tasks;

    /**
     * Trace pool.
     */
    private static BlockingQueue<PatternTracePair> _tracePool;

    /**
     * Database of sequences containing events and timestamps.
     */
    private final SequenceDatabase _sequences;

    /**
     * Trace of the joint chronicle.
     */
    private final static TimeConstraintSet _trace;

    /**
     * Whether the joint chronicle should be built.
     */
    private static boolean _join;

    static {
        _tasks = null;
        _tracePool = null;
        _join = false;
        _trace = new TimeConstraintSet();
    }

    /**
     * Constructor.
     * 
     * @param sequences The sequence database (sequences contain events and timestamps).
     */
    public TimeConstraintExtractor(SequenceDatabase sequences) {
        super();
        _sequences = sequences;
    }

    /**
     * Runs this instance of TimeConstraintExtractor. Each TimeConstraintExtractor will take a 
     * task from the tasks queue (it is removed) and treat it, until the queue is empty.
     */
    public void run() {
        Pattern currentTask;
        try {
            if (_join) {
                while ((currentTask = _tasks.poll()) != null) {
                    TimeConstraintSet trace = extractTimeConstraint(currentTask);
                    PatternTracePair ptp = new PatternTracePair(currentTask, trace);
                    _tracePool.put(ptp);
                    _trace.union(trace);
                }
            } else {
                while ((currentTask = _tasks.poll()) != null) {
                    TimeConstraintSet trace = extractTimeConstraint(currentTask);
                    PatternTracePair ptp = new PatternTracePair(currentTask, trace);
                    _tracePool.put(ptp);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Inserts all patterns into the tasks queue.
     * 
     * @param patterns A collection of sequential patterns
     */
    public static void populateQueue(Sequences patterns) {
        if (_tasks == null) {
            // Longer patterns will be treated first
            _tasks = new PriorityBlockingQueue<Pattern>(patterns.size(), new Comparator<Pattern>() {
                public int compare(Pattern p1, Pattern p2) {
                    return p1.size() - p2.size();
                }
            });
        }
        _tracePool = new LinkedBlockingQueue<PatternTracePair>(patterns.size());
        for (List<Pattern> lp : patterns.getLevels()) {
            _tasks.addAll(lp);
        }
    }

    /**
     * Sets whether the extractor should build the joint trace set.
     * 
     * @param join Whether the joint trace should be built.
     */
    public static void setJoin(boolean join) {
        _join = join;
    }

    /**
     * Returns the trace pool.
     * @return The trace pool.
     */
    public static BlockingQueue<PatternTracePair> getTracePool() {
        return _tracePool;
    }

    /**
     * Extracts the time constraints associated to the pattern treated.
     * 
     * @param pattern An instance of a sequential pattern.
     * 
     * @return A set of time constraint also called trace.
     */
    private TimeConstraintSet extractTimeConstraint(Pattern pattern) {

        if (pattern.size() == 1) return null;

        // Masking some objects for better reading
        BitSet appearingIn = pattern.getAppearingIn();
        List<Sequence> sequences = _sequences.getSequences();

        // This transformation allows for an easier comparison of the sequence and the pattern
        Sequence patternSequence = pattern.toSequence();

        // Actual declarations
        int itemsetsInPattern = patternSequence.size();
        int numPairs = combinations(itemsetsInPattern, 2);
        long delta;
        long[] patternMinDeltas = new long[numPairs];
        long[] patternMaxDeltas = new long[numPairs];

        for (int i = 0; i < numPairs; ++i) {
            patternMinDeltas[i] = Long.MAX_VALUE;
            patternMaxDeltas[i] = Long.MIN_VALUE;
        }
        
        for(int k = appearingIn.nextSetBit(0); k >= 0; k = appearingIn.nextSetBit(k + 1)) {
            Sequence currentSequence = sequences.get(k - 1);
            SubsequenceLattice sl = new SubsequenceLattice(currentSequence, patternSequence);
            sl.match();
            sl.prune();

            /* These variables are used to index the different pairs of events, e.g.
             * The pattern is A -> B -> C
             * Then, s = 0 is A -> B, s = 1 is A -> C and s = 2 is B -> C.
             */
            int start = 0, s = 0;

            // Compute time deltas
            for (int i = 0; i < patternSequence.size(); ++i) {
                ItemsetNode node = sl.get(i).getFirst();
                while (null != node && !node.isPruned()) {
                    s = start;
                    ItemsetNode target = node.getNext();
                    ItemsetNode firstInLayer = target;
                    for (int j = i + 1; j < patternSequence.size(); ++j, ++s) {
                        while (null != target && !target.isPruned()) {
                            delta = target.getItemset().getTimestamp() - node.getItemset().getTimestamp();
                            if (patternMinDeltas[s] > delta) {
                                patternMinDeltas[s] = delta;
                            } else if (patternMaxDeltas[s] < delta) {
                                patternMaxDeltas[s] = delta;
                            }
                            target = target.getNextInLayer();
                        }
                        target = firstInLayer.getNext();
                    }
                    node = node.getNextInLayer();
                }
                start = s;
            }
        }
        List<NumberedItemset> numberedPattern = new ArrayList<NumberedItemset>();
        for (int i = 0; i < patternSequence.size(); ++i) {
        	numberedPattern.add(new NumberedItemset(i, patternSequence.get(i)));
        }

        // Finally, create the trace set with all the time constraints
        TimeConstraintSet trace = new TimeConstraintSet(numPairs);
        for (int i = 0, s = 0; i < patternSequence.size() - 1; ++i) {
            for (int j = i + 1; j < patternSequence.size(); ++j, ++s) {
                trace.add(new TimeConstraint(numberedPattern.get(i), patternMinDeltas[s], patternMaxDeltas[s], numberedPattern.get(j)));
            }
        }
        return trace;
    }

    /**
     * Returns the joint trace, i.e. the union of all the extracted traces so far.
     * 
     * @return The unified trace.
     */
    public static TimeConstraintSet getTrace() {
        return _trace;
    }

    /**
     * Computes the combination nCk. Used to know how many pair of event types there are in a sequence.
     * TODO : add memoization to improve performance.
     * 
     * @return the combination nCk.
     */
    private int combinations(int n, int k) {
        int kfact = 1, nkfact = 1, i = 1;

        if (k > n - k) {
            k = n - k;
        }

        for (; i <= k; ++i) {
            kfact *= i;
        }

        for (i = n - k + 1; i <= n; ++i) {
            nkfact *= i;
        }

        return nkfact / kfact;
    }
}