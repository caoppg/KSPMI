package halfback.prototype.rulepruning;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.print("Please enter the name of the input file: ");
        String inputFileName = console.nextLine();

        Scanner in = null;
        try {
            in = new Scanner(new File(inputFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int n = in.nextInt();
        double[] array = new double[n];

        for (int i = 0; i < array.length; i++) {
            array[i] = in.nextDouble();
        }
        for (double d : array) {
            System.out.println(d); 
        }
        console.close();
    }
}