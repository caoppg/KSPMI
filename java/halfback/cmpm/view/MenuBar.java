package halfback.cmpm.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;
    
    public MenuBar(final JFrame main) {
        // Create file chooser
        final JFileChooser fileBrowser = new JFileChooser();
        fileBrowser.removeChoosableFileFilter(fileBrowser.getAcceptAllFileFilter()); // Do not allow "all files"
        FileFilter filter = new FileNameExtensionFilter("Comma-Separated Values (.csv)", "csv", "CSV");
        fileBrowser.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("ARFF files (.arff)", "arff", "ARFF");
        fileBrowser.addChoosableFileFilter(filter);
        
        // Create menu bar
        JMenu filesMenu = new JMenu("Files");
        add(filesMenu);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileBrowser.showOpenDialog(main);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileBrowser.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getAbsolutePath() + ".");
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        filesMenu.add(openItem);
    }

}