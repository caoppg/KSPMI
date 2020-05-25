package halfback.cmpm.experiencecap;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Event;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ExpertRuleInput extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExpertRuleInput frame = new ExpertRuleInput();
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
	public ExpertRuleInput() {
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
		
		//text area 1 is the output results
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		textArea_1.setBounds(84, 270, 723, 268);
		contentPane.add(textArea_1);
		
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
				String er = textArea_2.getText();
				System.out.println(er);
				
				//call the expert rule integration class
				textArea_1.append( textArea_2.getText() );
				textArea_2.setText("");
				
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton_1.setBounds(361, 163, 164, 36);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Start Prediction");
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
		
		
	}
}
