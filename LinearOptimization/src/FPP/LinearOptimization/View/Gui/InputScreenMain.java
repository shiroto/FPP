package FPP.LinearOptimization.View.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Data.Comparator;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.View.Save.SaveableIF;

public class InputScreenMain extends JPanel {

	private Double[][] simplexTableau;
	private JTextField tf_xVariables;
	private JTextField tf_restrictions;
	private JTable restrictionTable;
	private JTable functionTable;
	private JScrollPane scrollPaneRestrictions;
	private JScrollPane scrollPaneFunction;
	private JButton btnSubmit;
	private LinearOptimizationData inputObject;
	private JPanel panel;
	private ButtonGroup bg;
	private JPanel panel_combo;
	private MainFrame mainFrame;
	private InputScreenBenders inputBenders;
	private int xVariables;
	int restrictions;
	private JRadioButton rdbtnMin = new JRadioButton("Min");
	private JRadioButton rdbtnMax = new JRadioButton("Max");
	private JComboBox<String> cbAlgorithm;
	private InputScreenDantzig inputDanzig;
	public InputScreenBB inputBB;
	private JButton btnInput;

	public InputScreenMain(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initializeScreen();
	}

	/**
	 * Initializing the Main Input Screen.
	 */
	public void initializeScreen() {
		initializeComponents();

		btnInput = new JButton("Eingabe");
		btnInput.setBounds(75, 340, 100, 30);
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Eingaben prüfen
				if (!validateInput())
					return;
				reset();
				
				initializeInputComponents();
				loadFunctionTable();
				loadRadioBtn();
				loadCombo();
				revalidate();
				repaint();

			}

		});
		this.add(btnInput);
	}

	/*
	 * Open and load the benders input screen.
	 */
	protected void loadBendersScreen() {
		inputBenders = new InputScreenBenders(mainFrame);
		inputBenders.setVisible(true);
		inputBenders.setLayout(null);
		inputBenders.setFunctionTable(new JTable(getFunctionTable().getModel()));
		inputBenders.setMinProblem(rdbtnMin.isSelected());
		inputBenders.setOPs(loadOPs());
		inputBenders.setSimplexTableau(simplexTableau);
		inputBenders.setNumRestr(restrictions);
		inputBenders.setNumVar(xVariables);
		inputBenders.modifyFunctionTable();
		mainFrame.getTabs().addTab(Helper.Keyword.INPUTBENDERS, inputBenders);
		mainFrame.getTabs().setSelectedIndex(mainFrame.getTabs().indexOfTab(Helper.Keyword.INPUTBENDERS));
		
	}

	/*
	 * Load all operators from the RestrictionTable.
	 */
	private String[] loadOPs() {
		String[] ops = new String[restrictions];
		for(int i = 0; i < restrictions; i++) {
			ops[i] = (String) restrictionTable.getValueAt(i, xVariables);
		}
		return ops;
		
	}

	/*
	 * Setting all components of the input screen.
	 */
	protected void initializeInputComponents() {
		xVariables = Integer.parseInt(tf_xVariables.getText());
		restrictions = Integer.parseInt(tf_restrictions.getText());
		setRestrictionTable(new JTable(restrictions, xVariables + 2));
		getRestrictionTable().setBounds(446, 74, 435, 225);
		getRestrictionTable().setVisible(true);
		//+1 für Schlupfvariable
		setFunctionTable(new JTable(1, xVariables+1));
		getFunctionTable().setBounds(446, 374, 435, 225);
		getFunctionTable().setVisible(true);

		getRestrictionTable().putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		getFunctionTable().putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		JTableHeader restTh = getRestrictionTable().getTableHeader();
		TableColumnModel restTcm = restTh.getColumnModel();
		// restriction
		for (int i = 1; i <= xVariables; i++) {
			TableColumn tc = restTcm.getColumn(i - 1);
			tc.setHeaderValue("<html>x<sub>"+i+"</sub></html>");
		}
		TableColumn tc = restTcm.getColumn(xVariables);
		tc.setHeaderValue("OP");
		tc = restTcm.getColumn(xVariables + 1);
		tc.setHeaderValue("b");

		TableColumn opColumn = getRestrictionTable().getColumnModel()
				.getColumn(getRestrictionTable().getColumnCount() - 2);
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("<=");
		comboBox.addItem(">=");
		opColumn.setCellEditor(new DefaultCellEditor(comboBox));

		loadProblemTable();

		// function
		JTableHeader funcTh = getFunctionTable().getTableHeader();
		TableColumnModel funcTcm = funcTh.getColumnModel();
		for (int i = 1; i <= xVariables; i++) {
			TableColumn funcTc = funcTcm.getColumn(i - 1);
			funcTc.setHeaderValue("<html>x<sub>"+i+"</sub></html>");
		}
		TableColumn funcTc = funcTcm.getColumn(xVariables);
		funcTc.setHeaderValue("Schlupfvariable");
		//Zelle für Schlupfvariable initial 0
		getFunctionTable().setValueAt(0, 0, getFunctionTable().getColumnCount()-1);
		// Load Submit btn
		btnSubmit = new JButton("Weiter");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!validateTableInput())
					return;
				createSimplexTableau();
				if(mainFrame.getTabs().getTabCount() > 1)
					mainFrame.getTabs().remove(1);
				if(cbAlgorithm.getSelectedItem().toString().equals(Algorithm.BendersAlgorithm.getScreenName())) {
					loadBendersScreen();
				}
				else if (cbAlgorithm.getSelectedItem().toString().equals(Algorithm.DantzigAlgorithm.getScreenName())) {
					loadDanzigScreen();
				}
				else if (cbAlgorithm.getSelectedItem().toString().equals(Algorithm.BranchBoundAlgorithm.getScreenName())) {
					loadBBScreen();
				}
				
			}

		});
		this.add(btnSubmit);
		
	}

	protected void loadBBScreen() {
		inputBB = new InputScreenBB(mainFrame);
		inputBB.setVisible(true);
		inputBB.setLayout(null);
		JTable functionTableCopy = new JTable(getFunctionTable().getModel());
		JTableHeader functionTableCopyHeader = new JTableHeader();
		functionTableCopyHeader.setColumnModel(functionTable.getColumnModel());
		functionTableCopy.setTableHeader(functionTableCopyHeader);
		inputBB.setFunctionTable(functionTableCopy);
		JTable restrictionTableCopy = new JTable(getRestrictionTable().getModel());
		JTableHeader restrictionTableCopyHeader = new JTableHeader();
		restrictionTableCopyHeader.setColumnModel(restrictionTable.getColumnModel());
		restrictionTableCopy.setTableHeader(restrictionTableCopyHeader);
		inputBB.setRestrictionTable(restrictionTableCopy);
		inputBB.setNumRestr(restrictions);
		inputBB.setNumVar(xVariables);
		inputBB.setSimplexTableau(simplexTableau);
		inputBB.setMinProblem(rdbtnMin.isSelected());
		inputBB.setOPs(loadOPs());
		inputBB.initializeScreen();
		mainFrame.getTabs().addTab(Helper.Keyword.INPUTBANDB, inputBB);
		mainFrame.getTabs().setSelectedIndex(mainFrame.getTabs().indexOfTab(Helper.Keyword.INPUTBANDB));
		
	}

	protected void loadDanzigScreen() {
		inputDanzig = new InputScreenDantzig(mainFrame);
		inputDanzig.setVisible(true);
		inputDanzig.setLayout(null);
		inputDanzig.setFunctionTable(new JTable(getFunctionTable().getModel()));
		inputDanzig.setSimplexTableau(simplexTableau);
		mainFrame.getTabs().addTab(Helper.Keyword.INPUTDANTZIG, inputDanzig);
		mainFrame.getTabs().setSelectedIndex(mainFrame.getTabs().indexOfTab(Helper.Keyword.INPUTDANTZIG));
		
	}

	private void initializeComponents() {
		JPanel jp_xVariables = new JPanel();
		jp_xVariables.setBounds(50, 50, 150, 70);
		this.add(jp_xVariables);

		JLabel lb_xVariables = new JLabel("Anzahl Variablen:");
		jp_xVariables.add(lb_xVariables);

		tf_xVariables = new JTextField();
		tf_xVariables.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnInput.doClick();
			}
		});
		jp_xVariables.add(tf_xVariables);
		tf_xVariables.setColumns(10);

		JPanel jp_restrictions = new JPanel();
		jp_restrictions.setBounds(50, 180, 150, 70);
		this.add(jp_restrictions);

		JLabel lb_restrictions = new JLabel("Anzahl Restriktionen:");
		jp_restrictions.add(lb_restrictions);

		tf_restrictions = new JTextField();
		tf_restrictions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnInput.doClick();
			}
			
		});
		jp_restrictions.add(tf_restrictions);
		tf_restrictions.setColumns(10);
		
	}

	/*
	 * Reset all components of the input screen.
	 */
	private void reset() {
		if (panel_combo != null) {
			this.remove(panel_combo);
			scrollPaneFunction.removeAll();
			scrollPaneRestrictions.removeAll();
			this.remove(scrollPaneFunction);
			this.remove(scrollPaneRestrictions);
			this.remove(panel);
			this.remove(getRestrictionTable());
			this.revalidate();
			this.repaint();
		}
	}

	/*
	 * Check for valid inputs in all input tables.
	 */
	protected boolean validateTableInput() {
		if (cbAlgorithm.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Algorithmus aus.");
			return false;
		}
		for (int rowId = 0; rowId < getRestrictionTable().getRowCount(); rowId++) {
			for (int columnId = 0; columnId < getRestrictionTable().getColumnCount(); columnId++) {
				if (columnId != xVariables) {
					if (!Helper.isNumeric(String.valueOf(getRestrictionTable().getValueAt(rowId, columnId)))) {
						JOptionPane.showMessageDialog(null, "Bitte geben Sie nur numerische Werte ein.");
						return false;
					}

				}
			}
		}
		for (int columnId = 0; columnId < getFunctionTable().getColumnCount(); columnId++) {
			if (!Helper.isNumeric(String.valueOf(getFunctionTable().getValueAt(0, columnId)))) {
				JOptionPane.showMessageDialog(null, "Bitte geben Sie nur numerische Werte ein.");
				return false;
			}
		}
		return true;
	}

	/*
	 * Load the combobox with all Algorithms inside.
	 */
	protected void loadCombo() {
		panel_combo = new JPanel();
		panel_combo.setBounds(980, 350, 250, 100);

		JLabel lblAlgorithmus = new JLabel("Algorithmus: ");
		panel_combo.add(lblAlgorithmus);

		cbAlgorithm = new JComboBox<String>();
		panel_combo.add(cbAlgorithm);
		cbAlgorithm.addItem(Algorithm.BendersAlgorithm.getScreenName());
		//DantzigWolfe not implemented
		//cbAlgorithm.addItem(Algorithm.DantzigAlgorithm.getScreenName());
		cbAlgorithm.addItem(Algorithm.BranchBoundAlgorithm.getScreenName());
		cbAlgorithm.setSelectedItem(null);
		this.add(panel_combo);
		this.validate();
	}

	/*
	 * Load the Min / Max Radio buttons.
	 */
	protected void loadRadioBtn() {
		panel = new JPanel();
		bg = new ButtonGroup();
		panel.setBounds(446, 28, 153, 68);

		rdbtnMin.setActionCommand("min");
		rdbtnMax.setActionCommand("max");
		rdbtnMin.setSelected(true);

		bg.add(rdbtnMax);
		bg.add(rdbtnMin);
		panel.add(rdbtnMin);
		panel.add(rdbtnMax);
		panel.revalidate();
		this.add(panel);
		this.revalidate();
	}

	/*
	 * Creates the simplex tableau, with all the inputs of the tables.
	 */
	protected void createSimplexTableau() {
		simplexTableau = new Double[restrictions + 1][xVariables + 1];
		for (int rowId = 0; rowId < getRestrictionTable().getRowCount(); rowId++) {
			for (int columnId = 0; columnId < getRestrictionTable().getColumnCount() - 1; columnId++) {
				if (columnId != xVariables) {
					simplexTableau[rowId][columnId] = Double
							.parseDouble(String.valueOf(getRestrictionTable().getValueAt(rowId, columnId)));
				} else {
					simplexTableau[rowId][columnId] = Double
							.parseDouble(String.valueOf(getRestrictionTable().getValueAt(rowId, columnId + 1)));
				}
			}
		}
		for (int columnId = 0; columnId < getFunctionTable().getColumnCount(); columnId++) {
			simplexTableau[restrictions][columnId] = Double
					.parseDouble(String.valueOf(getFunctionTable().getValueAt(0, columnId)));
		}


		for (int rowId = 0; rowId < getRestrictionTable().getRowCount(); rowId++) {
			String compAsString = String
					.valueOf(getRestrictionTable().getValueAt(rowId, getRestrictionTable().getColumnCount() - 2));
			Comparator comp;
			switch (compAsString) {
			case "<=":
				break;
			case ">=":
				// Umformung in Standardform falls größer gleich
				for (int entry = 0; entry < xVariables + 1; entry++) {
					simplexTableau[rowId][entry] = simplexTableau[rowId][entry] * (-1);
				}
				break;
			}
		}
		// Umformung der Zielfunktion, falls Maximierungsproblem ausgewählt wurde
		if (rdbtnMax.isSelected()) {
			for (int entry = 0; entry < xVariables; entry++) {
				simplexTableau[restrictions][entry] = simplexTableau[restrictions][entry] * (-1);
			}
		}
		System.out.println("SimplexTableau fertiggestellt");
		System.out.println(Arrays.deepToString(simplexTableau));
		System.out.println("");

	}

	protected boolean validateInput() {
		if (!Helper.isInteger(tf_restrictions.getText()) || !Helper.isNumeric(tf_xVariables.getText())) {
			JOptionPane.showMessageDialog(null, "Bitte geben Sie nur numerische Werte ein.");
			return false;
		} else
			return true;
	}

	protected void loadFunctionTable() {
		JPanel functionPanel = new JPanel();
		functionPanel.setBounds(310, 100, 250, 20);
		this.add(functionPanel);

		JLabel functionLabel = new JLabel("Zielfunktion");
		functionPanel.add(functionLabel);
		
		scrollPaneFunction = new JScrollPane(getFunctionTable(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneFunction.setBounds(399, 128, 523, 60);
		this.add(scrollPaneFunction);
		getFunctionTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPaneFunction.setBorder(null);
		scrollPaneFunction.repaint();
	}

	protected void loadProblemTable() {
		JPanel problemPanel = new JPanel();
		problemPanel.setBounds(310, 210, 250, 20);
		this.add(problemPanel);

		JLabel problemLabel = new JLabel("Restriktionen");
		problemPanel.add(problemLabel);
		
		scrollPaneRestrictions = new JScrollPane(getRestrictionTable(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneRestrictions.setBounds(399, 250, 523, 325);
		this.add(scrollPaneRestrictions);
		getRestrictionTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	public LinearOptimizationData getInputObject() {
		return inputObject;
	}

	public void setInputObject(LinearOptimizationData inputObject) {
		this.inputObject = inputObject;
	}

	public void loadSimplexTableau(SaveableIF objekt) {
		tf_restrictions.setText(String.valueOf(objekt.getNumRestr()));
		tf_xVariables.setText(String.valueOf(objekt.getNumVar()));
		btnInput.doClick();
		for (int rowId = 0; rowId < getRestrictionTable().getRowCount(); rowId++) {
			for (int columnId = 0; columnId < getRestrictionTable().getColumnCount() - 1; columnId++) {
				if (columnId != xVariables) {
					getRestrictionTable().setValueAt(objekt.getArray()[rowId][columnId], rowId, columnId);
				} else {
					getRestrictionTable().setValueAt("<=", rowId, columnId);
					getRestrictionTable().setValueAt(objekt.getArray()[rowId][columnId], rowId, columnId+1);
				}
			}
		}
		for (int columnId = 0; columnId < objekt.getFunction().length; columnId++) {
			getFunctionTable().setValueAt(objekt.getFunction()[columnId],0, columnId);
			getFunctionTable().setValueAt(0, getFunctionTable().getRowCount()-1, getFunctionTable().getColumnCount()-1);
		}
		
		
	}

	public JTable getFunctionTable() {
		return functionTable;
	}

	public void setFunctionTable(JTable functionTable) {
		this.functionTable = functionTable;
	}

	public JTable getRestrictionTable() {
		return restrictionTable;
	}

	public void setRestrictionTable(JTable restrictionTable) {
		this.restrictionTable = restrictionTable;
	}

	public void setAlgorithm(Algorithm branchboundalgorithm) {
		cbAlgorithm.setSelectedItem(branchboundalgorithm.getScreenName());
		
	}

	public void setOPs(String[] oPs) {
		if(oPs != null) {
			for(int i = 0; i < restrictions; i++) {
				restrictionTable.setValueAt(oPs[i], i, xVariables);
			}
		}

		
	}

	public void setMinProblem(boolean min) {
		if(min) {
			rdbtnMax.setSelected(false);
			rdbtnMin.setSelected(true);
		} else {
			rdbtnMax.setSelected(true);
			rdbtnMin.setSelected(false);
		}
		
	}

}
