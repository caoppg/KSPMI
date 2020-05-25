package halfback.cmpm.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import halfback.cmpm.model.tce.PatternTracePair;
import halfback.cmpm.model.Chronicle;

/**
 * Implementation of a Chronicle Builder. <br>
 * A Chronicle Builder thread takes traces from a pool (built by TimeConstraintExtractor threads), builds a chronicle and puts it in another pool.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class ChronicleBuilder implements Runnable {

    private static BlockingQueue<PatternTracePair> _tracePool;

    private static Set<Chronicle> _chronicleSet;

    private static BlockingQueue<Chronicle> _chroniclePool;

    private static boolean _extractionFinished;

    static {
        _tracePool = null;
        _chronicleSet = Collections.synchronizedSet(new HashSet<Chronicle>());
        _chroniclePool = null;
        _extractionFinished = false;
    }

    public static void setTracePool(BlockingQueue<PatternTracePair> tracePool) {
        if (_chroniclePool == null) {
            _chroniclePool = new LinkedBlockingQueue<Chronicle>(tracePool.remainingCapacity());
        }
        _tracePool = tracePool;
    }

    public void run() {
        PatternTracePair currentTask;
        try {
            while (!_extractionFinished) {
                while ((currentTask = _tracePool.poll()) != null) {
                    Chronicle chronicle = new Chronicle(currentTask);
                    _chroniclePool.put(chronicle);
                    _chronicleSet.add(chronicle);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public static BlockingQueue<Chronicle> getChroniclePool() {
        return _chroniclePool;
    }

    public static void finish() {
        _extractionFinished = true;
    }

    public static Set<Chronicle> getChronicles() {
        return _chronicleSet;
    }
}