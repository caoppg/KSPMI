package halfback.prototype.experiencecapitalization;

/**
 * Expert rule integration algorithm and interface.
 * 
 * @author <qiushi.cao@insa-rouen.fr">Qiushi Cao</a>
 */

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Event;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.util.Args;
import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.mvel2.ast.RedundantCodeException;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.example.FailurePredictionResults;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;

import halfback.cmpm.failurepreinterface.FailurePrediction;
import halfback.prototype.rulepruning.ReconstructAccCov;
import java_cup.internal_error;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ExpertRuleIntegrationInterface extends JFrame {
	
	//input expert rule
//	private static String eri;
	
//	private static String issue;
	
//	private static String redundant;


	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExpertRuleIntegrationInterface frame = new ExpertRuleIntegrationInterface();
					frame.setTitle("HALFBACK Prototype: Experience Capitalization - Integration of Expert Rules");
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	


	/**
	 * Create the frame.
	 */
	public ExpertRuleIntegrationInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 695, 1);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(84, 270, 723, 268);
		contentPane.add(scrollPane_1);
		
		//text area 1 is the output results
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		textArea_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(86, 69, 721, 50);
		contentPane.add(scrollPane);
		
		//text area 2 listens to expert rule input
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		scrollPane.setViewportView(textArea_2);
		
		JButton btnNewButton = new JButton("Open Expert Rules");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open file chooser dialog
				JFileChooser jfc = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				jfc.setCurrentDirectory(workingDirectory);
				jfc.showOpenDialog(null);
				//select file
				File expertruleFile = jfc.getSelectedFile();
				//open the txt file
				if(expertruleFile.exists()) {
					try {
						Desktop.getDesktop().open(expertruleFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(86, 163, 171, 36);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Input Expert Rule");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                textArea_1.setText("");
				
				//text input of the expert rule
                String eri = textArea_2.getText();
				String eriwhole = "Expert rule input: " + textArea_2.getText();
				//show typed rule
				textArea_1.append(eriwhole + "\n");
				
				try {
					//display is the string shows the issue detection results
					String display = ExpertRuleCheck(eri);
					textArea_1.append("----------------------------------------------------------------------------------" + "\n");
					textArea_1.append(display + "\n");
					textArea_1.append("----------------------------------------------------------------------------------" + "\n");
					textArea_1.append("The integrated rule base has been updated");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//no redundant, check
				
				
				textArea_2.setText("");
			}
		});
			
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_1.setBounds(360, 139, 164, 36);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Start Prediction");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//run the prediction interface
				String[] emptyArray = new String[0];
				long starttimeFront = System.currentTimeMillis();
				try {
					
					FailurePrediction.main(emptyArray);
					
				} catch (OWLOntologyStorageException | SWRLParseException | SWRLBuiltInException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				long endtimefront   = System.currentTimeMillis();
		        long totaltimefront = endtimefront - starttimeFront;
				JOptionPane.showMessageDialog(null, "Failure prediction results have been saved in the ontology.\nTime consumption for ontology reasoning: " + totaltimefront + " Milliseconds.\nRun the SQWRL query to retrieve prediction results.");
				
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_2.setBounds(636, 163, 171, 36);
		contentPane.add(btnNewButton_2);
		
		JLabel lblExpertRuleIntegration = new JLabel("Expert Rule Integration Output");
		lblExpertRuleIntegration.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblExpertRuleIntegration.setBounds(86, 239, 235, 30);
		contentPane.add(lblExpertRuleIntegration);
		
		
		JLabel lblPleaseEnterExpert = new JLabel("Please Enter Expert Rule Here");
		lblPleaseEnterExpert.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPleaseEnterExpert.setBounds(84, 35, 237, 30);
		contentPane.add(lblPleaseEnterExpert);
		
		JButton btnNewButton_3 = new JButton("Clear");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 textArea_1.setText("");
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_3.setBounds(360, 211, 164, 36);
		contentPane.add(btnNewButton_3);
		
		
	}
	
	public static String ExpertRuleCheck(String eri) throws IOException {
		  String issue = null; 
		  File file = new File("Pruned SWRL Rule Base.txt");       
	      BufferedReader br = new BufferedReader(new FileReader(file));    
	      ArrayList<String> minedrulebase = new ArrayList<String>();
	      String st; 
	      while ((st = br.readLine()) != null) 
	      minedrulebase.add(st);
		//check redundancy issue with each chronicle rule 
		int k=0;
		//kk=0, both conflict and expert rule subsums chronicle, both add.
		int kk=0;
		//kkconf for conflict, 0 = conflict expert rule, add.
		int kkconf=0;
		//kksubsum for conflict, 0 = conflict expert rule, add.
		int kksubsum=0;
		for (int i = 0; i < minedrulebase.size(); i++) {
		if (eri.equals(minedrulebase.get(i))) {
		   issue = "Input expert rule is redundant with the rule with index "+(i+1) +", \nthe expert rule has been discarded.";
		   System.out.println(issue);
		   //redundancy issue
		   k=1;
		   kk=kk+k;
		}
		else {
			//no redundancy issue
		   k=0;
		   kk=kk+k;
		}
		}

        //check conflict issue with each chronicle rule
		int j=0;
		//split the expert rule
		String[] erparts = eri.split("->");
		String erpart1 = erparts[0]; // antecedent of the expert rule
		String erpart2 = erparts[1]; // consequent of the expert rule
		for (int i = 0; i < minedrulebase.size(); i++) {
		//split the rules in the chronicle rule base
		String[] crparts = minedrulebase.get(i).split("->");
		String crpart1 = crparts[0]; // antecedent of the chronicle rule
		String crpart2 = crparts[1]; // consequent of the chronicle rule	
		//check rule conflict between two rules
		if (erpart1.equals(crpart1) && !erpart2.equals(crpart2)) {
		   issue = "Input expert rule has conflict with the chronicle rule with index "+(i+1) +", \nchronicle rule " + (i+1) + " has been removed from the rule base, \nthe expert rule has been added to the rule base.";
		   System.out.println(issue);
		   //remove the conflict chronicle rule in the CR rule base
		   minedrulebase.remove(new String(minedrulebase.get(i)));
		   kk=0;
		   kkconf=1;
		} 	
		}
		
		//check subsumption issue, case one string subsumes
		for (int i = 0; i < minedrulebase.size(); i++) {
			//split the rules in the chronicle rule base
			String[] crparts = minedrulebase.get(i).split("->");
			String crpart1 = crparts[0]; // antecedent of the chronicle rule
			String crpart2 = crparts[1]; // consequent of the chronicle rule	
			//check rule subsumption between two rules
			
			//expert rule antecedent longer, more specific. cr subsums er
			if (erpart2.equals(crpart2) && erpart1.contains(crpart1) && !erpart1.equals(crpart1)) {
			   issue = "Chronicle rule with index " +(i+1) + " subsums the expert rule, the expert rule has been discarded.";
			   System.out.println(issue); 
			 //  System.out.println("Chronicle rule with index " +(i+1) + " subsums the expert rule, the expert rule has been discarded."); 
			   kk=1;
			} 
			//chronicle rule antecedent longer, more specific. er subsums cr
			else if (erpart2.equals(crpart2) && crpart1.contains(erpart1) && !crpart1.equals(erpart1)) {
			//remove subsumed cr
			   issue = "The expert rule subsums the chronicle rule with index " +(i+1) +", \nchronicle rule " + (i+1) + " has been removed from the rule base, \nthe expert rule has been integrated into the rule base.";
			   System.out.println(issue); 
			minedrulebase.remove(new String(minedrulebase.get(i)));
			kk=0;
			kksubsum=1;
			} 
			}
		
		    //no redundancy and conflict issue, add the expert rule, print what it should be
		    if (kk==0 && kkconf == 1 && kksubsum == 1) {
		    minedrulebase.add(eri);
		    } 
		    //conflict, still add expert rule
		    else if (kk==0 && kkconf == 1 && kksubsum == 0) {    
		    	 minedrulebase.add(eri);
		    }	
		    //expert rule subsums chronicle rule, add
		    else if (kk==0 && kkconf == 0 && kksubsum == 1) {    
		    	 minedrulebase.add(eri);
		    }
		    // no issue detected, issue print "no issue detected"
		    else if (kk==0 && kkconf == 0 && kksubsum == 0) {
		    	 issue = "No issue detected, the expert rule is integrated into the rule base.";
		    	 minedrulebase.add(eri);
		    }
		   FileWriter writer = new FileWriter("Pruned SWRL Rule Base.txt"); 
		   for(String str: minedrulebase) {
		        writer.write(str + System.lineSeparator());
		      }
		   writer.close();
		System.out.println("------------------------------------");
		System.out.println("The integrated rule base has been updated.");
		
		
		
		//print the integrated rule base 
		for (String x: minedrulebase)
		System.out.println(x);
	    System.out.println("------------------------------------");
        //Run the SWRL rules in the ontology		    
	    //create map for rule names
	      StringBuilder sb = new StringBuilder();
	      StringBuilder rulename = new StringBuilder();
	      for(int i = 0; i < minedrulebase.size(); i++) {
	         //add i to end of rule name
	         sb.append(i);
	         rulename.append(i);
	         //add the rule to rule base
	 //        SWRLAPIRule CR= ruleEngine.createSWRLRule(rulename.toString(), minedrulebase.get(i));
	         }
    //    ruleEngine.infer();
      
      // Save the inferred ontology
  //    File fileout = new File("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\inferredMFPO.owl");
   //   ontologyManager.saveOntology(ontology, new FunctionalSyntaxDocumentFormat(),
   //   new FileOutputStream(fileout));
   //   System.out.println("The inferred ontology has been saved");
	      return issue;
	}
	
	public static void RunFailurePrediction(String[] args) throws OWLOntologyStorageException, SWRLParseException, SWRLBuiltInException, IOException {
		FailurePrediction FP = new FailurePrediction();
		FP.main(args);
	}
	
	
	
	
	
	
	
	
	
}
