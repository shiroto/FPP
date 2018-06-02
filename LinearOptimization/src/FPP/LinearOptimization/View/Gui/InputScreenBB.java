package FPP.LinearOptimization.View.Gui;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;



public class InputScreenBB extends JPanel implements InputScreenIF {

	private JRadioButton[] binaerRadio;
	private JRadioButton[] arrayRadioGanzzahl;
	private final int YPOSRADIOBUTTON = 65;
	private JLabel problemBeschreibungPanel;
	private JCheckBox jCBMin;
	private int numRestr, numVar;
	private Double[][] simplexTableau;
	private boolean max;
	private JTable setFunctionTable;

	public InputScreenBB(MainFrame mainFrame) {
		// TODO Auto-generated constructor stub
		
	
	}
	
	public void initializeScreen() {
		this.numRestr = Integer.valueOf(this.simplexTableau.length);
		this.numVar = Integer.valueOf(this.simplexTableau[0].length-1);
		problemBeschreibungPanel = new JLabel();
		problemBeschreibungPanel.setVisible(false);
		this.add(problemBeschreibungPanel);
		
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
		
	}

	@Override
	public void setSimplexTableau(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;

	}

	@Override
	public void setFunctionTable(JTable functionTable) {
		this.setFunctionTable = functionTable;

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
			problemBeschreibungPanel.setVisible(false); // bei erneutem Aufruf wird zuerst altes Label gel�scht
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
				ausgabe += " , bin�r<br>";
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
		for(JRadioButton jr : this.binaerRadio) {
			jr.setVisible(false);
		}
	}
	
	private void setMinMaxProblem() {
		if (this.jCBMin.isSelected()) {
			max = false;
		} else {
			max = true;
		}
		
	}
	private boolean leseMinMaxProblem() {
		if (this.jCBMin.isSelected()) {
			return false;
		} else {
			return true;
		}
		
	}

}
