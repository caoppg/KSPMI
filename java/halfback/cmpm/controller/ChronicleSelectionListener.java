package halfback.cmpm.controller;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import halfback.cmpm.model.Chronicle;
import halfback.cmpm.view.ChronicleDescription;
import halfback.cmpm.view.ChronicleDesktop;
import halfback.cmpm.view.ChronicleView;

public class ChronicleSelectionListener implements ListSelectionListener {

    private final ChronicleDesktop _desktop;
    private final JTable _table;
    private final ChronicleDescription _description;

    public ChronicleSelectionListener(JTable table, ChronicleDesktop desktop, ChronicleDescription description) {
        _table = table;
        _desktop = desktop;
        _description = description;
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && _table.getSelectedRow() != -1) {
			int id = (int) _table.getValueAt(_table.getSelectedRow(), 0);
            Chronicle selected = ((ChronicleTableModel) _table.getModel()).getEntry(id).getChronicle();
            _desktop.addChronicleView(new ChronicleView(selected, id));
        }
	}
}