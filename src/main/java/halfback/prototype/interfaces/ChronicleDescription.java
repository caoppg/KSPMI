package halfback.prototype.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import halfback.prototype.cmpm.model.Chronicle;


/**
 * Panel with the description of the selected chronicle.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 *
 * @author <a href="qiushi.cao@insa-rouen.fr">Qiushi Cao</a>
 */
public class ChronicleDescription extends JPanel {

    private static final long serialVersionUID = 1L;
    
    /**
     * Currently described chronicle.
     */
    private Chronicle _described;
    
    /**
     * Chronicle description panel.
     */
    //private final JPanel _description;
    
    /**
     * To be able to change the support.
     */
  //  private final JLabel _support;
    
    private final JLabel _coverage;
    private final JLabel _accuracy;
    
    /**
     * Attribute description list
     */
    private final JList<AttributeDescription> _attributes;

    /**
     * Minimal panel for the attributes.
     * @author Carlos Miranda
     */
    public class AttributeDescription {

    }

    /**
     * Minimal panel for a title.
     * @author Carlos Miranda
     */
    public class TitlePane extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Creates the panel and the title label.
		 * @param title
		 */
		public TitlePane(String title) {
            setLayout(new BorderLayout());
            setBorder(new CompoundBorder(new EmptyBorder(4, 4, 4, 4), new MatteBorder(0, 0, 1, 0, Color.BLACK)));
            JLabel label = new JLabel(title);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            add(label);
        }
    }

    /**
     * Constructor
     */
    public ChronicleDescription() {
        super();
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        //
        c.ipadx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;

        // Add the title
        TitlePane title = new TitlePane("Current Selection");
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2.0;
        add(title, c);

     //   Add the support pane
        /**
        JPanel suppPane = new JPanel(new BorderLayout());
        _support = new JLabel();
        suppPane.add(new JLabel("Support: "), BorderLayout.WEST);
        suppPane.add(_support, BorderLayout.EAST);
        c.gridx = 0;
        c.gridy = 1;
        add(suppPane, c);
        */
        
        // Add coverage
        JPanel CovPane = new JPanel(new BorderLayout());
        _coverage = new JLabel();
        CovPane.add(new JLabel("Coverage: "), BorderLayout.WEST);
        CovPane.add(_coverage, BorderLayout.EAST);
        c.gridx = 0;
        c.gridy = 1;
        add(CovPane, c);
        
     // Add accuracy
        JPanel AccPane = new JPanel(new BorderLayout());
        _accuracy = new JLabel();
        AccPane.add(new JLabel("Accuracy: "), BorderLayout.WEST);
        AccPane.add(_accuracy, BorderLayout.EAST);
        c.gridx = 0;
        c.gridy = 2;
        add(AccPane, c);

        // Add the attributes
        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Attributes: "), c);
        _attributes = new JList<AttributeDescription>();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(_attributes, c);
        
    }

    /**
     * Sets the active (selected) chronicle.
     * @param selected The selected chronicle
     * @throws IOException 
     */
    public void setCurrent(Chronicle selected) throws IOException {
        _described = selected;
        describe();
    }

    /**
     * Update the description of the chronicle. To be called after the selected chronicle has changed.
     * @throws IOException 
     */
    private void describe() throws IOException {
    //    _support.setText(Integer.toString(_described.getSupport()));
        _coverage.setText(Double.toString(_described.getCoverage()));
        _accuracy.setText(Double.toString(_described.getAccuracy()));
        // TODO: Attribute list
    }
    
    /**
     * Clean the data.
     */
    public void clean() {
    	_coverage.setText("");
    	_attributes.removeAll();
    }
}