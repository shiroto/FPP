package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import FPP.LinearOptimization.Model.benders.BendersSolutionData;
import FPP.LinearOptimization.Model.benders.BendersStepData;

public class BendersSolutionScreen extends JPanel {
	private MainFrame mainFrame;
	private BendersSolutionData bendersSolutionObject;
	private JScrollPane scrollPaneSolution;
	private JTable solutionTable;
	private JTable functionTable;
	private Double[] solution;
	private JScrollPane scrollPaneFunction;
	private List<BendersStepData> steps;

	public BendersSolutionScreen(MainFrame mainFrame, BendersSolutionData bendersSolutionObject) {
		this.mainFrame = mainFrame;
		if (bendersSolutionObject.getSteps() != null)
			steps = bendersSolutionObject.getSteps();
		solution = bendersSolutionObject.getOptSolution();
		this.bendersSolutionObject = bendersSolutionObject;
	}

	public void initializeScreen() {
		solutionTable = new JTable(1, bendersSolutionObject.getOptSolution().length);
		// solutionTable.setBounds(446, 374, 435, 225);
		solutionTable.setVisible(true);

		// Tables
		JPanel solutionPanel = new JPanel();
		solutionPanel.setBounds(50, 200, 250, 20);
		this.add(solutionPanel);
		loadOptSolution();
		JLabel solutionLabel = new JLabel("Optimale Lösung");
		solutionPanel.add(solutionLabel);

		JPanel functionPanel = new JPanel();
		functionPanel.setBounds(50, 50, 250, 20);
		this.add(functionPanel);
		JLabel functionLabel = new JLabel("Funktion");
		functionPanel.add(functionLabel);
		// Add Tables
		functionTable.setBounds(60, 90, 523, 60);
		solutionTable.setBounds(60, 250, 523, 60);
		solutionTable.setOpaque(false);
		((DefaultTableCellRenderer) solutionTable.getDefaultRenderer(Object.class)).setOpaque(false);
		functionTable.setOpaque(false);
		((DefaultTableCellRenderer) functionTable.getDefaultRenderer(Object.class)).setOpaque(false);
		this.add(functionTable);
		this.add(solutionTable);

		// Addditional Info
		if (bendersSolutionObject.getAddInfo() != null && bendersSolutionObject.getAddInfo().isEmpty()) {
			JPanel infoPanel = new JPanel();
			infoPanel.setBounds(50, 280, 250, 20);
			this.add(infoPanel);
			JLabel infoLabel = new JLabel("Weitere Informationen: \n" + bendersSolutionObject.getAddInfo());
			infoPanel.add(infoLabel);
		}

		// Steps
		if (steps != null && steps.size() > 0) {
			JPanel stepsPanel = new JPanel();
			// stepsPanel.setLayout(new BorderLayout(10,10));
			stepsPanel.setBounds(50, 330, 250, 100);
			this.add(stepsPanel);
			JLabel stepsLabel = new JLabel("" + steps.size() + " Steps");
			//stepsPanel.add(stepsLabel);
			JTextField stepsTextField = new JTextField();
			stepsTextField.setSize(50, 50);
			JButton stepsButton = new JButton("Gehe zu Step");
			//stepsPanel.add(stepsButton);
			//stepsPanel.add(stepsTextField);
			
			GridBagLayout gbl = new GridBagLayout();
		    stepsPanel.setLayout(gbl);
		    
		    addComponent(stepsPanel, gbl, stepsLabel, 0, 0, 2, 1, 1, 1);
		    addComponent(stepsPanel, gbl, stepsTextField, 0, 1, 1, 1, 1, 1);
		    addComponent(stepsPanel, gbl, stepsButton, 1, 1, 1, 1, 1, 1);

		}
		/*
		 * scrollPaneSolution = new JScrollPane(solutionTable,
		 * JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 * scrollPaneSolution.setBounds(399, 250, 523, 60);
		 * this.add(scrollPaneSolution);
		 * solutionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 * scrollPaneSolution.setBorder(null);
		 * 
		 * scrollPaneFunction = new JScrollPane(functionTable,
		 * JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 * scrollPaneFunction.setBounds(399, 90, 523, 60); this.add(scrollPaneFunction);
		 * solutionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 * scrollPaneFunction.setBorder(null);
		 * 
		 * scrollPaneSolution.repaint(); scrollPaneFunction.repaint();
		 */
	}

	private void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	private void loadOptSolution() {
		DefaultTableModel model = new DefaultTableModel(bendersSolutionObject.getOptSolution(), 0);
		model.addRow(bendersSolutionObject.getOptSolution());
		solutionTable.setModel(model);
		solutionTable.setTableHeader(null);

		// Align
		for (int i = 0; i < solution.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			solutionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}

	public void setFunctionTable(JTable functionTable) {
		this.functionTable = functionTable;
	}

}
