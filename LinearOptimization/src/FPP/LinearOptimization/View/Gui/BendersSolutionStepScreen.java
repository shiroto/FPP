package FPP.LinearOptimization.View.Gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import FPP.LinearOptimization.Model.benders.BendersSolutionData;
import FPP.LinearOptimization.Model.benders.BendersStepData;

public class BendersSolutionStepScreen extends JPanel {

	private MainFrame mainFrame;
	private List<BendersStepData> stepsList;
	private int stepIndex;
	private BendersStepData step;
	private Double[][] master;
	private JTable masterTable;
	private Double[][] sub;
	private Double[] masterSolution;
	private Double[] subSolution;
	private JTable subTable;
	private JTable masterSolutionTable;
	private JTable subSolutionTable;
	private double ub;
	private double lb;
	private JLabel masterSolutionLabel;
	private JLabel subSolutionLabel;
	private JLabel masterLabel;
	private JLabel subLabel;
	private JLabel ubLabel;
	private JLabel lbLabel;
	private JLabel stepLabel;
	private JButton masterRoundButton;
	private JButton subRoundButton;
	private JButton stepButton;
	private JTextField stepTextField;
	private JButton nextStepButton;
	private JButton prevStepButton;
	private JButton finalStepButton;
	private JLabel roundingHint;
	private BendersSolutionData bendersSolutionObject;

	public BendersSolutionStepScreen(MainFrame mainFrame, BendersSolutionData bendersSolutionObject) {
		this.mainFrame = mainFrame;
		this.bendersSolutionObject = bendersSolutionObject;
		this.stepsList = bendersSolutionObject.getSteps();
	}

	public void initializeScreen(int stepIndex) {
		
		this.stepIndex = stepIndex;
		this.step = stepsList.get(stepIndex);
		this.master = step.getMasterProblem();
		this.sub = step.getSubProblem();
		this.masterSolution = step.getMasterSolution();
		this.subSolution = step.getSubSolution();
		this.ub = step.getUpperBound();
		this.lb = step.getLowerBound();
		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);
		loadComponents();
		loadTable(masterTable, master);
		loadTable(subTable, sub);

		loadSolutionTable(masterSolutionTable, masterSolution);
		loadSolutionTable(subSolutionTable, subSolution);
		
		//Header
		loadTableHeader(masterTable);
		loadTableHeader(subTable);
		loadTableHeader(masterSolutionTable);
		loadTableHeader(subSolutionTable);
		
		//ScrollPanes
		JScrollPane scrollPaneMaster= new  JScrollPane(masterTable);
		JScrollPane scrollPaneSub= new  JScrollPane(subTable);
		JScrollPane scrollPaneMasterSol= new  JScrollPane(masterSolutionTable);
		JScrollPane scrollPaneSubSol= new  JScrollPane(subSolutionTable);
		
		// TODO: Find right spots in layout for Components
		Helper.addComponent(this, gbl, masterLabel, 0, 0, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, scrollPaneMaster, 0, 1, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, masterSolutionLabel, 0, 4, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, scrollPaneMasterSol, 0, 5, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, masterRoundButton, 3, 1, 1, 1, 0, 0);

		Helper.addComponent(this, gbl, subLabel, 4, 0, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, scrollPaneSub, 4, 1, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, subSolutionLabel, 4, 4, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, scrollPaneSubSol, 4, 5, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, subRoundButton, 7, 1, 1, 1, 0, 0);

		Helper.addComponent(this, gbl, ubLabel, 0, 8, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, lbLabel, 0, 9, 3, 1, 1, 1);

		Helper.addComponent(this, gbl, stepLabel, 0, 11, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, roundingHint, 5, 11, 2, 1, 1, 1);
		Helper.addComponent(this, gbl, stepTextField, 0, 12, 1, 1, 1, 1);
		Helper.addComponent(this, gbl, stepButton, 1, 12, 2, 1, 1, 0.5);
		Helper.addComponent(this, gbl, prevStepButton, 3, 12, 2, 1, 1, 0.5);
		Helper.addComponent(this, gbl, nextStepButton, 5, 12, 2, 1, 1, 0.5);
		Helper.addComponent(this, gbl, finalStepButton, 7, 12, 2, 1, 1, 0.5);

	}

	private void loadTableHeader(JTable table) {
		int[] yIndices = bendersSolutionObject.getBendersOptimizationData().getYVariableIndices();
		int xIndicesCount = table.getColumnCount() - yIndices.length;
		Arrays.sort(yIndices);
		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		int yCount = 0;
		// x indices
		for (int i = 1; i <= xIndicesCount + yIndices.length; i++) {
			for (int j = 1; j <= yIndices.length; j++) {
				if (yIndices[j - 1] == i - 1) {
					TableColumn tc = tcm.getColumn(i - 1);
					tc.setHeaderValue("<html>y<sub>" + j + "</sub></html>");
					yCount++;
					break;
				} else {
					TableColumn tc = tcm.getColumn(i - 1);
					tc.setHeaderValue("<html>x<sub>" + (i - yCount) + "</sub></html>");
				}
			}

		}
		TableColumn tc = tcm.getColumn(table.getColumnCount() - 1);
		tc.setHeaderValue("L�sung");
	}

	private void loadComponents() {
		// Tables
		this.masterTable = new JTable(master.length, master[0].length);
		this.subTable = new JTable(master.length, master[0].length);
		this.masterSolutionTable = new JTable(1, masterSolution.length);
		this.subSolutionTable = new JTable(1, subSolution.length);
		// Labels
		this.masterSolutionLabel = new JLabel("L�sung Masterproblem");
		this.subSolutionLabel = new JLabel("L�sung SubProblem");
		this.masterLabel = new JLabel("Masterproblem");
		this.subLabel = new JLabel("Duales Problem zum Subproblem");
		this.ubLabel = new JLabel("Upper Bound = " + ub);
		this.lbLabel = new JLabel("Lower Bound = " + lb);
		this.stepLabel = new JLabel("Step: " + stepIndex + " von " + (stepsList.size() - 1));
		// Buttons
		masterRoundButton = new JButton("Gerundet");
		masterRoundButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (masterRoundButton.getText() == "Gerundet") {
					Double[][] roundMaster;
					Double[] roundMasterSolution;
					roundMaster = Helper.roundStepData(master, 1);
					loadTable(masterTable, roundMaster);
					loadTableHeader(masterTable);
					roundMasterSolution = Helper.roundSolutionData(masterSolution, 1);
					loadSolutionTable(masterSolutionTable, roundMasterSolution);
					loadTableHeader(masterSolutionTable);
					ubLabel.setText("Upper Bound = " + Helper.round(ub, 1));
					lbLabel.setText("Lower Bound = " + Helper.round(lb, 1));
					masterRoundButton.setText("Ungerundet");
				} else {
					loadTable(masterTable, master);
					loadTableHeader(masterTable);
					loadSolutionTable(masterSolutionTable, masterSolution);
					loadTableHeader(masterSolutionTable);
					ubLabel.setText("Upper Bound = " + ub);
					lbLabel.setText("Lower Bound = " + lb);
					masterRoundButton.setText("Gerundet");
				}

			}
		});
		subRoundButton = new JButton("Gerundet");
		subRoundButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (subRoundButton.getText() == "Gerundet") {
					Double[][] roundSub;
					Double[] roundSubSolution;
					roundSub = Helper.roundStepData(sub, 1);
					loadTable(subTable, roundSub);
					loadTableHeader(subTable);
					roundSubSolution = Helper.roundSolutionData(subSolution, 1);
					loadSolutionTable(subSolutionTable, roundSubSolution);
					loadTableHeader(subSolutionTable);
					ubLabel.setText("Upper Bound = " + Helper.round(ub, 1));
					lbLabel.setText("Lower Bound = " + Helper.round(lb, 1));
					subRoundButton.setText("Ungerundet");
				} else {
					loadTable(subTable, sub);
					loadTableHeader(subTable);
					loadSolutionTable(subSolutionTable, subSolution);
					loadTableHeader(subSolutionTable);
					lbLabel.setText("Lower Bound = " + lb);
					ubLabel.setText("Upper Bound = " + ub);
					subRoundButton.setText("Gerundet");
				}

			}
		});
		stepButton = new JButton("Gehe zu Step");
		stepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!validateInput())
					return;
				loadNewStep();

			}
		});
		// Textfield
		this.stepTextField = new JTextField();
		prevStepButton = new JButton("<");
		prevStepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((stepIndex - 1) >= 0) {
					decreaseStep();
				} else {
					return;
				}

			}
		});
		nextStepButton = new JButton(">");
		nextStepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((stepIndex + 1) < stepsList.size()) {
					increaseStep();
				} else {
					return;
				}

			}
		});
		finalStepButton = new JButton("Final Step");
		finalStepButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				goToLastStep();
			}
		});
		roundingHint = new JLabel(
				"<html><body>Alle Berechnungen wurden mit<br> ungerundeten Werten durchgef�hrt.</body></html>");

	}

	private void updateRowHeights(JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			int rowHeight = table.getRowHeight();

			for (int column = 0; column < table.getColumnCount(); column++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}

			table.setRowHeight(row, rowHeight);
		}
	}

	protected boolean validateInput() {
		if (!Helper.isNumeric(String.valueOf(stepTextField.getText()))) {
			JOptionPane.showMessageDialog(null, "Bitte geben Sie nur numerische Werte ein.");
			return false;
		} else
			return true;
	}

	protected void loadNewStep() {
		int step = Integer.valueOf(stepTextField.getText());
		reset();
		initializeScreen(step);
		mainFrame.getTabs().setTitleAt(mainFrame.getTabs().getSelectedIndex(), "Benders Step: " + step);

	}

	protected void increaseStep() {
		reset();
		initializeScreen(stepIndex + 1);
		mainFrame.getTabs().setTitleAt(mainFrame.getTabs().getSelectedIndex(), "Benders Step: " + (stepIndex));
	}

	protected void decreaseStep() {
		reset();
		initializeScreen(stepIndex - 1);
		mainFrame.getTabs().setTitleAt(mainFrame.getTabs().getSelectedIndex(), "Benders Step: " + (stepIndex));
	}

	protected void goToLastStep() {
		reset();
		initializeScreen(stepsList.size() - 1);
		mainFrame.getTabs().setTitleAt(mainFrame.getTabs().getSelectedIndex(), "Benders Step: " + (stepIndex));
	}

	private void reset() {
		for (Component c : this.getComponents()) {
			this.remove(c);
		}

	}

	private void loadSolutionTable(JTable table, Double[] array) {
		DefaultTableModel model = new DefaultTableModel(array, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells not editable
				return false;
			}
		};
		model.addRow(array);
		table.setModel(model);
		//table.setTableHeader(null);

		// Align
		for (int i = 0; i < array.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

	}

	private void loadTable(JTable table, Double[][] array) {
		DefaultTableModel model = new DefaultTableModel(array, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return super.isCellEditable(row, column);
			}
		};
		for (int i = 0; i < array.length; i++) {
			model.addRow(array[i]);
		}
		table.setModel(model);
		//table.setTableHeader(null);
		// Align
		for (int i = 0; i < array.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

	}

}
