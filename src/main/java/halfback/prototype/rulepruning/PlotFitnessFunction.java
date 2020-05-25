package halfback.prototype.rulepruning;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlotFitnessFunction extends JFrame{
JPanel imageJPanel = new JPanel();
	
	JLabel imageJLabel = new JLabel();
	
	//constructor
	public PlotFitnessFunction() {
		
		setTitle("HALFBACK Prototype: Plot of the Fitness Function");
		setVisible(true);
		setSize(650, 530);
		setLocationRelativeTo(null);
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//add image to JLabel
		imageJLabel.setIcon(new ImageIcon("Image\\Fitness_value.png"));
		imageJPanel.add(imageJLabel);
		add(imageJPanel);
		validate();
	}

	public static void main(String[] args) throws IOException {
		//run PlotFirstFrontRules class to generate image
	//	PlotFirstFrontRules PFFR = new PlotFirstFrontRules();
	//	PFFR.main(null);
	//	PlotImage PI = new PlotImage();
	
	}


}
