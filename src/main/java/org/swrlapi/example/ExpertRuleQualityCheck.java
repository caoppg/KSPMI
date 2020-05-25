package org.swrlapi.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;

public class ExpertRuleQualityCheck {

	public static void main(String[] args) throws OWLOntologyStorageException, SWRLParseException, SWRLBuiltInException, IOException
	  {
	    if (args.length > 1)
	      Usage();

	    Optional<String> owlFilename = args.length == 0 ? Optional.<String>empty() : Optional.of(args[0]);
	    Optional<File> owlFile = (owlFilename != null && owlFilename.isPresent()) ?
	      Optional.of(new File(owlFilename.get())) :
	      Optional.<File>empty();

	    try {
	      // Load the MFPO ontology using the OWLAPI
	      OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	      // load ontology with synthetic data
	      OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument
	      		  (new File("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\MFPO.owl"));
	      
	      //load ontology for chronicle reasoning
	      //   OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument
	    	//          (new File("C:\\Users\\caoqs\\Desktop\\EKAW\\CMMP10.29.owl"));

	      // Create SQWRL query engine using the SWRLAPI
	      SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

	      // Create a rule engine for mined rules
	      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
	      //read the text file for the chronicle rule base
	      File file = new File("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\rule base.txt");       
	      BufferedReader br = new BufferedReader(new FileReader(file));    
	      ArrayList<String> minedrulebase = new ArrayList<String>();
	      String st; 
	      while ((st = br.readLine()) != null) 
	      minedrulebase.add(st);
	      
	      //String r1 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
	      //String r2 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
	      //String r3 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
	      //String r4 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,1) ^ hasMaxTimetoFailure(?f, 5)";
	      //String r5 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 6)";
	      //String r6 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 4) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,3) ^ hasMaxTimetoFailure(?f, 6)";
	      //String r7 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 3) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 4) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,3) ^ hasMaxTimetoFailure(?f, 8)";
	      //String r8 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 8) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,1) ^ hasMaxTimetoFailure(?f, 6)";
	      
	        //Expert rule as input
	        String er; 
			BufferedReader brer = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Expert rule as input:"); 
		    er = brer.readLine();
		    
			//check redundancy issue with each chronicle rule 
			int k=0;
			int kk=0;
			for (int i = 0; i < minedrulebase.size(); i++) {
			if (er.equals(minedrulebase.get(i))) {
			   System.out.println("Expert rule redundant with the rule with index "+(i+1));
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
			String[] erparts = er.split("->");
			String erpart1 = erparts[0]; // antecedent of the expert rule
			String erpart2 = erparts[1]; // consequent of the expert rule
			for (int i = 0; i < minedrulebase.size(); i++) {
			//split the rules in the chronicle rule base
			String[] crparts = minedrulebase.get(i).split("->");
			String crpart1 = crparts[0]; // antecedent of the chronicle rule
			String crpart2 = crparts[1]; // consequent of the chronicle rule	
			//check rule conflict between two rules
			if (erpart1.equals(crpart1) && !erpart2.equals(crpart2)) {
			   System.out.println("Expert rule conflicts with the rule with index "+(i+1));
			   //remove the conflict chronicle rule in the CR rule base
			   minedrulebase.remove(new String(minedrulebase.get(i)));
			   kk=0;
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
				   System.out.println("Chronicle rule with index " +(i+1) + " subsums the expert rule"); 
				   kk=1;
				} 
				//chronicle rule antecedent longer, more specific. er subsums cr
				else if (erpart2.equals(crpart2) && crpart1.contains(erpart1) && !crpart1.equals(erpart1)) {
				//remove subsumed cr
				   System.out.println("The expert rule subsums the chronicle rule with index " +(i+1)); 
				minedrulebase.remove(new String(minedrulebase.get(i)));
				kk=0;
				}
				}
			
			    //no redundancy and conflict issue, add the expert rule
			    if (kk==0) {
			    minedrulebase.add(er);
			    }
			   FileWriter writer = new FileWriter("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\rule base.txt"); 
			   for(String str: minedrulebase) {
			        writer.write(str + System.lineSeparator());
			      }
			   writer.close();
			System.out.println("------------------------------------");
			System.out.println("The integrated rule base has been updated");
			
			
			
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
		         SWRLAPIRule CR= ruleEngine.createSWRLRule(rulename.toString(), minedrulebase.get(i));
		         }
	        ruleEngine.infer();
	      
	      // Save the inferred ontology
	      File fileout = new File("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\inferredMFPO.owl");
	      ontologyManager.saveOntology(ontology, new FunctionalSyntaxDocumentFormat(),
	      new FileOutputStream(fileout));
	      System.out.println("The inferred ontology has been saved");
	      System.out.println("------------------------------------");
	       
	    } catch (OWLOntologyCreationException e) {
	      System.err.println("Error creating OWL ontology: " + e.getMessage());
	      System.exit(-1);
	    } catch (RuntimeException e) {
	      System.err.println("Error starting application: " + e.getMessage());
	      System.exit(-1);
	    }
	    //call SWQRL tab
	    FailurePredictionResults.main(args);
	  }
	     

	  
	  private static void Usage()
	  {
	    System.err.println("Usage: " + ReasonwithInitialRuleBase.class.getName() + " [ <owlFileName> ]");
	    System.exit(1);
	  }
	}