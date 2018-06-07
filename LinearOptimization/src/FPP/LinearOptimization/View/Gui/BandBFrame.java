package FPP.LinearOptimization.View.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten;
import FPP.LinearOptimization.Model.BranchAndBound.*;
import FPP.LinearOptimization.View.Gui.*;

public class BandBFrame {
	BranchAndBoundKnoten bAndBKnoten; // Knoten, für den die Details angezeigt werden sollen
	JFrame knotenFrame;
	JPanel panel;
	JLabel tableauLabel, labelLoesungsInfo, labelBeschreibung, tableauBeschr, loesungBeschreibung,
			labelBeschreibungsTitel, endTableauBeschr, endTableauLabel;

	final Color hintergrundFarbe = Color.WHITE;

	public BandBFrame(BranchAndBoundKnoten k) {
		this.bAndBKnoten = k;

	}

	public void init() {
		knotenFrame = new JFrame("P" + bAndBKnoten.getNummer());
		knotenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		knotenFrame.setSize(720, 530);
		knotenFrame.setLayout(null);
		knotenFrame.getContentPane().setBackground(hintergrundFarbe);

		panel = new JPanel(new GridLayout(4, 2));
		knotenFrame.add(panel);
		panel.setBounds(5, 2, 700, 500);

		tableauBeschr = new JLabel();
		tableauBeschr.setBackground(hintergrundFarbe);
		tableauBeschr.setOpaque(true);
		tableauBeschr.setText("Initialtableau: ");
		panel.add(tableauBeschr);

		tableauLabel = new JLabel();
		tableauLabel.setBackground(hintergrundFarbe);
		tableauLabel.setOpaque(true);
		String html = bAndBKnoten.getProblem().getInitialTableau().printHTML();
		tableauLabel.setText(html);
		tableauLabel.setFont(new Font("Courier New", Font.ITALIC, 14));
		panel.add(new JScrollPane(tableauLabel));

		endTableauBeschr = new JLabel();
		endTableauBeschr.setBackground(hintergrundFarbe);
		endTableauBeschr.setOpaque(true);
		endTableauBeschr.setText("Endtableau:");
		panel.add(endTableauBeschr);

		endTableauLabel = new JLabel();
		endTableauLabel.setBackground(hintergrundFarbe);
		endTableauLabel.setOpaque(true);
		String endTableauString = bAndBKnoten.getProblem().getErgebnisTableau().printHTML();
		endTableauLabel.setText(endTableauString);
		endTableauLabel.setFont(new Font("Courier New", Font.ITALIC, 14));
		panel.add(new JScrollPane(endTableauLabel));

		loesungBeschreibung = new JLabel();
		loesungBeschreibung.setBackground(hintergrundFarbe);
		loesungBeschreibung.setOpaque(true);
		loesungBeschreibung.setText("Lösung:");
		panel.add(loesungBeschreibung);

		labelLoesungsInfo = new JLabel();
		labelLoesungsInfo.setBackground(hintergrundFarbe);
		labelLoesungsInfo.setOpaque(true);
		labelLoesungsInfo.setFont(new Font("Courier New", Font.ITALIC, 14));
		labelLoesungsInfo.setText(bAndBKnoten.getProblem().printLoesungHTML());

		panel.add(new JScrollPane(labelLoesungsInfo));

		labelBeschreibungsTitel = new JLabel();
		labelBeschreibungsTitel.setBackground(hintergrundFarbe);
		labelBeschreibungsTitel.setOpaque(true);
		labelBeschreibungsTitel.setText("Prozessinformation: ");

		panel.add(labelBeschreibungsTitel);

		labelBeschreibung = new JLabel();
		labelBeschreibung.setBackground(hintergrundFarbe);
		labelBeschreibung.setOpaque(true);
		panel.add(new JScrollPane(labelBeschreibung));

		String text = getBeschreibungsText();
		labelBeschreibung.setText(text);
		labelBeschreibung.setFont(new Font("Courier New", Font.ITALIC, 14));

		panel.repaint();
		knotenFrame.repaint();

		knotenFrame.setVisible(true);
	}

	/**
	 * Fügt dem Frame eine Beschreibung des vollzogenen Algorithmusschritts hinzu.
	 * Der zurückgegebene String enthält die Beschreibung im HTML Format.
	 * 
	 * @return
	 */

	private String getBeschreibungsText() {

		DecimalFormat df = InputScreenBB.getDecimalFormat();
		String text = "<html>";
		if (bAndBKnoten.getAuslotungsErgebnis() != null) {
			switch (bAndBKnoten.getAuslotungsErgebnis().getTyp()) {
			case SCHLECHTERE_LOESUNG:

				if (!bAndBKnoten.getProblem().isLoesbar()) {
					text += "Das Problem ist nicht lösbar, <br> ";
					if (bAndBKnoten.getProblem()
							.getLoesungsInformation() == LoesungsInformation.KEINE_OPTIMALE_LOESUNG) {

						text += "da das zugrundeliegende Simplex <br> Tableau keine optimale Lösung besitzt. <br> Dies "
								+ "tritt ein, wenn beim primalen <br> Austauschschritt im Tableau alle <br> Elemente der Pivotspalte &le; 0 sind";

					} else {
						text += "da das zugrundeliegende Simplex Tableau <br> keine zulässige Lösung besitzt. Dies <br> tritt ein, wenn im dualen <br> Austauschschritt alle "
								+ "Elemente <br> der Pivotzeile  &ge; 0 sind.";

					}

				} else {
					text += "Eine bereits gefundene Teillösung <br> besitzt mit "
							+ df.format(bAndBKnoten.getBessereLoesung())
							+ " einen besseren <br> Lösungswert als die lokale gefundene <br> Lösung "
							+ df.format(bAndBKnoten.getProblem().getLoesung()[bAndBKnoten.getProblem().getQ()]) + ". Es wird deshalb nicht <br> weiter"
									+ " verzweigt.";
				}
				break;

			case BESSERE_LOESUNG:
				text += "Die Lösung ist bezogen auf das <br> Ausgangsproblem P0  zulässig und "
						+ df.format(bAndBKnoten.getProblem().getLoesung()[bAndBKnoten.getProblem().getQ()])
						+ " <br> wird als bester gefundener <br> Zielfunktionswert gespeichert.";
				break;
			case GLEICHWERTIGE_LOESUNG:
				text += "Die gefundene Lösung besitzt mit "
						+ df.format(bAndBKnoten.getProblem().getLoesung()[bAndBKnoten.getProblem().getQ()])
						+ " <br> denselben Zielfunktionswert  wie eine<br> bereits  gefundene Lösung. <br> Die Lösung wurde deshalb der <br> Lösungsliste  hinzugefügt.";
				break;
			case VERZWEIGUNG_NOETIG:

				text += "x" + (bAndBKnoten.getAuslotungsIndex() + 1)
						+ " ist nicht ganzzahlig. <br> Aus diesem Grund werden zwei neue  <br> Teilprobleme mit den Restriktionen <br> x"
						+ (bAndBKnoten.getAuslotungsIndex() + 1) + " &le; " + bAndBKnoten.getAuslotungAbgerundet()
						+ " und x" + (bAndBKnoten.getAuslotungsIndex() + 1) + " &ge; "
						+ bAndBKnoten.getAuslotungAufgerundet() + " erstellt.";

			}
		} else {
			text += "Problem noch nicht ausgelotet.";
		}

		text += "</html>";
		return text;

	}

}
