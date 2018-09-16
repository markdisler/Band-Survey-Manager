import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Sidebar extends JPanel {

	private ManagedTableView table;
	private SurveyManager mainRef;
	private JTextField numBusesField;
	private JComboBox<String> generalPositiveAnswer, generalNegativeAnswer;
	 

	public Sidebar(SurveyManager mainRef) {
		super();

		this.mainRef = mainRef;


		// Map Section
		JPanel mapSection = new JPanel(new BorderLayout());
		this.add(mapSection);

		// Mapping Label
		JLabel mapLabel = new JLabel("Spreadsheet Column Maps", SwingConstants.LEFT);
		mapLabel.setFont(new Font("Arial", Font.BOLD, 14));
		mapLabel.setBorder(new EmptyBorder(4, 0, 4, 0));
		mapSection.add(mapLabel, BorderLayout.NORTH);

		this.table = new ManagedTableView();
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setFont(new Font("Arial", Font.PLAIN, 14));
		this.table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14));
		this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
		this.table.setRowHeight(24);

		JScrollPane scrollTable = new JScrollPane(this.table);
		mapSection.add(scrollTable, BorderLayout.CENTER);

		this.table.addColumn("Left Table Column");
		this.table.addColumn("Right Table Column");
		this.table.addColumn("Designation");

		String[] designations = {"First Name", "Last Name", "Nickname", "Full Name", "Instrument", "Email", "Class Year", "Timestamp", "Bus Response", "Bus Overflow Volunteer"};
		TableColumn col = table.getColumnModel().getColumn(2);
		col.setCellEditor(new ComboBoxEditor(designations));
		
		Vector<String> row1 = new Vector<String>();
		row1.add("A");
		row1.add("B");
		row1.add("First Name");
		this.table.addRow(row1);
		
		Vector<String> row2 = new Vector<String>();
		row2.add("B");
		row2.add("C");
		row2.add("Last Name");
		this.table.addRow(row2);
		
		Vector<String> row3 = new Vector<String>();
		row3.add("C");
		row3.add("");
		row3.add("Nickname");
		this.table.addRow(row3);
		
		Vector<String> row4 = new Vector<String>();
		row4.add("D");
		row4.add("");
		row4.add("Email");
		this.table.addRow(row4);
		
		Vector<String> row5 = new Vector<String>();
		row5.add("E");
		row5.add("");
		row5.add("Class Year");
		this.table.addRow(row5);
		
		Vector<String> row6 = new Vector<String>();
		row6.add("F");
		row6.add("D");
		row6.add("Instrument");
		this.table.addRow(row6);
		
		Vector<String> row7 = new Vector<String>();
		row7.add("");
		row7.add("E");
		row7.add("Bus Response");
		this.table.addRow(row7);
		
		Vector<String> row8 = new Vector<String>();
		row8.add("");
		row8.add("F");
		row8.add("Bus Overflow Volunteer");
		this.table.addRow(row8);
		
		Vector<String> row9 = new Vector<String>();
		row9.add("");
		row9.add("A");
		row9.add("Timestamp");
		this.table.addRow(row9);
		
		for (int i = 0; i < 3; i++)
			this.table.addBlankRow();


		// Bus List Section
		JPanel busSection = new JPanel(new BorderLayout());
		this.add(busSection);

		// Bus Label
		JLabel busLabel = new JLabel("Bus Configuration", SwingConstants.LEFT);
		busLabel.setFont(new Font("Arial", Font.BOLD, 14));
		busLabel.setBorder(new EmptyBorder(4, 0, 4, 0));
		busSection.add(busLabel, BorderLayout.NORTH);

		JPanel generalBus = new JPanel();
		generalBus.setLayout(new BoxLayout(generalBus, BoxLayout.Y_AXIS));
		busSection.add(generalBus);



		// Bus Answer
		JLabel positiveAnswerLbl = new JLabel("General Positive Answer: ", SwingConstants.LEFT);
		positiveAnswerLbl.setMaximumSize(new Dimension(3000, 50));

		Box  b1 = Box.createHorizontalBox();
	    b1.add(positiveAnswerLbl);
	    b1.add(Box.createHorizontalGlue());
	    generalBus.add(b1);
	    
		JComboBox<String> genPosAnswer = new JComboBox<String>();
		genPosAnswer.setMaximumSize(new Dimension(3000, 50));
		genPosAnswer.setPrototypeDisplayValue("                                       ");
		generalBus.add(genPosAnswer);
		this.generalPositiveAnswer = genPosAnswer;
		
		JLabel negativeAnswerLbl = new JLabel("General Negative Answer: ", SwingConstants.LEFT);
		negativeAnswerLbl.setMaximumSize(new Dimension(3000, 50));

		Box  b2 = Box.createHorizontalBox();
	    b2.add(negativeAnswerLbl);
	    b2.add(Box.createHorizontalGlue());
	    generalBus.add(b2);
	    
	    JComboBox<String> genNegAnswer = new JComboBox<String>();
		genNegAnswer.setMaximumSize(new Dimension(3000, 50));
		genNegAnswer.setPrototypeDisplayValue("                                       ");
		generalBus.add(genNegAnswer);
		this.generalNegativeAnswer = genNegAnswer;
		
		//JPanel genAns = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//genAns.setMaximumSize(new Dimension(3000, 50));
		//genAns.add(generalAnswerLbl);
		//generalBus.add(genAns);
		 

		JPanel updateGen = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
		JButton updateGenBtn = new JButton("Update Choices");
		updateGenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, ColumnMapping> mappings = getMappings();
				ArrayList<String> choicesList = TableOps.responsesInColumn(mainRef.getRightTableData(), mappings.get("bus_response"));

				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) genPosAnswer.getModel();
				model.removeAllElements();
				for (String item : choicesList) {
					model.addElement(item);
				}
				genPosAnswer.setModel(model);
				
				
				DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) genNegAnswer.getModel();
				model2.removeAllElements();
				for (String item : choicesList) {
					model2.addElement(item);
				}
				genNegAnswer.setModel(model2);
			}
		});
		updateGen.add(updateGenBtn);
		generalBus.add(updateGen);




	}

	public Map<String, ColumnMapping> getMappings() {
		Map<String, ColumnMapping> mappings = new HashMap<String, ColumnMapping>();

		for (int i = 0; i < this.table.getRowCount(); i++) {

			if (this.table.getValueAt(i, 0) != null && this.table.getValueAt(i, 2) != null) {
				String origin = this.table.getValueAt(i, 0).toString();
				String destination = this.table.getValueAt(i, 2).toString();
				String next = destination.equals("Nickname") || destination.equals("Email")  ? "" : this.table.getValueAt(i, 1).toString();

				ColumnMapping colMap = new ColumnMapping(origin, next, destination);
				String key = destination.toLowerCase().replaceAll(" ", "_");

				mappings.put(key, colMap);
			}
		}

		return mappings;
	}


	
	public int getNumberOfBuses() {
		String s = this.numBusesField.getText();
		int n = Integer.parseInt(s);
		return n;
	}
	
	public String getPositiveAnswer() {
		return this.generalPositiveAnswer.getSelectedItem().toString();
	}
	
	public String getNegativeAnswer() {
		return this.generalNegativeAnswer.getSelectedItem().toString();
	}

	public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
		public ComboBoxRenderer(String[] items) {
			super(items);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelectedItem(value);
			return this;
		}
	}

	class ComboBoxEditor extends DefaultCellEditor {
		public ComboBoxEditor(String[] items) {
			super(new JComboBox(items));
			JComboBox cb = (JComboBox)this.editorComponent;
			cb.setEditable(true);
		}
	}
}

