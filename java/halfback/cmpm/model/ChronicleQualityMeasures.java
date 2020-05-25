package halfback.cmpm.model;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;
import halfback.cmpm.model.tce.NumberedItemset;
import halfback.cmpm.model.tce.PatternTracePair;
import halfback.cmpm.model.tce.TimeConstraint;
import halfback.cmpm.model.tce.TimeConstraintSet;

/**
 * Compute the accuracy and coverage of chronicles
 */

public class ChronicleQualityMeasures {

    /**
     * A map corresponding to a graph representation of the chronicle
     */
    private Map<Itemset, List<TimeConstraint>> _graph;

    /**
     * Set of all the event types used in the chronicle
     */
    private Set<Itemset> _episode;

    private Pattern _pattern;
    
    /**
     * Start and end timestamps for this time constraint.
     */
    private long _startTime;

	private long _endTime;

    /**
     * Start end end event types (see {@link ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset})
     * for this time constraint.
     */
    private NumberedItemset _startEv;

	private NumberedItemset _endEv;
    
    public void TimeConstraint(NumberedItemset startEv, long startTime, long endTime, NumberedItemset endEv) {
        _startEv = startEv;
        _endEv = endEv;
        _startTime = startTime;
        _endTime = endTime;
    }
    
    /**
     * Returns the start event of this time constraint.
     * 
     * @return The start event of this time constraint.
     */
    public Itemset getStartEvent() {
        return _startEv;
    }

    /**
     * Returns the end event of this time constraint.
     * 
     * @return The end event of this time constraint.
     */
    public Itemset getEndEvent() {
        return _endEv;
    }
    
    private int j=0;
    private int k=0;
    private int l=0;

    /**
     * Empty constructor.
     */
    public ChronicleQualityMeasures() {
        _graph = new LinkedHashMap<Itemset, List<TimeConstraint>>();
        _episode = new HashSet<Itemset>();
    }

    /**
     * Constructor. <br>
     * It takes a trace and builds a chronicle from it.
     * @param trace The trace used to build the chronicle
     */
    public ChronicleQualityMeasures(PatternTracePair ptp) {
        _graph = new LinkedHashMap<Itemset, List<TimeConstraint>>();
        _episode = new HashSet<Itemset>();
        _pattern = ptp.getPattern();
        fromTrace(ptp.getTrace());
    }

    /**
     * This method takes a trace and adds the time constraints to the trace and the event types to the episode.
     * @param trace The trace used to build the chronicle
     */
    private void fromTrace(TimeConstraintSet trace) {
        for (TimeConstraint tc : trace.values()) {
            // Compute TP for accuracy
            if (_graph.containsKey(tc.getStartEvent())) {
                _graph.get(tc.getStartEvent()).add(tc);
            } else {
            // If the key was not found, add it and also add it to the episode
                List<TimeConstraint> timeconstraints = new ArrayList<TimeConstraint>();
                timeconstraints.add(tc);
                _graph.put(tc.getStartEvent(), timeconstraints);
                _episode.add(tc.getStartEvent());
            }
            // Finally, add the end event to the episode (sets allow no duplicate)
            _episode.add(tc.getEndEvent());
        }
    }

    
    private void fromTrace1(TimeConstraintSet trace1) {
        for (TimeConstraint tc : trace1.values()) {
            // Compute marginal total for coverage
            if (_graph.containsKey(tc.getEndEvent())) {
                _graph.get(tc.getEndEvent()).add(tc);
            } else {
            // If the key was not found, add it and also add it to the episode
                List<TimeConstraint> timeconstraints = new ArrayList<TimeConstraint>();
                timeconstraints.add(tc);
                _graph.put(tc.getStartEvent(), timeconstraints);
                _episode.add(tc.getStartEvent());
            }
            // Finally, add the end event to the episode (sets allow no duplicate)
            _episode.add(tc.getEndEvent());
        }
    }
    
    public TimeConstraint compare(TimeConstraint tc) {
        long startTime, endTime;
        //check wether the intervals in sequences are within the temporal constraints.
        if (get_startTime() < tc.get_startTime()) {
            startTime = get_startTime();
            j++;
        } else {
            startTime = tc.get_startTime();
            k++;
        }
        if (get_endTime() > tc.get_startTime()) {
            endTime = get_endTime();
            k++;
        } else {
            endTime = tc.get_startTime();
        }
      //Compute marginal total for accuracy
        if (get_startTime() > tc.get_startTime()) {
            startTime = get_startTime();
            l++;
        }
          else {
                endTime = tc.get_startTime();
            k++;
          }
		return tc;
    }
		
    private long get_endTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	private long get_startTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
     * Returns the episode of this chronicle.
     * 
     * @return The episode of this chronicle.
     */
    public Set<Itemset> getEpisode() {
        return _episode;
    }

    /**
     * Returns the size of the episode.
     * 
     * @return The size of the episode.
     */
    public int size() {
        return _episode.size();
    }

    /**
     * Returns the trace of this chronicle.
     * 
     * @return The trace of this chronicle.
     */
    public Map<Itemset, List<TimeConstraint>> getTrace() {
        return _graph;
    }

    public int getSupport() {
        return _pattern.getSupport();
    }
    
    public double getCoverage() {
    	double divisionResult = (double) (j) / (double) (j+k-_pattern.getSupport());
    	return divisionResult;
    }
    
    public double getAccuracy() {
    	double divisionResult1 = (double) (l) / (double) (j+k-_pattern.getSupport());
    	return divisionResult1;
    }

    public String getPattern() {
        return _pattern.toSequence().toString();
    }

    /**
     * Returns the string representation of a chronicle as a graph.
     * 
     * @return A reprensentation of this chronicle as a graph.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Itemset i : _graph.keySet()) {
            for (TimeConstraint tc : _graph.get(i)) {
                sb.append(tc.toString()).append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

}