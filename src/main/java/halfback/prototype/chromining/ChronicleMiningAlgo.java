package halfback.prototype.chromining;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.AlgoCM_ClaSP;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequences;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.savers.SaverIntoMemory;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreatorStandard_Map;
import halfback.prototype.cmpm.model.Chronicle;
import halfback.prototype.cmpm.model.ChronicleBuilder;
import halfback.prototype.cmpm.model.ChronicleWriter;
import halfback.prototype.cmpm.model.tce.PatternTracePair;
import halfback.prototype.cmpm.model.tce.TimeConstraintExtractor;

public class ChronicleMiningAlgo {

    /**
     * The extracted closed chronicle.
     */
    private Chronicle _chronicle;

    /**
     * Duration of the extraction phase.
     */
    private long _dextraction;

    /**
     * Minimum support.
     */
    private double _support;

    public double get_support() {
		return _support;
	}

	public void set_support(double _support) {
		this._support = _support;
	}

	/**
     * Input and output file.
     */
    private String _infile, _outfile;

    /**
     * Constructor.
     * 
     * @param support The minimum support to use for frequent sequence mining.
     * @param infile The input file name.
     * @param outfile The output file name.
     */
    public ChronicleMiningAlgo(double support, String infile, String outfile) {
        _support = support;
        _infile = infile;
        _outfile = outfile;
    }

    /**
     * Constructor. <br>
     * If output filename is not given, output file will be infile.out.
     * 
     * @param support The minimum support to use for frequence sequence mining.
     * @param infile The input file name.
     */
    public ChronicleMiningAlgo(double support, String infile) {
        this(support, infile, infile + ".out");
    }

    /**
     * Runs the algorithm.
     * 
     * @throws IOException Input or output file related error.
     */
    public void run() throws IOException {

        /******************************************************/
        /*******                                        *******/
        /******* CM-ClaSP algorithm setup and execution *******/
        /*******                                        *******/
        /******************************************************/

        // Load a sequence database
        double support = _support;

        boolean keepPatterns = true;
        boolean verbose = false;
        boolean findClosedPatterns = true;
        boolean executePruningMethods = true;
        
        // if you set the following parameter to true, the sequence ids of the sequences where
        // each pattern appears will be shown in the result
        boolean outputSequenceIdentifiers = true;

        AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
        IdListCreator idListCreator = IdListCreatorStandard_Map.getInstance();

        SequenceDatabase sequenceDatabase = new SequenceDatabase(abstractionCreator, idListCreator);

        // Loading the input file
        // Running the algorithm won't modify sequenceDatabase
        double relativeSupport = sequenceDatabase.loadFile(_infile, support);

        // Running the CloSpan algorithm
        final AlgoCM_ClaSP algorithm = new AlgoCM_ClaSP(relativeSupport, abstractionCreator, findClosedPatterns, executePruningMethods);

        try {
            algorithm.runAlgorithm(sequenceDatabase, keepPatterns, verbose, null, outputSequenceIdentifiers);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Write closed frequent sequences to a .cfs file
        new Thread() {
            public void run() {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(_outfile + ".cfs")))) {
                    bw.write(algorithm.printStatistics());
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
            }
        }.run();

        /***************************************************************/
        /*******                                                 *******/
        /******* Time constraints extraction setup and execution *******/
        /*******                                                 *******/
        /***************************************************************/

        System.gc();
        // Start time of our procedure
        long start = System.currentTimeMillis();

        // Get the patterns from the algorithm
        SaverIntoMemory saver = (SaverIntoMemory) algorithm.saver;
        Sequences patterns = saver.getSequences();

        // Populate the queue for time extraction, set the trace pool for chronicle builder consumption and
        // set the chronicle pool for the chronicle writer consumption.
        TimeConstraintExtractor.populateQueue(patterns);
        ChronicleBuilder.setTracePool(TimeConstraintExtractor.getTracePool());
        ChronicleWriter cw = new ChronicleWriter(_outfile + ".chronicles", ChronicleBuilder.getChroniclePool());

        // We want the joint chronicle
        TimeConstraintExtractor.setJoin(true);

        // Define number of threads and run them
        final int EXTRACTOR_THREADS = 4;
        final int BUILDER_THREADS = 2;
        Thread[] extractors = new Thread[EXTRACTOR_THREADS];
        Thread[] builders = new Thread[BUILDER_THREADS];
        Thread writer = new Thread(cw);

        for (int i = 0; i < EXTRACTOR_THREADS; i++) {
            TimeConstraintExtractor tce = new TimeConstraintExtractor(sequenceDatabase);
            extractors[i] = new Thread(tce);
            extractors[i].start();
        }

        for (int i = 0; i < BUILDER_THREADS; ++i) {
            ChronicleBuilder cb = new ChronicleBuilder();
            builders[i] = new Thread(cb);
            builders[i].start();
        }

        writer.start();

        // Wait for threads to finish, i.e. to complete all the tasks in the task queue and
        // building chronicles
        for (int i = 0; i < EXTRACTOR_THREADS; ++i) {
            try {
                extractors[i].join();
            } catch (InterruptedException ie) {}
        }

        ChronicleBuilder.finish();

        for (int i = 0; i < BUILDER_THREADS; ++i) {
            try {
                builders[i].join();
            } catch (InterruptedException ie) {}
        }

        cw.finish();

        _dextraction = System.currentTimeMillis() - start;

        System.out.println("Time Constraints Extraction terminated. Time elapsed: " + _dextraction  + " ms");

        _chronicle = new Chronicle(new PatternTracePair(null, TimeConstraintExtractor.getTrace()));

        // Write the joint chronicle
        new Thread() {
            @Override
            public void run() {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(_outfile + ".jchronicle")))) {
                    bw.write(_chronicle.toString());
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
            }
        }.run();
    }

    /**
     * Returns the actual file path for the given filename.
     * 
     * @param filename A file's name.
     * 
     * @throws UnsupportedEncodingException Whether the encoding of the file is supported or not.
     * 
     * @return The actual file path for the given filemane.
     */
    public String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = ChronicleMiningAlgo.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

    /**
     * Returns the chronicle produced by this algorithm.
     * 
     * @return The chronicle produced by this algorithm.
     */
    public Chronicle getChronicle() {
        return _chronicle;
    }
}