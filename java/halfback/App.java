package halfback;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import halfback.Utils;
import halfback.cmpm.ChronicleMiningAlgo;
import halfback.cmpm.chronicletoSWRLrules.ChroniclestoSWRL;
import halfback.cmpm.model.ChronicleBuilder;
import halfback.cmpm.chronicletoSWRLrules.*;
import halfback.cmpm.view.ChronicleDescription;
import halfback.cmpm.view.MainFrame;
import halfback.cmpm.view.MergeTwoMeasures;
import preprocessing.Preprocessing;
import halfback.cmpm.model.Chronicle;
import halfback.cmpm.controller.ChronicleTableModel;
import halfback.cmpm.controller.ChronicleTableEntry;

public class App {
	
	private static String CovfilepathString;
	
	private static String AccfilepathString;
	
	static String infile;
	
	static String outfile;

	

	public String getInfile() {
		return infile;
	}



	public void setInfile(String infile) {
		this.infile = infile;
	}



	public static String getOutfile() {
		return outfile;
	}



	public void setOutfile(String outfile) {
		this.outfile = outfile;
	}



	public static String getCovfilepathString() {
		return CovfilepathString;
	}



	public static void setCovfilepathString(String covfilepathString) {
		CovfilepathString = covfilepathString;
	}



	public static String getAccfilepathString() {
		return AccfilepathString;
	}



	public static void setAccfilepathString(String accfilepathString) {
		AccfilepathString = accfilepathString;
	}



	public static void main(String args[]) throws NullPointerException, IOException {

		infile = Utils.getParam(args, "if");
		outfile = Utils.getParam(args, "of");
		Boolean preprocess = Utils.getBooleanParam(args, "p");
		if (infile == null) {
			throw new NullPointerException();
		} else {
			File file = new File(infile);
			if (!file.exists()) {
				throw new NullPointerException();
			}
			if (!file.isAbsolute()) {
				infile = file.getAbsolutePath();
			}
		}

		if (outfile == null) {
			outfile = System.getProperty("user.dir") + "\\output";
		} else {
			File file = new File(outfile);
			boolean overwrite = Utils.getBooleanParam(args, "f");
			if (file.exists() && !overwrite) {
				throw new NullPointerException();
			} else {
				outfile = file.getAbsolutePath();
			}
		}
		
		if (preprocess) {
			System.out.println("Running preprocessing task...");
			Preprocessing p = new Preprocessing();
			try {
				p.run(infile);
				infile = outfile + "_xd.txt";
				p.toSPMF(infile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Preprocessing ended.");
		} else {
			// TODO : read 
		}

		ChronicleMiningAlgo cm = new ChronicleMiningAlgo(Utils.getDoubleParam(args, "m", 0.9), infile, outfile);
		try {
			System.out.println("Running extraction algorithm.");
			cm.run();
			System.out.println("Extraction ended.");
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		}

		//write the Acc and Cov into a file, with support					
	//	MergeTwoMeasures mergetwomeasureobject = new MergeTwoMeasures();
	//	mergetwomeasureobject.mergeacccov();
	//	File CovOldFile = new File("Cov_.txt");
	//	File CovNewFile = new File("Acc+Cov_" + Utils.getDoubleParam(args, "m", 0.9) + ".txt");
	//	CovOldFile.renameTo(CovNewFile);
		
		
		
		//save the mined acc+cov, in the file filepathString.
//		measuresfilepathString = "Acc+Cov_" + Utils.getDoubleParam(args, "m", 0.9) + ".txt";
		
		//call the main frame, first GUI (chronicle mining)
		MainFrame mf = new MainFrame(ChronicleBuilder.getChronicles());
		
		//call the rule transformation method
		
		
		//call the interface

	}
}
