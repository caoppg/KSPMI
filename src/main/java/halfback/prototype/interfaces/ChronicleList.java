package halfback.prototype.interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

import halfback.prototype.cmpm.controller.ChronicleSelectionListener;
import halfback.prototype.cmpm.controller.ChronicleTableModel;


public class ChronicleList extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTable _table;

	public ChronicleList(ChronicleTableModel model, ChronicleDesktop desktop, ChronicleDescription description) {
        super();
        setLayout(new BorderLayout());
        _table = new JTable(model);
        //_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _table.getColumnModel().getColumn(1).setMinWidth(250);
        _table.setAutoCreateRowSorter(true);
        JScrollPane pane = new JScrollPane(_table);
        add(pane, BorderLayout.CENTER);
        _table.getSelectionModel().addListSelectionListener(new ChronicleSelectionListener(_table, desktop, description));
        _table.getRowSorter().addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                _table.clearSelection();
            }
        });
        MergeTwoMeasures mergetwomeasureobject = new MergeTwoMeasures();
        mergetwomeasureobject.mergeacccov();
    	//	File CovOldFile = new File("Cov_.txt");
    	//	File CovNewFile = new File("Acc+Cov_" + Utils.getDoubleParam(args, "m", 0.9) + ".txt");
    	//	CovOldFile.renameTo(CovNewFile);
    }
}