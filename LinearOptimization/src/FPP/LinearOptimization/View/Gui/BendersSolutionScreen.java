package FPP.LinearOptimization.View.Gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import FPP.LinearOptimization.Model.benders.BendersSolutionData;

public class BendersSolutionScreen extends JPanel {
	private MainFrame mainFrame;
	private BendersSolutionData bendersSolutionObject;
	private JScrollPane scrollPaneFunction;
	private JTable functionTable;
	private Double[] solution;

	public BendersSolutionScreen(MainFrame mainFrame, BendersSolutionData bendersSolutionObject) {
		this.mainFrame = mainFrame;
		solution = bendersSolutionObject.getOptSolution();
		this.bendersSolutionObject = bendersSolutionObject;
		initializeScreen();
	}

	private void initializeScreen() {
		functionTable = new JTable(1, bendersSolutionObject.getOptSolution().length);
		functionTable.setBounds(446, 374, 435, 225);
		functionTable.setVisible(true);

		JPanel functionPanel = new JPanel();
		functionPanel.setBounds(310, 100, 250, 20);
		this.add(functionPanel);
		loadOptSolution();
		JLabel functionLabel = new JLabel("Optimale Lösung");
		functionPanel.add(functionLabel);

		scrollPaneFunction = new JScrollPane(functionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneFunction.setBounds(399, 128, 523, 60);
		this.add(scrollPaneFunction);
		functionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPaneFunction.setBorder(null);
		scrollPaneFunction.repaint();

	}

	private void loadOptSolution() {
		DefaultTableModel model = new DefaultTableModel(bendersSolutionObject.getOptSolution(), 0);
		model.addRow(bendersSolutionObject.getOptSolution());
		functionTable.setModel(model);
		functionTable.setTableHeader(null);
		
		//Align
		for (int i = 0; i < solution.length - 1; i++) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			functionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}

}
