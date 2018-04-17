package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import FPP.LinearOptimization.Data.LinearOptimizationData;
import javafx.scene.control.TableRow;

public class InputScreenBenders extends JPanel {
	private JPanel panel_functionTable;
	private LinearOptimizationData inputObject;
	private JTable functionTable;
	private JTable variableDefTable;
	private JTable typeDefTable;
	private JPanel panel_defTable;

	public InputScreenBenders() {
		initializeScreen();

	}

	private void initializeScreen() {
		panel_defTable = new JPanel();
		panel_defTable.setLayout(new BorderLayout());
		panel_defTable.setBounds(50, 50, 455, 50);
		this.add(panel_defTable);

	}

	public void modifyFunctionTable() {

		Object[] comboBoxArr = new Object[inputObject.GetCX()];
		for (int i = 0; i < inputObject.GetCX(); i++) {
			comboBoxArr[i] = new JComboBox();
			((JComboBox) comboBoxArr[i]).addItem("R");
			((JComboBox) comboBoxArr[i]).addItem("I");
		}

		variableDefTable = new JTable(1, inputObject.GetCX());
		typeDefTable = new JTable(1, inputObject.GetCX());
		for (int i = 0; i < inputObject.GetCX(); i++) {
			TableColumn opColumn = variableDefTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("Y");
			comboBox.addItem("X");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
		for (int i = 0; i < inputObject.GetCX(); i++) {
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
		for (int i = 0; i < inputObject.GetCX(); i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			functionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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

}
