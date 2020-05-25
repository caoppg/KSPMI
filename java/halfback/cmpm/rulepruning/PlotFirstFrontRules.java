package halfback.cmpm.rulepruning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.netlib.util.doubleW;

import java_cup.internal_error;

//plot the rule measures in the first front
public class PlotFirstFrontRules {
	
	
	//x plot is accuracy
	private static double[] x = new double[10];
	//y plot is coverage
	private static double[] y = new double[10];

	public static void main(String[] args) throws IOException {
	// configuring everything by default

		//read Cov+Acc file of the pruned rules       
        ArrayList<String> string = new ArrayList<>();	        
        Scanner rulescanner = new Scanner(new FileReader("Reconstructed_final_Cov+Acc.txt"));
            while (rulescanner.hasNext()) {
            	string.add(rulescanner.nextLine());
            }

        int n = string.size() * 2;
        double[] array = new double[n];

        //write string array to double array, even line cov, odd line acc
        Scanner rulescanner1 = new Scanner(new FileReader("Reconstructed_final_Cov+Acc.txt"));
        
        for (int i = 0; i < array.length; i++) {
            array[i] = rulescanner1.nextDouble();
        }

        for (double d : array) {
       //     System.out.println(d); 
        }
        rulescanner.close();
        
        for (int j = 0; j < array.length; j++) {
        	//even, cov
        	if((j % 2) == 0) {
        		int k = j/2;
        	//	 System.out.println(array[k]); 
        		y[k] = array[j]; 
        		System.out.println(y[k]); 
        	} 
        	//odd, acc
        	else {
        		int k = j/2;
        		x[k] = array[j];
        	//	System.out.println(x[k]);
        	}
        	
        }
		
		DefaultPlot plot = DefaultPlot.plot(DefaultPlot.plotOpts().

		        title("The Two Measures of the Rules in the First Pareto Front (X: -Accuracy; Y: -Coverage)").
		        legend(DefaultPlot.LegendFormat.NONE)).
		    xAxis("-Acc", DefaultPlot.axisOpts().
		        range(-1, -0.8)).
		    yAxis("-Cov", DefaultPlot.axisOpts().
		        range(-1, -0.8)).
		    series("", DefaultPlot.data().xy(x, y), DefaultPlot.seriesOpts().
		            marker(DefaultPlot.Marker.DIAMOND).
		            markerColor(DefaultPlot.GREEN).
		            color(DefaultPlot.BLACK));

		plot.save("BestRulePlot", "png");
	}
	
}