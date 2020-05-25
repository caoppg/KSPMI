package halfback.cmpm.failurepreinterface;

/**
 * Call the SWRL API+SQWRL API, use ontology to prediction
 * 
 * @author <qiushi.cao@insa-rouen.fr">Qiushi Cao</a>
 */

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

public class FailurePrediction {

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
	      		  (new File("Ontologies\\MFPO.owl"));
	   //   OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument
	   //   		  (new File("C:\\Users\\caoqs\\Downloads\\MCMO.owl"));
	      
	      //load ontology for chronicle reasoning
	      //   OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument
	    	//          (new File("C:\\Users\\caoqs\\Desktop\\EKAW\\CMMP10.29.owl"));

	      // Create SQWRL query engine using the SWRLAPI
	      SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

	      // Create a rule engine for mined rules
	      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
	      //read the text file for the chronicle rule base
	      File file = new File("Pruned SWRL Rule Base.txt");    
	 //     File file = new File("C:\\Users\\caoqs\\Desktop\\CyberneticsJournal2019\\Codes for expert rule integration\\rule base.txt");
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
	      File fileout = new File("Ontologies\\inferredMFPO.owl");
	      ontologyManager.saveOntology(ontology, new FunctionalSyntaxDocumentFormat(),
	      new FileOutputStream(fileout));
	      System.out.println("The inferred ontology has been saved");
//	      System.out.println("------------------------------------");
	       
	    } catch (OWLOntologyCreationException e) {
	      System.err.println("Error creating OWL ontology: " + e.getMessage());
	      System.exit(-1);
	    } catch (RuntimeException e) {
	      System.err.println("Error starting application: " + e.getMessage());
	      System.exit(-1);
	    }
	    //call SWQRL tab
	    FailurePredictionInterface.main(args);
	  }
	

	  
	  private static void Usage()
	  {
	    System.err.println("Usage: " + FailurePrediction.class.getName() + " [ <owlFileName> ]");
	    System.exit(1);
	  }
	}