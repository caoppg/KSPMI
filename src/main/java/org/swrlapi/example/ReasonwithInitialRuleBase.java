package org.swrlapi.example;

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
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;

public class ReasonwithInitialRuleBase
{
  public static void main(String[] args) throws OWLOntologyStorageException, SWRLParseException, SWRLBuiltInException, IOException
  {
    if (args.length > 1)
      Usage();

    Optional<String> owlFilename = args.length == 0 ? Optional.<String>empty() : Optional.of(args[0]);
    Optional<File> owlFile = (owlFilename != null && owlFilename.isPresent()) ?
      Optional.of(new File(owlFilename.get())) :
      Optional.<File>empty();

    try {
      // Load an OWL ontology using the OWLAPI
      OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument
    		  (new File("C:\\Users\\caoqs\\Desktop\\Ontologies\\MPMO.owl"));

      // Create SQWRL query engine using the SWRLAPI
      SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

      // Create a rule engine for mined rules
      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
      String r1 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
      String r2 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
      String r3 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 5)";
      String r4 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,1) ^ hasMaxTimetoFailure(?f, 5)";
      String r5 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 1) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 5) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,2) ^ hasMaxTimetoFailure(?f, 6)";
      String r6 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 4) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,3) ^ hasMaxTimetoFailure(?f, 6)";
      String r7 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 3) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 4) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,3) ^ hasMaxTimetoFailure(?f, 8)";
      String r8 = "Event(?e) ^ TimeInterval(?ti) ^ hasMinBound(?ti, ?min) ^ swrlb:equal(?min, 2) ^ hasMaxBound(?ti, ?max) ^ swrlb:equal(?max, 8) ^ Follows(?ti, ?e) ^ Event(?f) ^ Follows(?f, ?ti) -> hasMinTimetoFailure (?f,1) ^ hasMaxTimetoFailure(?f, 6)";
      
      SWRLAPIRule rule1 = ruleEngine.createSWRLRule("CR1", r1);
      SWRLAPIRule rule2 = ruleEngine.createSWRLRule("CR2", r2);
      SWRLAPIRule rule3 = ruleEngine.createSWRLRule("CR3", r3);
      SWRLAPIRule rule4 = ruleEngine.createSWRLRule("CR4", r4);
      SWRLAPIRule rule5 = ruleEngine.createSWRLRule("CR5", r5);
      SWRLAPIRule rule6 = ruleEngine.createSWRLRule("CR6", r6);
      SWRLAPIRule rule7 = ruleEngine.createSWRLRule("CR7", r7);
      SWRLAPIRule rule8 = ruleEngine.createSWRLRule("CR8", r8);
      
      
      // Run the SWRL rules in the ontology
      ruleEngine.infer();
      
      // Save the inferred ontology
      File fileout = new File("C:\\Users\\caoqs\\Desktop\\Ontologies\\inferred.owl");
      ontologyManager.saveOntology(ontology, new FunctionalSyntaxDocumentFormat(),
      new FileOutputStream(fileout));
      System.out.println("The inferred ontology has been saved");
      System.out.println("------------------------------------");
      
    //  redundancy check
      System.out.println("Rule 1 and Rule 2 are redundant " + r1.equals(r2));
      System.out.println("Rule 1 and Rule 3 are redundant " + r1.equals(r3));
      System.out.println("Rule 1 and Rule 4 are redundant " + r1.equals(r4));
      System.out.println("Rule 1 and Rule 5 are redundant " + r1.equals(r5));
      System.out.println("Rule 2 and Rule 3 are redundant " + r2.equals(r3));
      System.out.println("Rule 2 and Rule 4 are redundant " + r2.equals(r4));
      System.out.println("Rule 2 and Rule 5 are redundant " + r2.equals(r5));
      System.out.println("Rule 3 and Rule 4 are redundant " + r3.equals(r4));
      System.out.println("Rule 3 and Rule 5 are redundant " + r3.equals(r5));
      System.out.println("Rule 4 and Rule 5 are redundant " + r4.equals(r5));
      System.out.println("------------------------------------");
      
      //subsumption check
      r1 =r1.replaceAll("[^\\.0123456789]","");
      r2 =r2.replaceAll("[^\\.0123456789]","");
      r3 =r3.replaceAll("[^\\.0123456789]","");
      r4 =r4.replaceAll("[^\\.0123456789]","");
      r5 =r5.replaceAll("[^\\.0123456789]",""); 
      r6 =r6.replaceAll("[^\\.0123456789]",""); 
      r7 =r7.replaceAll("[^\\.0123456789]",""); 
      r8 =r8.replaceAll("[^\\.0123456789]",""); 
      
      String[][] sr = {
    		  {r1},
    		  {r2},
    		  {r3},
    		  {r4},
    		  {r5},
    		  {r6},
    		  {r7},
    		  {r8},
      };
      
      //check conflicts
      String r10 = r1.substring(0, 2);
      String r20 = r2.substring(0, 2);
      String r11 = r1.substring(2, 4);
      String r21 = r2.substring(2, 4);
      if ((r10.equals(r20)) && (!r11.equals(r21)))
      System.out.println("Rule r1 has conflict with rule r2 ");
      
      String r101 = r1.substring(0, 2);
      String r30 = r3.substring(0, 2);
      String r111 = r1.substring(2, 4);
      String r31 = r3.substring(2, 4);
      if ((r101.equals(r30)) && (!r111.equals(r31)))
      System.out.println("Rule r1 has conflict with rule r3 ");
      
      String r102 = r1.substring(0, 2);
      String r40 = r4.substring(0, 2);
      String r112 = r1.substring(2, 4);
      String r41 = r4.substring(2, 4);
      if ((r102.equals(r40)) && (!r112.equals(r41)))
      System.out.println("Rule r1 has conflict with rule r4 ");
      
      String r103 = r1.substring(0, 2);
      String r50 = r5.substring(0, 2);
      String r113 = r1.substring(2, 4);
      String r51 = r5.substring(2, 4);
      if ((r103.equals(r50)) && (!r113.equals(r51)))
      System.out.println("Rule r1 has conflict with rule r5 ");
      
      String r201 = r2.substring(0, 2);
      String r301 = r3.substring(0, 2);
      String r211 = r2.substring(2, 4);
      String r311 = r3.substring(2, 4);
      if ((r201.equals(r301)) && (!r211.equals(r311)))
      System.out.println("Rule r2 has conflict with rule r3 ");
      
      String r202 = r2.substring(0, 2);
      String r401 = r4.substring(0, 2);
      String r212 = r2.substring(2, 4);
      String r411 = r4.substring(2, 4);
      if ((r202.equals(r401)) && (!r212.equals(r411)))
      System.out.println("Rule r2 has conflict with rule r4 ");
      
      String r203 = r2.substring(0, 2);
      String r501 = r5.substring(0, 2);
      String r213 = r2.substring(2, 4);
      String r511 = r5.substring(2, 4);
      if ((r203.equals(r501)) && (!r213.equals(r511)))
      System.out.println("Rule r2 has conflict with rule r5 ");
      
      String r302 = r3.substring(0, 2);
      String r402 = r4.substring(0, 2);
      String r312 = r3.substring(2, 4);
      String r412 = r4.substring(2, 4);
      if ((r302.equals(r402)) && (!r312.equals(r412)))
      System.out.println("Rule r3 has conflict with rule r4 ");
      
      String r303 = r3.substring(0, 2);
      String r503 = r5.substring(0, 2);
      String r313 = r3.substring(2, 4);
      String r513 = r5.substring(2, 4);
      if ((r303.equals(r503)) && (!r313.equals(r513)))
      System.out.println("Rule r3 has conflict with rule r5 ");
      
      String r403 = r4.substring(0, 2);
      String r504 = r5.substring(0, 2);
      String r413 = r4.substring(2, 4);
      String r514 = r5.substring(2, 4);
      if ((r403.equals(r504)) && (!r413.equals(r514)))
      System.out.println("Rule r4 has conflict with rule r5 ");
         
      
    } catch (OWLOntologyCreationException e) {
      System.err.println("Error creating OWL ontology: " + e.getMessage());
      System.exit(-1);
    } catch (RuntimeException e) {
      System.err.println("Error starting application: " + e.getMessage());
      System.exit(-1);
    }
  }

  
  private static void Usage()
  {
    System.err.println("Usage: " + ReasonwithInitialRuleBase.class.getName() + " [ <owlFileName> ]");
    System.exit(1);
  }
}
