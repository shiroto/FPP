package FPP.LinearOptimization.View.Gui;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import FPP.LinearOptimization.Model.benders.BendersStepData;

public class BendersSolutionStepScreen  extends JPanel{

	private Object mainframe;
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

	public BendersSolutionStepScreen(MainFrame mainFrame, List<BendersStepData> stepsList) {
		this.mainframe = mainframe;
		this.stepsList = stepsList;
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
		
		loadTable(masterTable, master);
		loadTable(subTable, sub);
		
		loadSolutionTable(masterSolutionTable, masterSolution);
		loadSolutionTable(subSolutionTable, subSolution);
		
		//TODO: Find right spots in layout for Components
		
		
	}

	private void loadComponents() {
		//Tables
		this.masterTable = new JTable(master.length, master[0].length);
		this.subTable = new JTable(master.length, master[0].length);
		this.masterSolutionTable = new JTable(1, masterSolution.length);
		this.subSolutionTable = new JTable(1, subSolution.length);
		//Labels
		this.masterSolutionLabel = new JLabel("Lösung Masterproblem");
		this.subSolutionLabel = new JLabel("Lösung SubProblem");
		this.masterLabel = new JLabel("Masterproblem");
		this.subLabel = new JLabel("Subproblem");
		this.ubLabel = new JLabel("Upper Bound = " + ub);
		this.lbLabel = new JLabel("Lower Bound = "+ lb);
		this.stepLabel = new JLabel("Step: " + stepIndex);
		//Buttons
		masterRoundButton = new JButton("Runden");
		subRoundButton = new JButton("Runden");
		stepButton = new JButton("Gehe zu Step");
		//Textfield
		this.stepTextField = new JTextField();
		
		
	}

	private void loadSolutionTable(JTable table, Double[] array) {
		DefaultTableModel model = new DefaultTableModel(array, 0);
		model.addRow(array);
		table.setModel(model);
		table.setTableHeader(null);

		// Align
		for (int i = 0; i < array.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
	}

	private void loadTable(JTable table, Double[][] array) {
		DefaultTableModel model = new DefaultTableModel(array, 0);
		for(int i = 0; i < array.length; i++) {
			model.addRow(array[i]);
		}
		table.setModel(model);
		table.setTableHeader(null);
		// Align
		for (int i = 0; i < array.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
	}

}
