package halfback.cmpm.model;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import halfback.cmpm.model.Chronicle;

/**
 * An implementation of a Chronicle Writer. <br>
 * The Chronicle Writer thread takes chronicles from a pool (built by Chronicle Builder threads) and writes them to a file.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class ChronicleWriter implements Runnable {

    private final BlockingQueue<Chronicle> _chroniclePool;

    private final Set<Chronicle> _chronicles;

    private boolean _buildingFinished;

    private final BufferedWriter _bw;

    public ChronicleWriter(String outfile, BlockingQueue<Chronicle> chroniclePool) throws IOException {
        _bw = new BufferedWriter(new FileWriter(new File(outfile)));
        _chroniclePool = chroniclePool;
        _chronicles = new HashSet<Chronicle>();
    }

    public void run() {
        Chronicle currentTask;
        try {
            while (!_buildingFinished) {
                while((currentTask = _chroniclePool.poll()) != null) {
                    _bw.write(currentTask.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                _bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void finish() {
        _buildingFinished = true;
    }
}