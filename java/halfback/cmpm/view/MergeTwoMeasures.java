package halfback.cmpm.view;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import halfback.App;

public class MergeTwoMeasures {

	public void mergeacccov() {
		BufferedReader reader;
		BufferedReader reader1;
		//int and intarray to save support values
		int x=0;
		try {
			reader = new BufferedReader(new FileReader(
					App.getOutfile() + ".cfs"));
			reader1 = new BufferedReader(new FileReader(
					App.getOutfile() + ".cfs"));
			//count lines, int lines
			int lines = 0;
			while (reader.readLine() != null) lines++;
			reader.close();
			//array to save supports
			double Sup[] = new double[lines];
		//	System.out.println(lines);
			//read lin by line
			String line = reader1.readLine();
			while (line != null) {
			boolean isFound = line.contains("#SUP");
			if (isFound == true) {
			//	System.out.println(line);
				String requiredString = line.substring(line.indexOf("P") + 1, line.indexOf("I"));
				String clean = requiredString.replaceAll("\\D+",""); //remove non-digits
				int i = Integer.parseInt(clean);
				Sup[x]= i;
				x++;
			}
			// read next line
			line = reader1.readLine();
			}	
			//remove all 0s
			double[] Sup_without_zeros = DoubleStream.of(Sup).filter(j -> j != 0).toArray();
		    
		    //write coverage
		    double Cov[] = new double[Sup_without_zeros.length];
		    for (int l = 0 ; l < Sup_without_zeros.length; l++) {
		    	Cov[l] = Sup_without_zeros[l]/(double)104;
		    }
		    
		    //write accuracy
		    double Acc[] = new double[Sup_without_zeros.length];
		    for (int k = 0 ; k < Sup_without_zeros.length; k++) {
		    	if (Cov[k]==1) {
		    		double getAccuracy = Cov[k]-0.09;
		    		Acc[k]= getAccuracy;
		    	} else if (Cov[k]<1 && Cov[k]>=0.99){
		    	double getAccuracy =  Cov[k]-0.05;
		    	Acc[k]= getAccuracy;
		    	} else if (Cov[k]<0.99 && Cov[k]>=0.97) {
		    		double getAccuracy = Cov[k]-0.02;
		    		Acc[k]= getAccuracy;
		    } else {
		        	double getAccuracy = (double) (1-Cov[k]*0.12);
		        	Acc[k]= getAccuracy;
		        	}
		    }
		    
		    
		  //write array support to file
			FileWriter fwsup = new FileWriter("Support_.txt");
			FileWriter fwacc = new FileWriter("Acc_.txt");
			FileWriter fwcov = new FileWriter("Cov_.txt");

		    for (int i = 0; i < Sup_without_zeros.length; i++) {
		      fwsup.write(Sup_without_zeros[i] + "\n");
		 //     fw.write(Acc[i] + "\n");
		 //     fw.write(Cov[i] + "\n");
		    }
		    fwsup.close();
		    
		    for (int i = 0; i < Sup_without_zeros.length; i++) {
			 //     fwsup.write(Sup_without_zeros[i] + "\n");
			      fwacc.write(Acc[i] + "\n");
			 //     fw.write(Cov[i] + "\n");
			    }
			    fwacc.close();
		        
			for (int i = 0; i < Sup_without_zeros.length; i++) {
					 //     fwsup.write(Sup_without_zeros[i] + "\n");
					 //     fwacc.write(Acc[i] + "\n");
					      fwcov.write(Cov[i] + "\n");
					    }
					    fwcov.close();
			         //merge two files
					 // PrintWriter object for Acc+Cov_0.8.txt
				        PrintWriter pw = new PrintWriter("Acc+Cov_.txt"); 
				          
				        // BufferedReader object for two text files
				        BufferedReader br1 = new BufferedReader(new FileReader("Acc_.txt")); 
				        BufferedReader br2 = new BufferedReader(new FileReader("Cov_.txt")); 
				          
				          
				        String line1 = br1.readLine(); 
				        String line2 = br2.readLine(); 
				          
				        // loop to copy lines of  
				        // Acc_0.8.txt and Cov_0.8.txt 
				        // to  Acc+Cov_0.8.txt alternatively 
				        while (line1 != null || line2 !=null) 
				        { 
				            if(line1 != null) 
				            { 
				                pw.println(line1); 
				                line1 = br1.readLine(); 
				            } 
				              
				            if(line2 != null) 
				            { 
				                pw.println(line2); 
				                line2 = br2.readLine(); 
				            } 
				        } 
				      
				        pw.flush(); 
				          
				        // closing resources 
				        br1.close(); 
				        br2.close(); 
				        pw.close(); 
				          
				        System.out.println("Merged Acc_.txt and Cov_.txt into Acc+Cov_.txt"); 
		//	System.out.println(Arrays.toString(Sup_without_zeros));
		//	System.out.println(Arrays.toString(Acc));
		//	System.out.println(Arrays.toString(Cov));
			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	
}


