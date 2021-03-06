package FPP.LinearOptimization.View.Gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.ILinearOptimization;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;
import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;
import FPP.LinearOptimization.Model.benders.BendersSolutionData;
import FPP.LinearOptimization.View.Save.BendersSaveClass;
import FPP.LinearOptimization.View.Save.LinearOptFileHandler;

public class InputScreenBenders extends JPanel implements InputScreenIF {
	private JPanel panel_functionTable;
	private LinearOptimizationData inputObject;
	private JTable functionTable;
	private JTable variableDefTable;
	private JTable typeDefTable;
	private JTable paramNegIndicesTable;
	private JScrollPane scrollPane;
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
	private int numRestr, numVar;
	private boolean minProblem;
	private String[] operators;
	private JFrame frame;
	private List<JComboBox> xyBoxes;
	private List<JComboBox> typeBoxes;
	private JLabel variables;
	private JLabel valueRange;
	
	public InputScreenBenders(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initializeScreen();

	}

	private void initializeScreen() {
		xyBoxes = new ArrayList<JComboBox>();
		typeBoxes = new ArrayList<JComboBox>();
		JPanel bendersPanel = new JPanel();
		bendersPanel.setBounds(40, 40, 600, 80);
		this.add(bendersPanel);

		JLabel bendersLabel = new JLabel("<html><body>Definition von x, y-Variablen:<br>"
				+ "Y-Variablen sind Entscheidungsvariablen des Masterproblems<br>"
				+ "X-Variablen sind Entscheidungsvariablen des Subproblems</body></html>");
		bendersPanel.add(bendersLabel);

		panel_defTable = new JPanel();
		panel_defTable.setLayout(new GridBagLayout());
		scrollPane = new JScrollPane(panel_defTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(160, 120, 600, 80);
		this.add(scrollPane);
		variables = new JLabel("Auswahl Variablenart:");
		variables.setBounds(0, 140, 150, 20);
		this.add(variables);
		valueRange = new JLabel("Auswahl Wertebereich:");
		valueRange.setBounds(0, 170, 150, 20);
		this.add(valueRange);


		

		btnSubmit = new JButton("Berechnen");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					validateInput();
					try {
						createBendersProblem();
						loadScreen();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Problem nicht l�sbar. ", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception exep) {
					JOptionPane.showMessageDialog(frame, "Fehlerhafte Eingabe: " + exep.getMessage(), "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		this.add(btnSubmit);
		// Keeping previous Zoom State
		for (int i = 0; i < mainFrame.getZoomState(); i++) {
			for (Component c : this.getComponents())
				mainFrame.zoomIn(c);
		}

	}

	protected void loadScreen() {
		solutionBenders = new BendersSolutionScreen(mainFrame, bendersSolutionObject);
		solutionBenders.setFunctionTable(new JTable(functionTable.getModel()));
		solutionBenders.setVisible(true);
		solutionBenders.setLayout(null);
		solutionBenders.initializeScreen();
		mainFrame.getTabs().addTab(Helper.Keyword.SOLUTIONBENDERS, solutionBenders);
		mainFrame.getTabs().setSelectedIndex(mainFrame.getTabs().indexOfTab(Helper.Keyword.SOLUTIONBENDERS));

	}

	private void createBendersProblem() {
		loadYvariableIndices();
		loadBendersMasterCoefficientTypes();
		loadParamNegIndices();
		bendersInputObject = new BendersOptimizationData(simplexTableau, parameterNegativeIndices, yVariables,
				bendersMasterCoefficientType);
		ILinearOptimizationSolutionData benders = mainFrame.GetController().createAlgorithm(bendersInputObject,
				Algorithm.BendersAlgorithm);
		bendersSolutionObject = (BendersSolutionData) benders;
		System.out.println();
	}

	private void loadParamNegIndices() {
		List<Integer> paramaterNegativeIndicesList = new ArrayList<Integer>();
		/*for (int i = 0; i < paramNegIndicesTable.getColumnCount(); i++) {
			if (paramNegIndicesTable.getValueAt(0, i).toString().equals("<= 0")) {
				paramaterNegativeIndicesList.add(i);
			}
		}*/
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

	private void validateInput() throws Exception {
		int countY = 0;
		for (int columnId = 0; columnId < variableDefTable.getColumnCount(); columnId++) {
			if (variableDefTable.getValueAt(0, columnId) == null) {
				throw new Exception("Geben Sie alle x bzw. y-Variablen an.");
			}
		}
		for (int columnId = 0; columnId < typeDefTable.getColumnCount(); columnId++) {
			if (variableDefTable.getValueAt(0, columnId).toString().equals("Y")) {
				countY += 1;
				if (typeDefTable.getValueAt(0, columnId) == null) {
					throw new Exception("Y-Variable ben�tigt einen Wert.");
				}
			}
		}
		/*for (int columnId = 0; columnId < paramNegIndicesTable.getColumnCount(); columnId++) {
			if (paramNegIndicesTable.getValueAt(0, columnId) == null) {
				throw new Exception("Geben Sie den Wertebereich f�r jede Variable an.");
			}
		}*/
		if (countY == 0) {
			throw new Exception("Eingabe f�r Benders Algorithmus nicht geeignet. Y-Variable fehlt.");
		}
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
			//When variable X is selected, "R" is selected as type by default
			comboBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					String item = (String)e.getItem();
					int step = xyBoxes.indexOf(e.getSource());
					JComboBox typeBox = typeBoxes.get(step);
					if (item.equals("X")) {
						typeDefTable.setEnabled(false);
						typeBox.setSelectedItem("R");
						typeBox.setEnabled(false);
						typeBox.setFocusable(false);
						typeBox.setVisible(false);
						typeDefTable.getModel().setValueAt("R", 0, step);
						alignCells();
					} else if (item.equals("Y")) {
						typeDefTable.setEnabled(true);
						typeBox.setEnabled(true);
						typeBox.setVisible(true);
						typeBox.setFocusable(true);
						alignCells();
					}

				}
				
			});
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
			xyBoxes.add(comboBox);
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			TableColumn opColumn = typeDefTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("R");
			comboBox.addItem("I");
			comboBox.addItem("B");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
			typeBoxes.add(comboBox);
		}
		for (int i = 0; i < simplexTableau[0].length - 1; i++) {
			TableColumn opColumn = paramNegIndicesTable.getColumnModel().getColumn(i);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem(">= 0");
			comboBox.addItem("<= 0");
			opColumn.setCellEditor(new DefaultCellEditor(comboBox));
		}
		
		// Function table not editable
		functionTable.setEnabled(false);

		// Align cell
		alignCells();
		
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
		//panel_defTable.add(paramNegIndicesTable, c);
	}

	public void alignCells() {
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

	@Override
	public void save(String path) throws Exception {
		try {
			validateInput();
			path = path + Helper.Keyword.PATHBENDERS;
			loadYvariableIndices();
			loadBendersMasterCoefficientTypes();
			loadParamNegIndices();
			BendersSaveClass bs = new BendersSaveClass();
			bs.setNumRestr(numRestr);
			bs.setNumVar(numVar);
			bs.setYVariableIndices(this.yVariables);
			bs.setBendersMasterCoefficientTypes(this.bendersMasterCoefficientType);
			bs.setParamNegIndices(this.parameterNegativeIndices);
			bs.setArray(this.simplexTableau);
			bs.setFunction(this.simplexTableau[this.simplexTableau.length - 1]);
			bs.setMin(this.minProblem);
			bs.setOPs(this.operators);
			LinearOptFileHandler.save(path, bs);
		} catch (Exception exept) {
			throw new Exception("Geben Sie alle notwendigen Daten ein.");
		}

	}

	@Override
	public void setNumRestr(int numRestr) {
		this.numRestr = numRestr;

	}

	@Override
	public int getNumRestr() {
		return this.numRestr;
	}

	@Override
	public void setNumVar(int numVar) {
		this.numVar = numVar;

	}

	@Override
	public int getNumVar() {
		return this.numVar;
	}

	public void createView(BendersSaveClass obj) {
		this.setNumRestr(obj.getNumRestr());
		this.setNumVar(obj.getNumVar());
		this.setSimplexTableau(obj.getArray());
		modifyFunctionTable();
		/*for (int j = 0; j < paramNegIndicesTable.getColumnCount(); j++) {
			paramNegIndicesTable.setValueAt(">= 0", 0, j);
		}
		for (int i = 0; i < obj.getParameterNegativeIndices().length; i++) {
			paramNegIndicesTable.setValueAt("<= 0", 0, obj.getParameterNegativeIndices()[i]);
		}*/

		for (int i = 0; i < variableDefTable.getColumnCount(); i++) {
			variableDefTable.setValueAt("X", 0, i);
		}

		for (int i = 0; i < obj.getyVariables().length; i++) {
			variableDefTable.setValueAt("Y", 0, obj.getyVariables()[i]);
		}
		for (int i = 0; i < typeDefTable.getColumnCount(); i++) {
			typeDefTable.setValueAt("", 0, i);
		}

		for (int i = 0; i < obj.getyVariables().length; i++) {
			BendersMasterCoefficientType type = obj.getBendersMasterCoefficientType()[i];
			switch (type) {
			case Float:
				typeDefTable.setValueAt("R", 0, obj.getyVariables()[i]);
				break;
			case Integer:
				typeDefTable.setValueAt("I", 0, obj.getyVariables()[i]);
				break;
			case Binary:
				typeDefTable.setValueAt("B", 0, obj.getyVariables()[i]);
				break;
			}
		}
		paramNegIndicesTable.revalidate();
		typeDefTable.revalidate();
		variableDefTable.revalidate();
		paramNegIndicesTable.repaint();
		typeDefTable.repaint();
		variableDefTable.repaint();

	}

	public void setMinProblem(boolean selected) {
		this.minProblem = selected;

	}

	public void setOPs(String[] ops) {
		this.operators = ops;
	}

}
