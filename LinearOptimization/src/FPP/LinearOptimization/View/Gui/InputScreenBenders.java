package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;
import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;
import FPP.LinearOptimization.Model.benders.BendersSolutionData;

public class InputScreenBenders extends JPanel {
	private JPanel panel_functionTable;
	private LinearOptimizationData inputObject;
	private JTable functionTable;
	private JTable variableDefTable;
	private JTable typeDefTable;
	private JTable paramNegIndicesTable;
	private JPanel panel_defTable;
	private Double[][] simplexTableau;
	private JButton btnSubmit;
	private int[] yVariables;
	private BendersSolutionData bendersSolutionObject;
	private int[] parameterNegativeIndices;
	private BendersMasterCoefficientType[] bendersMasterCoefficientType;
	private BendersOptimizationData bendersInputObject;
	private BendersSolutionScreen solutionBenders;
	private MainFrame mainFrame;

	public InputScreenBenders(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initializeScreen();

	}

	private void initializeScreen() {
		JPanel bendersPanel = new JPanel();
		bendersPanel.setBounds(-120, 40, 600, 20);
		this.add(bendersPanel);

		JLabel bendersLabel = new JLabel("Definition von x, y-Variablen und deren Werten:");
		bendersPanel.add(bendersLabel);

		panel_defTable = new JPanel();
		panel_defTable.setLayout(new GridBagLayout());
		panel_defTable.setBounds(50, 80, 455, 80);
		this.add(panel_defTable);

		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validateInput();
				createBendersProblem();
				loadScreen();
			}

		});
		this.add(btnSubmit);

	}

	protected void loadScreen() {
		solutionBenders = new BendersSolutionScreen(mainFrame, bendersSolutionObject);
		solutionBenders.setVisible(true);
		solutionBenders.setLayout(null);
		mainFrame.getTabs().addTab("Benders solution", solutionBenders);
		mainFrame.setTab(2);

	}

	private void createBendersProblem() {
		loadYvariableIndices();
		loadBendersMasterCoefficientTypes();
		loadParamNegIndices();
		bendersInputObject = new BendersOptimizationData(simplexTableau, parameterNegativeIndices, yVariables,
				bendersMasterCoefficientType);
		BendersAlgorithm benders = new BendersAlgorithm();
		bendersSolutionObject = (BendersSolutionData) benders.solve(bendersInputObject);
		System.out.println();
	}

	private void loadParamNegIndices() {
		List<Integer> paramaterNegativeIndicesList = new ArrayList<Integer>();
		for (int i = 0; i < paramNegIndicesTable.getColumnCount(); i++) {
			if (paramNegIndicesTable.getValueAt(0, i).toString().equals("< 0")) {
				paramaterNegativeIndicesList.add(i);
			}
		}
		parameterNegativeIndices = paramaterNegativeIndicesList.stream().mapToInt(i -> i).toArray();
	}

	private void loadYvariableIndices() {
		List<Integer> yVariableIndiciesList = new ArrayList<Integer>();
		for (int i = 0; i < variableDefTable.getColumnCount(); i++) {
			if (variableDefTable.getValueAt(0, i).toString().equals("Y")) {
				yVariableIndiciesList.add(i);
			}
		}
		yVariables = yVariableIndiciesList.stream().mapToInt(i -> i).toArray();
	}

	private void loadBendersMasterCoefficientTypes() {
		bendersMasterCoefficientType = new BendersMasterCoefficientType[yVariables.length];
		for (int i = 0; i < yVariables.length; i++) {
			String type = typeDefTable.getValueAt(0, yVariables[i]).toString();
			switch (type) {
			case "R":
				bendersMasterCoefficientType[i] = BendersMasterCoefficientType.Float;
				break;
			case "I":
				bendersMasterCoefficientType[i] = BendersMasterCoefficientType.Integer;
				break;
			case "B":
				bendersMasterCoefficientType[i] = BendersMasterCoefficientType.Binary;
				break;
			}
		}

	}

	private void validateInput() {
		// TODO Auto-generated method stub

	}

	public void modifyFunctionTable() {

		Object[] comboBoxArr = new Object[simplexTableau[0].length];
		for (int i = 0; i < simplexTableau[0].length; i++) {
			comboBoxArr[i] = new JComboBox();
			((JComboBox) comboBoxArr[i]).addItem("R");
			((JComboBox) comboBoxArr[i]).addItem("I");
		}

		variableDefTable = new JTable(1, simplexTableau[0].length - 1);
		typeDefTable = new JTable(1, simplexTableau[0].length - 1);
		paramNegIndicesTable = new JTable(1, simplexTableau[0].length - 1);
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			TableColumn opColumn = variableDefTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("Y");
			comboBox.addItem("X");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			TableColumn opColumn = typeDefTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("R");
			comboBox.addItem("I");
			comboBox.addItem("B");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			TableColumn opColumn = paramNegIndicesTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("> 0");
			comboBox.addItem("< 0");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
		// Function table not editable
		functionTable.setEnabled(false);

		// Align cell
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			functionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			variableDefTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			typeDefTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			paramNegIndicesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		panel_defTable.add(functionTable, c);
		c.gridy = 1;
		panel_defTable.add(variableDefTable, c);
		c.gridy = 2;
		panel_defTable.add(typeDefTable, c);
		c.gridy = 3;
		panel_defTable.add(paramNegIndicesTable, c);
	}

	public LinearOptimizationData getInputObject() {
		return inputObject;
	}

	public void setInputObject(LinearOptimizationData inputObject) {
		this.inputObject = inputObject;
	}

	public void setFunctionTable(JTable functionTable) {
		functionTable.removeColumn(functionTable.getColumnModel().getColumn(functionTable.getColumnCount() - 1));
		this.functionTable = functionTable;

	}

	public void setSimplexTableau(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;

	}

}
