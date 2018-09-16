import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SurveyManager {

	public JFrame frame;
	private Toolbar topToolbar;
	private ManagedTableView leftTable, rightTable;
	private JLabel leftPath, rightPath;
	private JTextArea consoleArea;
	private Sidebar sidebar;

	private String[][] leftTableData, rightTableData;
	public Map<String, ColumnMapping> colMappings;

	public ArrayList<Person> people;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SurveyManager window = new SurveyManager();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SurveyManager() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Survey Manager");
		System.setProperty("apple.awt.brushMetalLook", "true");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		// Create the window
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 400));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(ResourceLoader.getImage("Correlate"));

		// * * * * * * * * * * * * * * * * *
		//
		//  Toolbar 
		//
		// * * * * * * * * * * * * * * * * *

		// CREATE the top toolbar
		topToolbar = new Toolbar();
		topToolbar.setBackground(new Color(224, 224, 224));
		frame.getContentPane().add(topToolbar, BorderLayout.NORTH);


		// CREATE Import Roster Button
		ToolbarButton rosterBtn = new ToolbarButton("Import Roster", ResourceLoader.getIcon("ImportRoster"));
		rosterBtn.setPreferredSize(new Dimension(100, 80));
		rosterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", "xls", "xlsx");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String selectedPath = chooser.getSelectedFile().getAbsolutePath();
					loadRosterFromFile(selectedPath);
				}
			}
		});
		topToolbar.leftSection.add(rosterBtn);

		// CREATE Import Survey Button
		ToolbarButton surveyBtn = new ToolbarButton("Import Survey", ResourceLoader.getIcon("ImportSurvey"));
		surveyBtn.setPreferredSize(new Dimension(100, 80));
		surveyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", "xls", "xlsx");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String selectedPath = chooser.getSelectedFile().getAbsolutePath();
					loadSurveyFromFile(selectedPath);
				}
			}
		});
		topToolbar.leftSection.add(surveyBtn);

		// CREATE Correlate Button
		ToolbarButton correlateBtn = new ToolbarButton("Correlate", ResourceLoader.getIcon("Correlate"));
		correlateBtn.setPreferredSize(new Dimension(100, 80));
		correlateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				correlateSet();
			}
		});
		topToolbar.rightSection.add(correlateBtn);

		// CREATE Bus List Button
		ToolbarButton busListBtn = new ToolbarButton("Bus List", ResourceLoader.getIcon("BusList"));
		busListBtn.setPreferredSize(new Dimension(100, 80));
		busListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateBusLists();
			}
		});
		topToolbar.rightSection.add(busListBtn);


		// * * * * * * * * * * * * * * * * *
		//
		//  Primary Area 
		//
		// * * * * * * * * * * * * * * * * *

		// CREATE Main Area
		JPanel primaryArea = new JPanel(new BorderLayout());
		frame.getContentPane().add(primaryArea, BorderLayout.CENTER);

		// CREATE Console
		JScrollPane consoleScrollPane = new JScrollPane();
		this.consoleArea = new JTextArea();
		this.consoleArea.setFont(new Font("Helvetica", Font.PLAIN, 18));
		this.consoleArea.setLineWrap(true);
		this.consoleArea.setWrapStyleWord(true);
		consoleScrollPane.setViewportView(this.consoleArea);

		// CREATE tables
		this.leftTable = new ManagedTableView();
		this.leftTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		this.rightTable = new ManagedTableView();
		this.rightTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane leftScrollTable = new JScrollPane(this.leftTable);
		leftScrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		leftScrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane rightScrollTable = new JScrollPane(this.rightTable);
		rightScrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		rightScrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel leftArea = new JPanel(new BorderLayout());
		JPanel rightArea = new JPanel(new BorderLayout());

		leftArea.add(leftScrollTable, BorderLayout.CENTER);
		rightArea.add(rightScrollTable, BorderLayout.CENTER);

		this.leftPath = new JLabel("1) No file loaded");
		this.leftPath.setPreferredSize(new Dimension(0, 22));
		this.rightPath = new JLabel("2) No file loaded");
		this.rightPath.setPreferredSize(new Dimension(0, 22));

		this.sidebar = new Sidebar(this);
		this.sidebar.setLayout(new BoxLayout(this.sidebar, BoxLayout.Y_AXIS));
		JScrollPane sidebarScrollView = new JScrollPane(this.sidebar);
		sidebarScrollView.setBorder(new EmptyBorder(0, 0, 0, 0));
		sidebarScrollView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		leftArea.add(this.leftPath, BorderLayout.NORTH);
		rightArea.add(this.rightPath, BorderLayout.NORTH);

		// CREATE Horizontal Split Pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftArea, rightArea);
		splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		splitPane.setResizeWeight(0.5);

		// CREATE Vertical Split Pane
		JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, consoleScrollPane);
		verticalSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		verticalSplitPane.setResizeWeight(0.75);

		// CREATE Full Split
		JSplitPane bigSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, verticalSplitPane, sidebarScrollView);
		bigSplitPane.setResizeWeight(0.75);
		primaryArea.add(bigSplitPane, BorderLayout.CENTER);

	}

	public void loadRosterFromFile(String path) {

		String htmlStr = path;
		htmlStr = htmlStr.replaceAll("/", "<FONT COLOR=BLUE> / </FONT>");
		this.leftPath.setText("<html> " + htmlStr + " </html>");
		printToConsole("Loaded " + path);

		String[][] roster = SpreadsheetManager.getTableFromSpreadsheet(path);
		this.leftTableData = roster;

		for (int i = 0; i < roster[0].length; i++) {
			this.leftTable.addColumn();
		}

		for (int i = 0; i < roster.length; i++) {
			Vector<String> row = new Vector<String>();
			for (int j = 0; j < roster[i].length; j++) {
				row.add(roster[i][j]);
			}
			this.leftTable.addRow(row);
		}
		this.leftTable.fitToContent();
	}

	public void loadSurveyFromFile(String path) {

		String htmlStr = path;
		htmlStr = htmlStr.replaceAll("/", "<FONT COLOR=BLUE> / </FONT>");
		this.rightPath.setText("<html> " + htmlStr + " </html>");
		printToConsole("Loaded " + path);

		String[][] roster = SpreadsheetManager.getTableFromSpreadsheet(path);
		this.rightTableData = roster;

		for (int i = 0; i < roster[0].length; i++) {
			this.rightTable.addColumn();
		}

		for (int i = 0; i < roster.length; i++) {
			Vector<String> row = new Vector<String>();
			for (int j = 0; j < roster[i].length; j++) {
				row.add(roster[i][j]);
			}
			this.rightTable.addRow(row);
		}

		this.rightTable.fitToContent();

	}

	private void correlateSet() {
		ArrayList<Person> people = TableOps.getPeopleList(this.leftTableData, this.sidebar.getMappings());
		TableOps.fillSurveyData(people, this.rightTableData, this.sidebar.getMappings());

		for (Person p: people) {
			System.out.println(p);
		}

		ArrayList<Person> diff = TableOps.getSurveyStragglers(people);

		printToConsole("\nThe following people have not filled out the active survey:");
		for (Person p : diff) {
			String name = "";
			if (p.getNickname().equals("")) {
				name = p.getFirstName() + " " + p.getLastName();
			} else {
				name = p.getNickname() + " " + p.getLastName();
			}
			printToConsole(name + "\t" + p.getEmailAddress());
		}
		printToConsole("\n");
		
		this.people = people;

	}

	public void generateBusLists() {
		TableOps.generateBusLists(this.people, this.rightTableData, this.sidebar.getMappings(), this.sidebar.getPositiveAnswer(), this.sidebar.getNegativeAnswer()); 
		printToConsole("Bus List Generation Complete. File Saved.");
	}

	public void printToConsole(String s) {
		if (this.consoleArea.getText().length() == 0) {
			this.consoleArea.setText(s);
		} else {
			this.consoleArea.setText(this.consoleArea.getText() + "\n" + s);
		}
	}

	public String[][] getRightTableData() {
		return this.rightTableData;
	}

}
