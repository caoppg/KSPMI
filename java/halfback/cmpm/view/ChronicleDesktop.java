package halfback.cmpm.view;

import javax.swing.JDesktopPane;

/**
 * Sous-classe de JDesktopPane qui permet la gestion de plusieurs ChronicleView.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class ChronicleDesktop extends JDesktopPane {

    private static final long serialVersionUID = 1L;
    
    public void addChronicleView(ChronicleView chronicle) {
        chronicle.setVisible(true);
        add(chronicle);
        try {
            chronicle.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

}