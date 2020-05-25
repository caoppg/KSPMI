package halfback.cmpm.view;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Set;

import halfback.cmpm.chronicletoSWRLrules.ChroniclestoSWRL;
import halfback.cmpm.controller.ChronicleTableModel;
import halfback.cmpm.controller.ChronicleViewListener;
import halfback.cmpm.model.Chronicle;

public class MainFrame extends JFrame /*implements ActionListener*/ {
    
    private static final long serialVersionUID = 1L;
    private Set<Chronicle> _chronicles;
    static int current = 0;
    //button to the next frame
    private JButton ruletransbutton;
    
	public MainFrame(Set<Chronicle> chronicles) {
        super("HALFBACK Prototype: Chronicle Mining for Predictive Maintenance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _chronicles = chronicles;

        JPanel content = new JPanel(new GridBagLayout());
        setContentPane(content);
        content.setBackground(Color.BLACK);
        int inset = 100;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset-50,
                  screenSize.width  - inset * 15,
                  screenSize.height - inset * 15);
        

        
        GridBagConstraints c = new GridBagConstraints();
        

        MenuBar menu = new MenuBar(this);
        setJMenuBar(menu);
        
        ChronicleTableModel model = new ChronicleTableModel(_chronicles);
        ChronicleDescription description = new ChronicleDescription();
        ChronicleView.setInternalFrameListener(new ChronicleViewListener(description, model));
        ChronicleDesktop desktop = new ChronicleDesktop();
        ChronicleList list = new ChronicleList(model, desktop, description);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        description.setPreferredSize(new Dimension(2 * (screenSize.width - 2 * inset) / 3, 2 * (screenSize.height - inset * 2) / 5));
        list.setPreferredSize(new Dimension((screenSize.width - 2 * inset) / 3, 2 * (screenSize.height - inset * 2) / 5));
        desktop.setPreferredSize(new Dimension(screenSize.width - inset * 2, 3 * (screenSize.height - inset * 2) / 5));
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(list, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        content.add(description, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weighty = 1.0;
        content.add(desktop, c);
        
        //button to the next frame      
        ruletransbutton = new JButton("Transform the extracted chronicles into SWRL rules");
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 15;
        c.weightx = 0.0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
   //     c.gridwidth = 10;
        content.add(ruletransbutton, c);
        
        pack();
    //  setSize(1200, 800);
        setVisible(true);
        try {
			ruletransbutton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					//after button click, start rule transformation
					ChroniclestoSWRL ChroniclestoSWRL = new ChroniclestoSWRL();
					
					try {
						ChroniclestoSWRL.ruletransform();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(null, "The running time of the SWRL rule transformation algorihtm is: " + ChroniclestoSWRL.getTotalTime() + " milliseconds");
					
					
				//	RuleTransTimeWindow.main(null);
				//	RuleTransTimeWindow RTT1 = new RuleTransTimeWindow();
					
					RuleTransGUI.main(null);
					RuleTransGUI RTG1 = new RuleTransGUI();	
					
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	

}