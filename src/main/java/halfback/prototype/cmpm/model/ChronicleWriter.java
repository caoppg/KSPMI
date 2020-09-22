package halfback.prototype.cmpm.model;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import halfback.prototype.cmpm.model.Chronicle;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

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