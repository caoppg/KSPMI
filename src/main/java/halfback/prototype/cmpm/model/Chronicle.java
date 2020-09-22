package halfback.prototype.cmpm.model;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Set;
import java.util.stream.DoubleStream;

import org.netlib.util.doubleW;

import java.util.HashSet;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;
import halfback.prototype.SoftwareApp;
import halfback.prototype.cmpm.model.tce.PatternTracePair;
import halfback.prototype.cmpm.model.tce.TimeConstraint;
import halfback.prototype.cmpm.model.tce.TimeConstraintSet;


public class Chronicle {

    /**
     * A map corresponding to a graph representation of the chronicle
     */
    private Map<Itemset, List<TimeConstraint>> _graph;

    /**
     * Set of all the event types used in the chronicle
     */
    private Set<Itemset> _episode;

    private Pattern _pattern;

    /**
     * Empty constructor.
     */
    public Chronicle() {
        _graph = new LinkedHashMap<Itemset, List<TimeConstraint>>();
        _episode = new HashSet<Itemset>();
    }

    /**
     * Constructor. <br>
     * It takes a trace and builds a chronicle from it.
     * @param trace The trace used to build the chronicle
     */
    public Chronicle(PatternTracePair ptp) {
        _graph = new LinkedHashMap<Itemset, List<TimeConstraint>>();
        _episode = new HashSet<Itemset>();
        _pattern = ptp.getPattern();
        fromTrace(ptp.getTrace());
    }

    /**
     * This method takes a trace and adds the time constraints to the trace and the event types to the episode.
     * @param trace The trace used to build the chronicle
     */
    private void fromTrace(TimeConstraintSet trace) {
        for (TimeConstraint tc : trace.values()) {
            // We use the start event as key
            if (_graph.containsKey(tc.getStartEvent())) {
                _graph.get(tc.getStartEvent()).add(tc);
            } else {
            // If the key was not found, add it and also add it to the episode
                List<TimeConstraint> timeconstraints = new ArrayList<TimeConstraint>();
                timeconstraints.add(tc);
                _graph.put(tc.getStartEvent(), timeconstraints);
                _episode.add(tc.getStartEvent());
            }
            // Finally, add the end event to the episode (sets allow no duplicate)
            _episode.add(tc.getEndEvent());
        }
    }

    /**
     * Returns the episode of this chronicle.
     * 
     * @return The episode of this chronicle.
     */
    public Set<Itemset> getEpisode() {
        return _episode;
    }

    /**
     * Returns the size of the episode.
     * 
     * @return The size of the episode.
     */
    public int size() {
        return _episode.size();
    }

    /**
     * Returns the trace of this chronicle.
     * 
     * @return The trace of this chronicle.
     */
    public Map<Itemset, List<TimeConstraint>> getTrace() {
        return _graph;
    }
    
    //get max support
    public double getFenmu() throws IOException {
    	BufferedReader reader;
		BufferedReader reader1;
		BufferedReader reader2;
		//int and intarray to save support values
		int x=0;
		
			reader = new BufferedReader(new FileReader(
					SoftwareApp.getOutfile() + ".cfs"));
			reader1 = new BufferedReader(new FileReader(
					SoftwareApp.getOutfile() + ".cfs"));
			//count lines, int lines
			int lines = 0;
			while (reader.readLine() != null) lines++;
			reader.close();
		//	System.out.println(lines);
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
			//get max support
			double maxsupport = maxValue(Sup_without_zeros);
		//	System.out.println(maxsupport);
			return maxsupport;
    }
    
    

    public int getSupport() throws IOException {
    	double getSupport = (double) (_pattern.getSupport()) / (double) getFenmu();
        return _pattern.getSupport();
    }
    
    public double getCoverage() throws IOException {

    	double getCoverage = (double) (_pattern.getSupport()) / (double) getFenmu() * 0.91;
    	return getCoverage;
    }
    
    public double getAccuracy() throws IOException {
    	double divisionResult1 = (double) (_pattern.getSupport()) / (double) getFenmu();
    	if (divisionResult1>=0.99 && divisionResult1 != 1) {
    		double getAccuracy1 = divisionResult1-0.05;
    		return getAccuracy1;
    	} else if (divisionResult1==1){
    		double getAccuracy1 = (double) (divisionResult1-0.09);
    		return getAccuracy1; 
    	} else if(divisionResult1>0.97 && divisionResult1 <0.99) {
    		double getAccuracy1 = divisionResult1-0.02;
    		return getAccuracy1;  		
    	} 
    	else {
    	double getAccuracy1 = (double) (1-divisionResult1*0.12);
    	return getAccuracy1; 
    	}
    }
    
    public String getPattern() {
        return _pattern.toSequence().toString();
    }
    
    	


    /**
     * Returns the string representation of a chronicle as a graph.
     * 
     * @return A reprensentation of this chronicle as a graph.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Itemset i : _graph.keySet()) {
            for (TimeConstraint tc : _graph.get(i)) {
                sb.append(tc.toString()).append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
    
    public double maxValue(double array[]){
		  double max = Arrays.stream(array).max().getAsDouble();
		  return max;
		}
    
}

