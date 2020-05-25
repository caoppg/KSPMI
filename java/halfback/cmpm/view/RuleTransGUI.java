package halfback.cmpm.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import halfback.cmpm.experiencecap.ExpertRuleInput;
import halfback.cmpm.rulepruning.PlotFirstFrontRules;
import halfback.cmpm.rulepruning.PlotImage;
import halfback.cmpm.rulepruning.ReconstructAccCov;
import java_cup.internal_error;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.List;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;

public class RuleTransGUI extends JFrame {

	DefaultListModel<String> listModel;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RuleTransGUI frame = new RuleTransGUI();
					frame.setTitle("HALFBACK Prototype: SWRL Rule Transformation and Rule Pruning");
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
	 * @param string 
	 */
	public RuleTransGUI() {
		
		listModel = new DefaultListModel<>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 300, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("SWRL Rules Transformation and Rule Pruning Interface");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 10, 797, 48);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 68, 456, 481);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 456, 481);
		panel.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(456, 68, 416, 481);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton button = new JButton("Display SWRL rules");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String filepathString = "Transformed SWRL Rules.txt";
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(filepathString));
					//number of entries in the rule file
					ArrayList<String> chronicles = new ArrayList<String>();
					String st; 
					while ((st = br.readLine()) != null) {
					chronicles.add(st);
					listModel.addElement(st);	
					}
					list.setModel(listModel);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setActionCommand("Display SWRL rules");
		button.setBounds(88, 83, 238, 38);
		panel_1.add(button);
		
		JButton button_1 = new JButton("Rule Pruning");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The rule pruning process has been completed.\nAlgorithm used: Fast Non-dominated Sorting.");
				String prunedfilepathString = "Pruned SWRL Rule Base.txt";
				BufferedReader brpurn = null;
				//run ReconstructAccCov.java
				try {
					ReconstructAccCov.main(null);
					
					DefaultListModel listModel = (DefaultListModel) list.getModel();
			        listModel.removeAllElements();
			        
			        
					brpurn = new BufferedReader(new FileReader(prunedfilepathString));
					//number of entries in the rule file
					ArrayList<String> chronicles = new ArrayList<String>();
					String st; 
					while ((st = brpurn.readLine()) != null) {
					chronicles.add(st);
					listModel.addElement(st);	
					}
					list.setModel(listModel);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		button_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		button_1.setBounds(88, 165, 238, 38);
		panel_1.add(button_1);
		
		JButton button_2 = new JButton("Plot the Best Rules");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PlotFirstFrontRules PFFR = new PlotFirstFrontRules();
			    try {
					PFFR.main(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PlotImage PI = new PlotImage();
				
			}
		});
		button_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		button_2.setBounds(88, 253, 238, 38);
		panel_1.add(button_2);
		
		JButton button_3 = new JButton("Save New Rule Base");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The New Rule Base has Been Saved.");
			}
		});
		button_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		button_3.setBounds(88, 337, 238, 38);
		panel_1.add(button_3);
		
		JButton button_4 = new JButton("Next");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check if the rules have been pruned
				File prunedrulefile = new File("Pruned SWRL Rule Base.txt");
				boolean exists = prunedrulefile.exists();
				//if pruned rule file exists, open next JFrame 
				if (prunedrulefile.exists()) {
					ExpertRuleInput.main(null);
					setVisible(false);
				} 
				//do not exist, do pruning
				else if (exists == false){    
			    JOptionPane.showMessageDialog(null, "The Rule Pruning Process has not been Finished.");

			}
				
		}});
		button_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		button_4.setBounds(298, 443, 108, 38);
		panel_1.add(button_4);
	}
}
