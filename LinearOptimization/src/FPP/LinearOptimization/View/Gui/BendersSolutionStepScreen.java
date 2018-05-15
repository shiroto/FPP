package FPP.LinearOptimization.View.Gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import FPP.LinearOptimization.Model.benders.BendersStepData;

public class BendersSolutionStepScreen  extends JPanel{

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

	public BendersSolutionStepScreen(MainFrame mainFrame, List<BendersStepData> stepsList) {
		this.mainFrame = mainFrame;
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
		loadComponents();
		loadTable(masterTable, master);
		loadTable(subTable, sub);
		
		loadSolutionTable(masterSolutionTable, masterSolution);
		loadSolutionTable(subSolutionTable, subSolution);

		//TODO: Find right spots in layout for Components
		Helper.addComponent(this, gbl, masterLabel, 0, 0, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, masterTable, 0, 1, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, masterSolutionLabel, 0, 4, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, masterSolutionTable, 0, 5, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, masterRoundButton, 3, 1, 1, 1, 1, 1);
		
		Helper.addComponent(this, gbl, subLabel, 4, 0, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, subTable, 4, 1, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, subSolutionLabel, 4, 4, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, subSolutionTable, 4, 5, 3, 3, 1, 1);
		Helper.addComponent(this, gbl, subRoundButton, 7, 1, 1, 1, 1, 1);
		
		Helper.addComponent(this, gbl, ubLabel, 0, 8, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, lbLabel, 0, 9, 3, 1, 1, 1);
		
		Helper.addComponent(this, gbl, stepLabel, 0, 11, 3, 1, 1, 1);
		Helper.addComponent(this, gbl, stepTextField, 0, 12, 1, 1, 1, 1);
		Helper.addComponent(this, gbl, stepButton, 1, 12, 2, 1, 1, 1);

		
		
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
		masterRoundButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					Double[][] roundMaster;
					roundMaster = Helper.roundStepData(master,1);
					/*DefaultTableModel model = new DefaultTableModel(roundMaster, 0);
					for(int i = 0; i < roundMaster.length; i++) {
						model.addRow(roundMaster[i]);
					}
					masterTable.setModel(model);
					masterTable.setTableHeader(null);
					for (int i = 0; i < roundMaster.length - 1; i++) {
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment(JLabel.CENTER);
						masterTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
					} */
					loadTable(masterTable, roundMaster);
				
			}
		});
		subRoundButton = new JButton("Runden");
		subRoundButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Double[][] roundSub;
				roundSub = Helper.roundStepData(sub,1);
			/*	DefaultTableModel model = new DefaultTableModel(roundSub, 0);
				for(int i = 0; i < roundSub.length; i++) {
					model.addRow(roundSub[i]);
				}
				subTable.setModel(model);
				subTable.setTableHeader(null);
				for (int i = 0; i < roundSub.length - 1; i++) {
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(JLabel.CENTER);
					subTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				} */
				loadTable(subTable,roundSub);
				
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
		//Textfield
		this.stepTextField = new JTextField();
		
		
	}
	
	private void updateRowHeights(JTable table)
	{
	    for (int row = 0; row < table.getRowCount(); row++)
	    {
	        int rowHeight = table.getRowHeight();

	        for (int column = 0; column < table.getColumnCount(); column++)
	        {
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
		mainFrame.getTabs().setTitleAt(mainFrame.getTabs().getSelectedIndex(), "Benders Step: "+step);
		
	}

	private void reset() {
		for(Component c : this.getComponents()) {
			this.remove(c);
		}
		
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
