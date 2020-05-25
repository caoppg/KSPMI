package halfback.prototype.chronicletoSWRLrules;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;

import halfback.prototype.SoftwareApp;

/**
 * SWRL rule transformation algorithm.
 * 
 * @author <qiushi.cao@insa-rouen.fr">Qiushi Cao</a>
 */

public class ChroniclestoSWRL {
	
	private String rulefilepathString; 
	
	private long totalTime;
	
	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public void ruletransform() throws IOException {
		// start the rule transformation process?
				Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		//	    System.out.println("Start the SWRL rule transformation process? (Y/N)");   
			//    String answer = myObj.nextLine();  // Read user input
			    String answer = "Y";
			    if(answer.equals("Y")) {  
			    long startTime = System.currentTimeMillis();
			    
			    
			    //copy the chronicle file to a .txt, SECOM data set
			//    D:\PhD\eclipse-workspace\cpmwithQualityMeasures\data\output
			    File oldFile =new File(SoftwareApp.getOutfile() + ".chronicles");
			    File newFile =new File("resultchronicles.txt");
			    copyFileUsingNIO(oldFile,newFile);	    
			    File file = new File("resultchronicles.txt");       
			    
			    
			    /**
			    //copy the chronicle file to a .txt, Synthetic data set
			    File oldFile =new File("C:\\Users\\caoqs\\Desktop\\CPM\\data\\output\\db1000_ss20_dic20_items5_out.txt.chronicles");
			    File newFile =new File("C:\\Users\\caoqs\\Desktop\\CPM\\data\\output\\resultsyntheticchronicles.txt");
			    copyFileUsingNIO(oldFile,newFile);	    
			    File file = new File("C:\\Users\\caoqs\\Desktop\\CPM\\data\\output\\resultsyntheticchronicles.txt");
			    */
			    
			    
			    //read chronicle txt file
			    BufferedReader br = new BufferedReader(new FileReader(file)); 
				//arraylist for chronicles
				ArrayList<String> chronicles = new ArrayList<String>();
				String st; 
				while ((st = br.readLine()) != null) 
				chronicles.add(st);
				//read the chronicle txt file, i count for no. of chronicles
			//	for (String x: chronicles)
			//		System.out.println(x);
			//	System.out.println("------------------------------------");
				//System.out.println(chronicles.get(0).toString());	
				ArrayList<String> rules = new ArrayList<String>();
				int index;
				for (int i = 0; i < chronicles.size()-1; i++) {
				//exist temporal constraints
					String y1=chronicles.get(i);
					String y2=chronicles.get(i+1);
					//ttf, next line blank, end of chronicle
					if (y1.contains("0 _") && y2.length() == 0) {
					//index of last ","
					index=y1.lastIndexOf(",", 99999);
					//split at the "," 
					String mintime = y1.substring(index-1, index);
					String maxtime = y1.substring(index);
					//max time to failure
					String maxtime2 =maxtime.substring(1);		
				//	System.out.println(maxtime2);
					//not the first line
					if (i>0 ) {		
						int indexy4;
						int indexy4more;
						int indexy4last;
						int indexntc;
						//previous line
						String y3=chronicles.get(i-1);
						//i>0, previous line not empty, means a long chronicle
				        if (y3.length() != 0) {
				        //i-2 line
				        String y4=chronicles.get(i-2);
				        //index of last ",", construct the events
						indexy4=y4.lastIndexOf("\",", 99999);
						//index of last ",", construct the ntc of normal events
						indexntc=y4.lastIndexOf(",", 99999);
						//split at the "," 
						String minntc = y4.substring(indexntc-1, indexntc);
						String maxntc = y4.substring(indexntc);
						String ruleanteini = "ManufacturingProcess(?s) ^ hasEvent(?s,?e1) ^ hasEvent(?s,?e2) ^ hasTimeInterval(?s,?t) ^ hasSubEvent(?t, ?e2) ^ hasProEvent(?t, ?e1) ";
						String ruleantenfc = ruleanteini + "^ hasLowerBound(?t, ?lb) ^ swrlb:equal(?lb," + minntc + ")" + 
						"^ hasUpperBound(?t, ?lb) ^ swrlb:equal(?lb," + maxntc + ")";
						//split at the ",", do for the events
						String y4first = y4.substring(0,indexy4);
						//split again
						indexy4more = y4first.lastIndexOf("\"", 99999);
						indexy4last = y4first.lastIndexOf("_", 99999);
						//numbers for first event
						String firstevent = y4first.substring(1,indexy4more-5);
						//numbers for second event
						String secondevent = y4first.substring(indexy4more+1,indexy4last-1);
						//tranform string to rule antecedent
						String[] strArrfe = firstevent.split("\\s+");
						String[] strArrse = secondevent.split("\\s+");
					//	for (String x: strArrfe)
					//		System.out.println(x);
						String rulefeatomall = "";
						String ruleseatomall = "";
						for (int j = 0; j < strArrfe.length; j++) {
							String rulefeatom = " ^ hasItem(?e1," + strArrfe[j] + ")";
							rulefeatomall = rulefeatomall + rulefeatom;
						}
						for (int jj = 0; jj < strArrse.length; jj++) {
						String ruleseatom  = " ^ hasItem(?e2," + strArrse[jj] + ")";
						ruleseatomall = ruleseatomall + ruleseatom;
						}
					String ruleante = ruleantenfc + rulefeatomall + ruleseatomall;
			//		System.out.println(ruleante);
					
					
				    //previous line, ttf
				    index=y3.lastIndexOf(",", 99999);
				    String mintimey3 = y3.substring(index-1, index);
					String maxtimey3 = y3.substring(index);
					String maxtimey32 =maxtimey3.substring(1);
					
		        	//write the tc into string		
					String minF = "hasMinF(?e2, " + mintime + ")" + " ^ hasMinF(?e1, " + mintimey3 + ")";
					String maxF = "hasMaxF(?e2, " + maxtime2 + ")" + " ^ hasMaxF(?e1, " + maxtimey32 + ")";
					String consequent = " -> " + minF + "^" + maxF;
					String rule = ruleante + consequent;
					rules.add(rule);
		            
				//	System.out.println(consequent);
				        } else {
				        	//previous line empty, simple chronicle, i>0       	
				        	String ruleanteini = "ManufacturingProcess(?s) ^ hasEvent(?s, ?e1) ";
				        	int indexsimple=y1.lastIndexOf(" _0", 99999);
							//split at the "," 
							String eventsimplenum = y1.substring(1, indexsimple);
							//max time to failure
							String[] strArrfe = eventsimplenum.split("\\s+");
							String rulesimpleatomall = "";
							for (int j = 0; j < strArrfe.length; j++) {
								String rulesimpleatom = " ^ hasItem(?e1," + strArrfe[j] + ")";
								rulesimpleatomall = rulesimpleatomall + rulesimpleatom;
							}
				        	
				        	String minF = "hasMinF(?e1, " + mintime + ")";
							String maxF = "hasMaxF(?e1, " + maxtime2 + ")";	
							String consequent = "-> " + minF + "^" + maxF;
					//		System.out.println(consequent);
							String rule = ruleanteini + rulesimpleatomall + consequent;
							rules.add(rule);
				        }
				       
					}
				        else {
				        	//i=0, first line always simple chronicle
				        	String ruleanteini = "ManufacturingProcess(?s) ^ hasEvent(?s, ?e1) ";
				        	int indexsimple=y1.lastIndexOf(" _0", 99999);
							//split at the "," 
							String eventsimplenum = y1.substring(1, indexsimple);
					//		System.out.println(eventsimplenum);
					//		System.out.println(eventsimplenum.length());
							//max time to failure
							String[] strArrfe = eventsimplenum.split("\\s+");
							String rulesimpleatomall = "";
							for (int j = 0; j < strArrfe.length;j++) {
								String rulesimpleatom = " ^ hasItem(?e1," + strArrfe[j] + ")";
								rulesimpleatomall = rulesimpleatomall + rulesimpleatom;			
							}	        	
				        	String minF = "hasMinF(?e1, " + mintime + ")";
							String maxF = "hasMaxF(?e1, " + maxtime2 + ")";	
							String consequent = " -> " + minF + " ^ " + maxF;
					//		System.out.println(consequent);
							String rule = ruleanteini + rulesimpleatomall + consequent;
							rules.add(rule);
					}		
				}
					
			    }
		//		for (String z: rules)
		//			System.out.println(z);
				
				//the transformed rule file
				FileWriter writer = new FileWriter("Transformed SWRL Rules.txt"); 
				   for(String str: rules) {
				        writer.write(str + System.lineSeparator());
				      }
				   writer.close();
				System.out.println("------------------------------------");
				System.out.println("The transformed SWRL rules have been saved in a txt file");
				System.out.println("------------------------------------");
				
				//open the txt file on computer
		//		File rulefile = new File("C:\\Users\\caoqs\\Desktop\\SWJ papaer\\Transformed SWRL Rules.txt");
		//		Desktop desktop = Desktop.getDesktop();
		//        if(rulefile.exists()) desktop.open(rulefile);
		        
		        
		        //count SWRL rule trasnformation algorihtm running time
		        long endTime   = System.currentTimeMillis();
		        totalTime = endTime - startTime;
		//        System.out.println("The time consumption of the SWRL rule transformation algorihtm is: " + totalTime + " milliseconds");
			    
			    }
			    
		}
			       
			private static void copyFileUsingNIO(File sourceFile, File destinationFile) throws IOException {
		        FileInputStream inputStream = new FileInputStream(sourceFile);
		        FileOutputStream outputStream = new FileOutputStream(destinationFile);
		        FileChannel inChannel = inputStream.getChannel();
		        FileChannel outChannel = outputStream.getChannel();
		        try {
		            inChannel.transferTo(0, inChannel.size(), outChannel);
		        } finally {
		            inChannel.close();
		            outChannel.close();
		            inputStream.close();
		            outputStream.close();
				
			}
			}
			

	}

