package FPP.LinearOptimization.View.Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import FPP.LinearOptimization.Model.BranchAndBound.*;
import FPP.LinearOptimization.View.Gui.*;
import FPP.LinearOptimization.View.Save.*;
import FPP.LinearOptimization.Model.BranchAndBound.*;

public class InputScreenBB extends JPanel implements InputScreenIF {

	public static int KOMMASTELLEN = 2;
	private JRadioButton[] binaerRadio;
	private JRadioButton[] arrayRadioGanzzahl;
	private final int YPOSRADIOBUTTON = 65;
	private JLabel problemBeschreibungPanel;
	private JCheckBox jCBMin;
	private int numRestr, numVar;
	private Double[][] simplexTableau;
	private boolean max = false;
	private MainFrame mainFrame;
	private JTable restrictionTable;
	private JScrollPane scrollPaneRestrictions;
	private JScrollPane scrollPaneFunction;
	private JButton btnSubmit;
	private BranchAndBound branchAndBoundProblem;
	private boolean[] binaerListe;
	private double[][] array;
	private boolean[] ganzzahlFlag;
	private boolean binaer;
	private double[] zielfunktion;
	private JTable functionTable;
	private LoesungsPanel loesungsPanel;
	private boolean minProblem;
	private String[] operators;

	public InputScreenBB(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		loesungsPanel = new LoesungsPanel(mainFrame);

	}

	/**
	 * Initializing Branch and Bound Inputscreen.
	 */
	public void initializeScreen() {
		problemBeschreibungPanel = new JLabel();
		problemBeschreibungPanel.setVisible(false);
		this.add(problemBeschreibungPanel);

		loadTables();
		jCBMin = new JCheckBox();
		jCBMin.setSelected(true);
		jCBMin.setVisible(false);
		jCBMin.setText("Minimierungsproblem");
		this.add(jCBMin);
		jCBMin.setBounds(760, 25, 160, 17);
		initiateRadioButtonListe(numVar);
		jCBMin.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					initiateProlembeschreibungsPanel(numVar);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {

					initiateProlembeschreibungsPanel(numVar);
				}
			}
		});

		initiiereProblemViewFelder();

		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(1033, 527, 171, 41);
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validateInput();
				branchAndBoundProblem = erstelleProblem();

				mainFrame.getTabs().addTab(Helper.Keyword.SOLUTIONBANDB, loesungsPanel);
				mainFrame.getTabs().setSelectedIndex(mainFrame.getTabs().indexOfTab(Helper.Keyword.SOLUTIONBANDB));
				new Thread(() -> branchAndBoundProblem.solveMaximumUpperBound(loesungsPanel), "AlgorithmusThread")
						.start();
			}

		});
		this.add(btnSubmit);

	}


	protected void validateInput() {
		// TODO Auto-generated method stub

	}

	
	private void loadTables() {
		Helper.alignCells(functionTable);
		Helper.alignCells(restrictionTable);
		JPanel problemPanel = new JPanel();
		problemPanel.setBounds(50, 210, 250, 20);

		JLabel problemLabel = new JLabel("Restriktionen");
		problemPanel.add(problemLabel);

		scrollPaneRestrictions = new JScrollPane(restrictionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneRestrictions.setBounds(150, 250, 523, 325);

		restrictionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JPanel functionPanel = new JPanel();
		functionPanel.setBounds(50, 100, 250, 20);

		JLabel functionLabel = new JLabel("Zielfunktion");
		functionPanel.add(functionLabel);

		scrollPaneFunction = new JScrollPane(functionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneFunction.setBounds(150, 128, 523, 60);

		functionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPaneFunction.setBorder(null);
		scrollPaneFunction.setVisible(true);
		scrollPaneRestrictions.setVisible(true);
		this.add(problemPanel);
		this.add(scrollPaneRestrictions);
		this.add(functionPanel);
		this.add(scrollPaneFunction);
		functionPanel.setVisible(true);
		problemPanel.setVisible(true);

	}

	@Override
	public void setSimplexTableau(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;

	}

	@Override
	public void setFunctionTable(JTable functionTable) {
		functionTable.setEnabled(false);
		this.functionTable = functionTable;

	}

	private void initiateRadioButtonListe(int numVar) {
		this.binaerRadio = new JRadioButton[numVar];
		this.arrayRadioGanzzahl = new JRadioButton[numVar];

		for (int i = 0; i < numVar; i++) {
			JRadioButton ganzzahlButton = new JRadioButton();
			ganzzahlButton.setSelected(true);
			ganzzahlButton.setVisible(true);
			ganzzahlButton.setBounds(740, YPOSRADIOBUTTON + (i * 18), 120, 18);
			String tempText = "x" + (i + 1) + " ganzzahlig";
			ganzzahlButton.setText(tempText);
			this.add(ganzzahlButton);
			this.arrayRadioGanzzahl[i] = ganzzahlButton;

			JRadioButton binaerButton = new JRadioButton();

			binaerButton.setSelected(false);
			binaerButton.setBounds(870, YPOSRADIOBUTTON + (i * 18), 120, 18);
			binaerButton.setText("x" + (i + 1) + " binaer");
			binaerButton.setVisible(true);
			this.add(binaerButton);
			binaerRadio[i] = binaerButton;
			binaerButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						ganzzahlButton.setEnabled(false);
						ganzzahlButton.setSelected(true);
						problemBeschreibungPanel.setVisible(false);
						initiateProlembeschreibungsPanel(numVar);

					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						ganzzahlButton.setEnabled(true);
						problemBeschreibungPanel.setVisible(false);
						initiateProlembeschreibungsPanel(numVar);

					}
				}
			});

			ganzzahlButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						binaerButton.setEnabled(true);
						binaerButton.setSelected(false);
						problemBeschreibungPanel.setVisible(false);
						initiateProlembeschreibungsPanel(numVar);

					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						binaerButton.setEnabled(false);
						binaerButton.setSelected(false);
						problemBeschreibungPanel.setVisible(false);
						initiateProlembeschreibungsPanel(numVar);

					}
				}
			});

		}
	}

	private void initiateProlembeschreibungsPanel(int numVar) {
		if (problemBeschreibungPanel != null)
			problemBeschreibungPanel.setVisible(false); // bei erneutem Aufruf wird zuerst altes Label gelöscht
		int yPos = this.YPOSRADIOBUTTON + numVar * 18;

		problemBeschreibungPanel.setBounds(740, yPos, 200, numVar * 26 + 26);

		String ausgabe = "<html>";
		if (!this.jCBMin.isSelected())
			ausgabe += "Maximierungsproblem<br> ";
		else
			ausgabe += "Minimierungsproblem<br>";
		for (int i = 0; i < numVar; i++) {
			ausgabe += "x" + (i + 1) + " ";
			if (this.arrayRadioGanzzahl[i].isSelected())
				ausgabe += "ganzzahlig";
			else
				ausgabe += "reellwertig";

			if (this.binaerRadio[i].isSelected())
				ausgabe += " , binär<br>";
			else
				ausgabe += "<br>";
		}
		ausgabe += "</html";
		problemBeschreibungPanel.setFont(new Font("Courier New", Font.ITALIC, 14));
		problemBeschreibungPanel.setText(ausgabe);
		this.problemBeschreibungPanel.setVisible(true);
		problemBeschreibungPanel.setVisible(true);
		this.repaint();
		this.revalidate();
	}

	private void initiiereProblemViewFelder() {
		this.jCBMin.setVisible(true);
		this.repaint();
	}

	private void setRadioButtonListsInvisible() {
		for (JRadioButton jr : this.arrayRadioGanzzahl) {
			jr.setVisible(false);
		}
		for (JRadioButton jr : this.binaerRadio) {
			jr.setVisible(false);
		}
	}

	private boolean leseMinMaxProblem() {
		if (this.jCBMin.isSelected()) {
			return false;
		} else {
			return true;
		}

	}

	public void setRestrictionTable(JTable restrictionTable) {
		restrictionTable.setEnabled(false);
		this.restrictionTable = restrictionTable;

	}

	private BranchAndBound erstelleProblem() {
		
		// meinTableModel.fireTableDataChanged();
		// tabelle.setModel(meinTableModel);

		setBinProblem();
		schreibeGanzzahlFlag();

		if (!binaer) {
			Arrays.fill(this.binaerListe, true);
			createArray();
		} else {
			schreibeBinaerListe();
			createArrayBinaerProblem();
		}

		setMinMaxProblem();
		branchAndBoundProblem = new BranchAndBound(new SimplexTableau(simplexTableau), max, ganzzahlFlag);

		return branchAndBoundProblem;
	}

	private void schreibeBinaerListe() {

		for (int i = 0; i < numVar; i++) {
			if (this.binaerRadio[i].isSelected())
				binaerListe[i] = true;
			else
				binaerListe[i] = false;
		}
	}

	private boolean[] leseBinaerListe() {
		boolean[] binTemp = new boolean[numVar];
		for (int i = 0; i < numVar; i++) {
			if (this.binaerRadio[i].isSelected())
				binTemp[i] = true;
			else
				binTemp[i] = false;
		}
		return binTemp;
	}

	private void setBinProblem() {
		this.binaerListe = new boolean[numVar];
		boolean isBin = false;
		for (int i = 0; i < numVar; i++) {
			if (this.binaerRadio[i].isSelected())
				isBin = true;
		}
		this.binaer = isBin;

	}

	private void setMinMaxProblem() {
		if (this.jCBMin.isSelected()) {
			max = false;
		} else {
			max = true;
		}

	}

	private void zeigeLoesung(List<double[]> loesung) {
		String ausgabe = "<html> Lösung <br>";
		for (int i = 0; i < loesung.size(); i++) {
			double[] temp = loesung.get(i);
			String zeileTemp = "";
			for (int e = 0; e < temp.length; e++) {
				if (e != temp.length - 1)
					zeileTemp = zeileTemp + "x" + (e + 1) + ": " + temp[e] + " ";
				else
					zeileTemp = zeileTemp + "Zielfunktion" + ": " + temp[e] + " ";
			}
			ausgabe = ausgabe + zeileTemp + "<br>";
		}
		ausgabe = ausgabe + "</html>";
		// this.jLResult.setText(ausgabe);
		// this.jLResult.setVisible(true);
	}

	private boolean[] schreibeGanzzahlFlag() {
		this.ganzzahlFlag = new boolean[numVar];

		for (int i = 0; i < numVar; i++) {
			if (arrayRadioGanzzahl[i].isSelected()) {
				this.ganzzahlFlag[i] = true;
			} else {
				this.ganzzahlFlag[i] = false;
			}
		}
		return this.ganzzahlFlag;
	}

	private boolean[] leseGanzzahlFlag() {
		boolean[] ganzzahlFlagTemp = new boolean[numVar];
		for (int i = 0; i < numVar; i++) {
			if (arrayRadioGanzzahl[i].isSelected()) {
				ganzzahlFlagTemp[i] = true;
			} else {
				ganzzahlFlagTemp[i] = false;
			}
		}
		return ganzzahlFlagTemp;
	}

	private void createArray() {
		this.array = new double[this.numRestr + 1][this.numVar + 1]; // endgueltiges
																		// Array

		schreibeZielfunktion();

		if (!this.jCBMin.isSelected()) {
			multiplieziereZf();
		}

		setRestriktionsarray();

		// fuege ZF array hinzu
		for (int i = 0; i < numVar + 1; i++) {
			double wert = this.zielfunktion[i];
			array[numRestr][i] = wert;
		}

	}

	private void multiplieziereZf() {
		for (int i = 0; i < numVar; i++) {
			this.zielfunktion[i] = this.zielfunktion[i] * -1;
		}
	}

	private void setRestriktionsarray() {

		int numColumns = restrictionTable.getModel().getColumnCount(); // mit
		// Restriktionen
		int failIndex = numColumns - 2;
		for (int i = 0; i < numRestr; i++) { // erstelle Restriktionen
			String opTemp = restrictionTable.getModel().getValueAt(i, failIndex).toString();
			for (int e = 0; e < numVar + 1; e++) {
				if (e != numVar) {
					if (opTemp.equals("<=")) {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString());
					} else {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString()) * -1;
					}
				} else {
					if (opTemp.equals("<=")) {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString());
					} else {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString())
								* -1;
					}
				}
			}

		}
	}

	/**
	 * Liest Zielfunktion aus Tablle und speichert diese Im Panel.
	 * 
	 * @return Zielfunktions vom Typ double[numVar+1]
	 * @throws Zielfunktionsexception
	 */
	private double[] schreibeZielfunktion() {
		this.zielfunktion = new double[numVar + 1];
		for (int i = 0; i < numVar + 1; i++) {
			if (i != numVar) {
				this.zielfunktion[i] = Double.valueOf(this.functionTable.getModel().getValueAt(0, i).toString());
			} else {
				this.zielfunktion[i] = 0d;
			}
		}
		return this.zielfunktion;
	}

	private void createArrayBinaerProblem() {

		int anzahlBin = getAnzahlBin();
		int anzahlZeilen = this.numRestr + 1 + anzahlBin * 2;
		this.array = new double[anzahlZeilen][this.numVar + 1];
		schreibeZielfunktion();
		if (!this.jCBMin.isSelected()) {
			multiplieziereZf();
		}
		setRestriktionsArrayBinaer();
		// fuege ZF array hinzu
		for (int i = 0; i < numVar + 1; i++) {
			Double wert = this.zielfunktion[i];
			array[numRestr + 2 * anzahlBin][i] = wert;
		}

	}

	private int getAnzahlBin() {
		int z = 0;
		for (int i = 0; i < this.binaerListe.length; i++) {
			if (this.binaerListe[i])
				z++;
		}
		return z;
	}

	private void setRestriktionsArrayBinaer() {
		int numColumns = restrictionTable.getModel().getColumnCount(); // mit
		// Restriktionen
		int failIndex = numColumns - 2; // gibt index an, an dem Operator steht
		for (int i = 0; i < numRestr; i++) { // erstelle Restriktionen
			String opTemp = restrictionTable.getModel().getValueAt(i, failIndex).toString();
			for (int e = 0; e < numVar + 1; e++) {
				if (e != numVar) {
					if (opTemp.equals("<=")) {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString());
					} else {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString()) * -1;
					}
				} else {
					if (opTemp.equals("<=")) {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString());
					} else {
						this.array[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString())
								* -1;
					}
				}
			}

		}
		// erstelle binaerrestriktionen
		int zeile = numRestr;
		for (int i = 0; i < numVar; i++) {
			if (binaerListe[i]) {
				for (int e = 0; e < numVar + 1; e++) {
					if (e == i || e == numVar)
						array[zeile][e] = 1;
					else
						array[zeile][e] = 0;
				}
				zeile++;
				for (int e = 0; e < numVar + 1; e++) {
					if (e == i)
						array[zeile][e] = -1;
					else
						array[zeile][e] = 0;
				}
				zeile++;

			}
		}
	}

	public static DecimalFormat getDecimalFormat() {
		String dec = "0.";
		for (int i = 0; i < InputScreenBB.KOMMASTELLEN; i++) {
			dec = dec + "#";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH); // Decimal Seperator auf . setzen
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern(dec);

		return df;
	}

	@Override
	public void save(String path) throws Exception {
		path = path + Helper.Keyword.PATHBANDB;
		if ((numRestr >= 1) && (numVar > 1)) {
			BranchAndBoundSpeicherKlasse sp = new BranchAndBoundSpeicherKlasse();
			sp.setNumRestr(numRestr);
			sp.setNumVar(numVar);
			sp.setFunction(leseZielfunktion());
			sp.setMax(leseMinMaxProblem());
			sp.setBin(leseBinaerListe());
			sp.setGanzzahl(leseGanzzahlFlag());
			sp.setArray(this.simplexTableau);
			sp.setMin(minProblem);
			sp.setOPs(operators);
			LinearOptFileHandler.save(path, sp);
		} else {

		}

	}

	private Double[] leseZielfunktion() {
		Double[] zf = new Double[numVar];

		for (int i = 0; i < numVar; i++) {
			zf[i] = Double.valueOf(this.functionTable.getModel().getValueAt(0, i).toString());
		}
		return zf;

	}

	private Double[][] leseRestkriktionsArray() {
		Double[][] restriktionen = new Double[numRestr][numVar + 1];
		int numColumns = restrictionTable.getModel().getColumnCount(); // mit
		// Restriktionen
		int failIndex = numColumns - 2;
		for (int i = 0; i < numRestr; i++) { // erstelle Restriktionen
			String opTemp = restrictionTable.getModel().getValueAt(i, failIndex).toString();
			for (int e = 0; e < numVar + 1; e++) {
				if (e != numVar) {
					if (opTemp.equals("<=")) {
						restriktionen[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString());
					} else {
						restriktionen[i][e] = Double.valueOf(restrictionTable.getModel().getValueAt(i, e).toString())
								* -1;
					}
				} else {
					if (opTemp.equals("<=")) {
						restriktionen[i][e] = Double
								.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString());
					} else {
						restriktionen[i][e] = Double
								.valueOf(restrictionTable.getModel().getValueAt(i, e + 1).toString()) * -1;
					}
				}
			}

		}
		return restriktionen;
	}

	public void erhalteBandBundErstelleView(BranchAndBoundSpeicherKlasse bAndB) {
		if (this.arrayRadioGanzzahl != null && this.binaerRadio != null) { // referenzen auf bestehende probleme
			// loeschen
			setRadioButtonListsInvisible();
		}
		this.binaerRadio = null;
		this.ganzzahlFlag = null;
		this.binaerListe = null;
		this.arrayRadioGanzzahl = null;
		this.repaint();
		this.revalidate();
		erstelleView(bAndB.getNumRestr(), bAndB.getNumVar());
		fuelleMax(bAndB);
		fuelleGanzzahlButtons(bAndB);
		fuelleBinaerButtons(bAndB);

	}

	private void fuelleBinaerButtons(BranchAndBoundSpeicherKlasse bAndB) {
		boolean[] bin = bAndB.getBin();
		for (int i = 0; i < bAndB.getNumVar(); i++) {
			if (bin[i])
				binaerRadio[i].setSelected(true);
			else
				binaerRadio[i].setSelected(false);
		}
		this.repaint();

	}

	private void fuelleGanzzahlButtons(BranchAndBoundSpeicherKlasse bAndB) {
		boolean[] flag = bAndB.getGanzzahl();
		for (int i = 0; i < flag.length; i++) {
			if (flag[i]) {
				this.arrayRadioGanzzahl[i].setSelected(true);
			} else {
				this.arrayRadioGanzzahl[i].setSelected(false);

			}
		}
		this.repaint();

	}

	private void fuelleMax(BranchAndBoundSpeicherKlasse bAndB) {
		boolean max = bAndB.isMax();
		if (max) {
			this.jCBMin.setSelected(false);
		} else {
			this.jCBMin.setSelected(true);
		}

	}

	private void erstelleView(int numRestrIn, int numVarIn) {
		this.numRestr = numRestrIn;
		this.numVar = numVarIn;
		initiiereProblemViewFelder();
		// createAndActivateZfTable(numVarIn);
		initiateRadioButtonListe(numVarIn);
		initiateProlembeschreibungsPanel(numVarIn);
		// createRestrictionTable(numRestrIn, numVarIn);

	}

	@Override
	public void setNumRestr(int numRest) {
		this.numRestr = numRest;

	}

	@Override
	public int getNumRestr() {
		return numRestr;
	}

	@Override
	public void setNumVar(int numVar) {
		this.numVar = numVar;

	}

	@Override
	public int getNumVar() {
		return numVar;
	}

	public void setMinProblem(boolean selected) {
		this.minProblem = selected;
		
	}

	public void setOPs(String[] ops) {
		this.operators = ops;
		
	}

}

/*
 * Bsp Input: {3,2,6}, {5,2,8}, {-2,-1, 0}
 * 
 */
