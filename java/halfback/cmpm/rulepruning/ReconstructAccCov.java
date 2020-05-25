package halfback.cmpm.rulepruning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import halfback.cmpm.view.RuleTransGUI;

public class ReconstructAccCov {

	public static void main(String[] args) throws IOException {
		
		//created merged arraylist for Coverage and Accuracy
		ArrayList<String> Mergedvalues = new ArrayList<String>();
		//read Coverage value file
		File Covfile = new File("Cov_.txt"); 
		BufferedReader Covbr = new BufferedReader(new FileReader(Covfile)); 
		//arraylist for Coverage values
		ArrayList<String> Covvalues = new ArrayList<String>();
		String Covst; 
		while ((Covst = Covbr.readLine()) != null) 
	    Covvalues.add(Covst);
		
		//read Accuracy value file
		File Accfile = new File("Acc_.txt"); 
	    BufferedReader Accbr = new BufferedReader(new FileReader(Accfile)); 
		//arraylist for Coverage values
		ArrayList<String> Accvalues = new ArrayList<String>();
		String Accst; 
		while ((Accst = Accbr.readLine()) != null) 
	    Accvalues.add(Accst);
		
		//merge the two measures to a new arraylist
		for (int i = 0; i < Accvalues.size(); i ++) {
			
			Mergedvalues.add("-" + Covvalues.get(i) + " " + "-" + Accvalues.get(i));
			
		}
	//	System.out.println(Mergedvalues.toString());
		
		//write into file
		FileWriter writer = new FileWriter("Reconstructed_Cov+Acc.txt"); 
		for(String str: Mergedvalues) {
		  writer.write(str + System.lineSeparator());
		}
		writer.close();
		
		
		
		
		//extract the first pareto front
		ExtractFirstParetoFront.main(new String[] {"Reconstructed_Cov+Acc.txt", "2"});
		
		//delete duplicate lines
		PrintWriter pw = new PrintWriter("Reconstructed_final_Cov+Acc.txt"); 
		        
		// BufferedReader object for input.txt 
		BufferedReader br = new BufferedReader(new FileReader("Reconstructed_Cov+Acc.txt.pf")); 
		          
		String line = br.readLine(); 
		          
		// set store unique values 
		HashSet<String> hs = new HashSet<String>(); 
		          
		// loop for each line of input.txt 
		while(line != null) 
		        { 
		            // write only if not 
		            // present in hashset 
		            if(hs.add(line)) 
		                pw.println(line); 
		              
		            line = br.readLine(); 
		              
		        } 
		          
		        pw.flush(); 
		          
		        // closing resources 
		        br.close(); 
		        pw.close(); 
		          
		     //   System.out.println("File operation performed successfully"); 
		        
		      //find non-dominated rules
				 String first = "Reconstructed_Cov+Acc.txt";
			     String second = "Reconstructed_final_Cov+Acc.txt";
			     BufferedReader fBr = new BufferedReader(new FileReader(first));
			     BufferedReader sBr = new BufferedReader(new FileReader(second));
			     
			     
			     
			     
			     ArrayList<String> strings = new ArrayList<String>();
			  //   first = fBr.readLine();
			  //   String first11 = first.substring(0, 9);
			  //   System.out.println(first11.toString());

			     //strings is the value arraylist for the original rule file
			     while ((first = fBr.readLine()) != null && first.length()!=0) {
			    	// System.out.println(first.length());
			    	    String first5 = first.substring(0, 8);
			            strings.add(first5);
			     }
			        fBr.close();

			        
			        //strings 2 is the arraylist for the pruned rule file
			        ArrayList<String> strings2 = new ArrayList<String>();
			        
			        		        

			       //strings3 is the arraylist for the original rule file
			        ArrayList<String> strings3 = new ArrayList<>();	        
			        Scanner rulescanner = new Scanner(new FileReader("Transformed SWRL Rules.txt"));
			            while (rulescanner.hasNext()) {
			            	strings3.add(rulescanner.nextLine());
			            }
			       //     System.out.println(strings3.toString());
			        
			//		ArrayList<String> string3 = new ArrayList<String>();
			//		String strule; 
			//		while ((strule = brrule.readLine()) != null) {
			//		string3.add(strule);
				//	System.out.println(string3.toString());

				     while ((second = sBr.readLine()) != null && second.length()!=0) {
				    	 String first25 = second.substring(0, 8); 
				      //      strings2.add(first25);
				            if (strings.contains(first25)) {
				           // 	System.out.println(strings.indexOf(first25));
				            	
				            	
				            	//find each rule in the original rule file
				            	strings2.add(strings3.get(strings.indexOf(first25)));
				            	
				            			                
				            
				     }
				     }
				        sBr.close();
				        
			        
					
					//save the pruned rule file	
					FileWriter writer1 = new FileWriter("Pruned SWRL Rule Base.txt");
					   for(String str: strings2) {
					        writer1.write(str + System.lineSeparator());
					      }
					   writer1.close();
		
		

	}
}



