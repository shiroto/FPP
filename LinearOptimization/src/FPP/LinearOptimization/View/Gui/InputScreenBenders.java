package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;
import javafx.scene.control.TableRow;

public class InputScreenBenders extends JPanel {
	private JPanel panel_functionTable;
	private LinearOptimizationData inputObject;
	private JTable functionTable;
	private JTable variableDefTable;
	private JTable typeDefTable;
	private JPanel panel_defTable;
	private Double[][] simplexTableau;
	private JButton btnSubmit;
	private int[] yVariables;
	private BendersMasterCoefficientType[] bendersMasterCoefficientType;

	public InputScreenBenders() {
		initializeScreen();

	}

	private void initializeScreen() {
		JPanel problemPanel = new JPanel();
		problemPanel.setBounds(-120, 40, 600, 20);
		this.add(problemPanel);

		JLabel problemLabel = new JLabel("Definition von x, y-Variablen und deren Werten:");
		problemPanel.add(problemLabel);
		
		panel_defTable = new JPanel();
		panel_defTable.setLayout(new BorderLayout());
		panel_defTable.setBounds(50, 80, 455, 50);
		this.add(panel_defTable);

		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validateInput();
				createBendersProblem();
				//mainFrame.getTabs().addTab("Benders Input", inputBenders);
				//mainFrame.setTab(1);
			}

		});
		this.add(btnSubmit);

	}

	private void createBendersProblem() {
		loadYvariableIndices();
		loadBendersMasterCoefficientTypes();
		BendersOptimizationData bendersInputObject = new BendersOptimizationData(simplexTableau, yVariables,
				bendersMasterCoefficientType);
		System.out.println("");
	}

	private void loadYvariableIndices() {
		yVariables = new int[variableDefTable.getColumnCount()];
		for (int i = 0; i < variableDefTable.getColumnCount(); i++) {
			if (variableDefTable.getValueAt(0, i).toString().equals("X")) {
				yVariables[i] = 0;
			} else
				yVariables[i] = 1;
		}

	}

	private void loadBendersMasterCoefficientTypes() {
		bendersMasterCoefficientType = new BendersMasterCoefficientType[typeDefTable.getColumnCount()];
		for (int i = 0; i < typeDefTable.getColumnCount(); i++) {
			String type = typeDefTable.getValueAt(0, i).toString();
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
		panel_defTable.add(functionTable, BorderLayout.NORTH);
		panel_defTable.add(variableDefTable, BorderLayout.CENTER);
		panel_defTable.add(typeDefTable, BorderLayout.SOUTH);
	}

	public LinearOptimizationData getInputObject() {
		return inputObject;
	}

	public void setInputObject(LinearOptimizationData inputObject) {
		this.inputObject = inputObject;
	}

	public void setFunctionTable(JTable functionTable) {
		this.functionTable = functionTable;

	}

	public void setSimplexTableau(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;

	}

}
