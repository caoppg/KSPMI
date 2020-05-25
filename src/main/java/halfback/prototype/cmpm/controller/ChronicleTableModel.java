package halfback.prototype.cmpm.controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import halfback.prototype.cmpm.model.Chronicle;

public class ChronicleTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    
    private List<ChronicleTableEntry> _table;

    public ChronicleTableModel(Iterable<Chronicle> chronicles) {
		_table = new ArrayList<ChronicleTableEntry>();
		for (Chronicle chronicle : chronicles) {
			_table.add(new ChronicleTableEntry(chronicle));
		}
    }


	@Override
	public int getRowCount() {
		return _table.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return rowIndex;
			case 1:
				return _table.get(rowIndex).toString();
			case 2:
			try {
				return _table.get(rowIndex).getSupport();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			case 3:
			try {
				return _table.get(rowIndex).getCoverage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			case 4:
			try {
				return _table.get(rowIndex).getAccuracy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			default:
				return null;
		}
	}
	
	public void main(String[] args) {
		System.out.println(_table.toString());
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 1:
				return "Chronicle";
			case 2:
				return "Support";
			case 3:
				return "Coverage";
			case 4:
				return "Accuracy";
			default:
				return "";
		}
	}

	
	public ChronicleTableEntry getEntry(int row) {
		return _table.get(row);
	}

	public ChronicleTableEntry getEntry1(int column) {
		return _table.get(column);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (_table.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}
	
}