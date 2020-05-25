package halfback.cmpm.rulepruning;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlotImage extends JFrame{
	
	
	JPanel imageJPanel = new JPanel();
	
	JLabel imageJLabel = new JLabel();
	
	//constructor
	public PlotImage() {
		
		setTitle("HALFBACK Prototype: Plot of the Rules in the First Pareto Front");
		setVisible(true);
		setSize(850, 680);
		setLocationRelativeTo(null);
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//add image to JLabel
		imageJLabel.setIcon(new ImageIcon("BestRulePlot.png"));
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
