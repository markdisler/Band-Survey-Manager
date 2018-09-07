import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ManagedTableView extends JTable {
	
	public DefaultTableModel internalModel;
	
	public ManagedTableView() {
		super();
		this.internalModel = new DefaultTableModel();
		this.setModel(this.internalModel);
		this.commonInit();
	}
	
	public ManagedTableView(DefaultTableModel model) {
		super();
		this.internalModel = model;
		this.setModel(this.internalModel);
		this.commonInit();
	}
	
	public void commonInit() {
		this.setBackground(new Color(252, 252, 252));
		this.setFont(new Font("Helvetica", Font.PLAIN, 18));
		this.setRowHeight(25);
		this.setGridColor(Color.black);
		this.getTableHeader().setFont(new Font("Helvetica", Font.PLAIN, 16));
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setColumnSelectionAllowed(true);
	}
	
	public int getNumberOfRows() {
		return this.internalModel.getRowCount();
	}

	public int getNumberOfColumns () {
		return this.internalModel.getColumnCount();
	}

	private String getCharForNumber(int i) {
		return (i > 0 && i < 27) ? String.valueOf((char)(i + 64)) : "";
	}
	
	public void addColumn() {
		int numberOfColumns = this.getNumberOfColumns();
		this.internalModel.addColumn(this.getCharForNumber(numberOfColumns+1));
	}
	
	public void addColumn(String title) {
		this.internalModel.addColumn(title);
	}
	
	public void addBlankRow() {
		Vector<String> row = new Vector<String>();
		row.add("");
		this.internalModel.addRow(row);
	}
	
	public void addRow(Vector<String> row) {
		this.internalModel.addRow(row);
	}
	
	public void fitToContent() {
		for (int i = 0; i < getNumberOfColumns(); i++) {
			int maxLen = 0;
			for (int j = 0; j < getNumberOfRows(); j++) {
				String value = this.internalModel.getValueAt(j, i).toString();
				maxLen = Math.max(maxLen, value.length());
			}
			this.getColumnModel().getColumn(i).setPreferredWidth(maxLen * 18);
			
		}
	}

}
