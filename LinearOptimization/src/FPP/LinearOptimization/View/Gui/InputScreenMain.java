package FPP.LinearOptimization.View.Gui;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import FPP.LinearOptimization.Data.Comparator;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Data.Restrictions;
import FPP.LinearOptimization.Data.Algorithm;

public class InputScreenMain extends JPanel {

	private Double[][] simplexTableau;
	private JTextField tf_xVariables;
	// private JTextField tf_yVariables;
	private JTextField tf_restrictions;
	private JTable restrictionTable;
	private JTable functionTable;
	private JButton btnSubmit;
	private LinearOptimizationData inputObject;
	private int countX;
	private ButtonGroup bg;
	private JPanel panel_combo;
	private MainFrame mainFrame;
	private InputScreenBenders inputBenders;
	private int xVariables;
	int restrictions;

	public InputScreenMain(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initializeScreen();
	}

	public void initializeScreen() {
		JPanel jp_xVariables = new JPanel();
		jp_xVariables.setBounds(50, 50, 250, 50);
		this.add(jp_xVariables);

		JLabel lb_xVariables = new JLabel("Anzahl Variablen");
		jp_xVariables.add(lb_xVariables);

		tf_xVariables = new JTextField();
		jp_xVariables.add(tf_xVariables);
		tf_xVariables.setColumns(10);

		JPanel jp_restrictions = new JPanel();
		jp_restrictions.setBounds(50, 150, 250, 50);
		this.add(jp_restrictions);

		JLabel lb_restrictions = new JLabel("Anzahl Restriktionen");
		jp_restrictions.add(lb_restrictions);

		tf_restrictions = new JTextField();
		jp_restrictions.add(tf_restrictions);
		tf_restrictions.setColumns(10);

		JButton btnInput = new JButton("Input");
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO: Eingaben prüfen
				xVariables = Integer.parseInt(tf_xVariables.getText());
				countX = xVariables;
				// int yVariables = Integer.parseInt(tf_yVariables.getText());
				restrictions = Integer.parseInt(tf_restrictions.getText());
				restrictionTable = new JTable(restrictions, xVariables + 2);
				restrictionTable.setBounds(446, 74, 435, 225);
				restrictionTable.setVisible(true);

				functionTable = new JTable(1, xVariables);
				functionTable.setBounds(446, 374, 435, 225);
				functionTable.setVisible(true);

				restrictionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
				functionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

				JTableHeader restTh = restrictionTable.getTableHeader();
				TableColumnModel restTcm = restTh.getColumnModel();
				// restriction
				for (int i = 1; i <= xVariables; i++) {
					TableColumn tc = restTcm.getColumn(i - 1);
					tc.setHeaderValue("x" + i);
				}
				TableColumn tc = restTcm.getColumn(xVariables);
				tc.setHeaderValue("OP");
				tc = restTcm.getColumn(xVariables + 1);
				tc.setHeaderValue("b");

				TableColumn opColumn = restrictionTable.getColumnModel()
						.getColumn(restrictionTable.getColumnCount() - 2);
				JComboBox comboBox = new JComboBox();
				comboBox.addItem("<=");
				comboBox.addItem(">=");
				opColumn.setCellEditor(new DefaultCellEditor(comboBox));

				loadProblemTable();

				// function
				JTableHeader funcTh = functionTable.getTableHeader();
				TableColumnModel funcTcm = funcTh.getColumnModel();
				for (int i = 1; i <= xVariables; i++) {
					TableColumn funcTc = funcTcm.getColumn(i - 1);
					funcTc.setHeaderValue("x" + i);
				}

				loadFunctionTable();
				loadRadioBtn();
				loadCombo();

			}

		});

		// Load Submit btn
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnInput.setBounds(132, 417, 171, 41);
		this.add(btnInput);

		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validateInput();
				createimplexTableau();
				inputBenders = new InputScreenBenders();
				inputBenders.setVisible(true);
				inputBenders.setLayout(null);
				inputBenders.setFunctionTable(functionTable);
				inputBenders.setInputObject(inputObject);
				inputBenders.modifyFunctionTable();
				mainFrame.getTabs().addTab("Benders Input", inputBenders);
				mainFrame.setTab(1);
			}
		});
		this.add(btnSubmit);

	}

	protected void loadCombo() {
		panel_combo = new JPanel();
		panel_combo.setBounds(50, 250, 250, 100);

		JLabel lblAlgorithmus = new JLabel("Algorithmus");
		panel_combo.add(lblAlgorithmus);

		JComboBox comboBox = new JComboBox();
		panel_combo.add(comboBox);

		comboBox.addItem(Algorithm.BendersAlgorithm);
		comboBox.addItem(Algorithm.DanzigAlgorithm);
		this.add(panel_combo);
		this.validate();
	}

	protected void loadRadioBtn() {
		JPanel panel = new JPanel();
		bg = new ButtonGroup();
		panel.setBounds(446, 28, 153, 68);

		JRadioButton rdbtnMin = new JRadioButton("Min");
		rdbtnMin.setActionCommand("min");
		rdbtnMin.setSelected(true);
		JRadioButton rdbtnMax = new JRadioButton("Max");
		rdbtnMax.setActionCommand("max");

		bg.add(rdbtnMax);
		bg.add(rdbtnMin);
		panel.add(rdbtnMin);
		panel.add(rdbtnMax);
		panel.revalidate();
		this.add(panel);
		this.revalidate();
	}

	protected void createimplexTableau() {
		simplexTableau = new Double[restrictions + 1][xVariables + 1];
		for (int rowId = 0; rowId < restrictionTable.getRowCount(); rowId++) {
			for (int columnId = 0; columnId < restrictionTable.getColumnCount()-1; columnId++) {
				if (columnId != xVariables) {
					simplexTableau[rowId][columnId] = Double
							.parseDouble(String.valueOf(restrictionTable.getValueAt(rowId, columnId)));
				} else {
					simplexTableau[rowId][columnId] = Double
							.parseDouble(String.valueOf(restrictionTable.getValueAt(rowId, columnId+1)));
				}
			}
		}
		for (int columnId = 0; columnId < functionTable.getColumnCount(); columnId++) {
			simplexTableau[restrictions][columnId] = Double
					.parseDouble(String.valueOf(functionTable.getValueAt(0, columnId)));
		}
		
		simplexTableau[restrictions][xVariables] = 0.;
		
		for (int rowId = 0; rowId < restrictionTable.getRowCount(); rowId++) {
			String compAsString = String
					.valueOf(restrictionTable.getValueAt(rowId, restrictionTable.getColumnCount() - 2));
			Comparator comp;
			switch (compAsString) {
			case "<=":
				comp = Comparator.LESS_OR_EQUAL;
				break;
			case ">=":
				comp = Comparator.MORE_OR_EQUAL;
				break;
			default:
				comp = Comparator.LESS_OR_EQUAL;
			}
			// inputObject.restriction.comparators[rowId] = comp;
		}
		/*
		 * inputObject.cx = countX; String selection =
		 * bg.getSelection().getActionCommand(); switch(selection) { case "min":
		 * inputObject.maximize=false; break; case "max": inputObject.maximize=true;
		 * break;
		 * 
		 * }
		 */
		System.out.println("");

	}

	protected void validateInput() {

	}

	protected void loadFunctionTable() {
		JScrollPane scrollPane = new JScrollPane(functionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(399, 128, 523, 50);
		this.add(scrollPane);
		functionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setBorder(null);

	}

	protected void loadProblemTable() {
		JScrollPane scrollPane = new JScrollPane(restrictionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(399, 200, 523, 325);
		this.add(scrollPane);
		restrictionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	}

	public LinearOptimizationData getInputObject() {
		return inputObject;
	}

	public void setInputObject(LinearOptimizationData inputObject) {
		this.inputObject = inputObject;
	}

}
