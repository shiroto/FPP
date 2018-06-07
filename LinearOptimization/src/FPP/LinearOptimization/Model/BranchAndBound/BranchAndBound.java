package FPP.LinearOptimization.Model.BranchAndBound;

import java.util.*;

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten.Auslotungsergebnis;


/**
 * Die BranchAndBound Klasse stellt den allgemeinen Algorithmusablauf dar. Sie
 * implementiert unter anderem die solveMaximumUpperBound Funktion mit der
 * gemischt ganzzahlige Probleme gelöst werden können. Sie erhält im Konstruktor
 * ein SimplexTableau, welches das Ausgangsproblem darstellt. Der zweite
 * Parameter gibt an ob maximiert oder minimiert werden soll: Bei max. TRUE, bei
 * min. FALSE . Das boolean Array im Konstruktor gibt an, welche Variablen der
 * Ganzzahligkeitsbedingung unterliegen. <br>
 * 
 */
public class BranchAndBound {

	private List<BranchAndBoundKnoten> mubQueue; // Liste enthaelt alle weder ausgeloteten noch verzweigten Knoten
	private List<BranchAndBoundKnoten> queueOld = new ArrayList<>();
	private double globaleSchranke; // globale untere Schranke
	private double[] gesamtloesung; // enthält erste Loesung
	private List<double[]> loesungsListe; // enthält alternative Loesungen, nach
											// Ablauf der solve Funktion alle
											// Loesungen
	private boolean solved = false; // Gibt an ob BranchAndBound Problem geloest
									// wurde
	private boolean[] flagInteger; // Gibt an welche der Entscheidungsvariablen
									// ganzzahlig sein muessen, true wenn
									// ganzzahlig, false wenn reellwertig
	private boolean max; // TRUE wenn maximierung vorliegt

	private int knotenCounter; // Anzahl der im Branch and Bound berechneten
								// Knoten -1
	private final BranchAndBoundKnoten startKnoten;

	public BranchAndBound(SimplexTableau a, boolean max, boolean[] flagInteger) {
		this.setTableauCounter(0);
		this.flagInteger = flagInteger;
		this.max = max;
		// this.queue = new ArrayDeque<>();
		startKnoten = new BranchAndBoundKnoten(new SimplexProblem(a, this.max), this, 0);
		// this.queue.add(startKnoten); // Starttableau wird Liste abzuarbeitender
		// Probleme
		// hinzugefuegt
		this.mubQueue = new ArrayList<>();
		this.mubQueue.add(startKnoten);
		this.gesamtloesung = new double[a.getQ() + 1];
		Arrays.fill(gesamtloesung, Double.NaN);
		if (max) {
			this.globaleSchranke = -Double.MAX_VALUE;
		} else {
			this.globaleSchranke = Double.MAX_VALUE;
		}
		loesungsListe = new ArrayList<>();

	}

	/**
	 * Funktion löst die BranchAndBound Aufgabenstellung nach dem Maximum Upper
	 * Bound Prinzip. Die ArrayList vom Typ double[] mit den Loesungen des
	 * ganzzahligen Problems wird zurueckgegeben. Die jeweiligen Elemente der Array
	 * List stellen die optimalen Lösungen dar. <br>
	 * Wird beispielsweise das Array [1.0, 3.0 ,0, 1777.87] zurückgegeben, bedeutet
	 * das, dass x1=1, x2=3, x3=0 die Entscheidungsvariablen sind und der optimale
	 * Zielfunktionswert 1777.87 ist. In diesem Fall gibt es nur eine optimale
	 * Lösung. Möglich ist auch, dass mehrere double Arrays zurückgegeben werden,
	 * wenn mehrere Lösungen exitieren.
	 */

	public List<double[]> solveMaximumUpperBound(BranchAndBoundSteuerung steuerung) {
		steuerung.algorithmusBeginnt();
		if (!solved) {
			BranchAndBoundKnoten startKnoten = mubQueue.get(0);
			startKnoten.getProblem().loeseAllgemein();
			Auslotungsergebnis erg1 = startKnoten.auslotung(globaleSchranke, flagInteger, knotenCounter);
			switch (erg1.getTyp()) {
			case SCHLECHTERE_LOESUNG:
				queueOld.add(startKnoten);
				mubQueue.remove(0);
				break;
			case BESSERE_LOESUNG:
				globaleSchranke = startKnoten.getProblem().getZFWert(); // Untere Schranke des Branch and Bound Problems
																		// wird aktualisiert
				gesamtloesung = startKnoten.getProblem().getLoesung(); // Lösung des BranchAndBound Problems wird
																		// gespeichert
				queueOld.add(startKnoten);
				mubQueue.remove(0);
				break;
			case GLEICHWERTIGE_LOESUNG:
				loesungsListe.add(startKnoten.getProblem().getLoesung());
				queueOld.add(startKnoten);
				mubQueue.remove(0);
				break;
			case VERZWEIGUNG_NOETIG:
				while (!mubQueue.isEmpty()) {
					BranchAndBoundKnoten knoten;

					if (max) {
						knoten = mubQueue.remove(getKnotenMitMaxZf());
					} else {
						knoten = mubQueue.remove(getKnotenMitMinZf());
					}
					queueOld.add(knoten);
					steuerung.naechsterSchritt(queueOld, knoten); // hier wird auf Benutzerinteraktion gewartet
					Auslotungsergebnis erg2 = knoten.auslotung(globaleSchranke, flagInteger, knotenCounter);
					switch (erg2.getTyp()) {
					case SCHLECHTERE_LOESUNG:
						// nichts zu tun
						break;
					case BESSERE_LOESUNG:
						globaleSchranke = knoten.getProblem().getZFWert(); // Untere Schranke des Branch and Bound
																			// Problems wird aktualisiert
						gesamtloesung = knoten.getProblem().getLoesung(); // Lösung des BranchAndBound Problems wird
																			// gespeichert

						break;
					case GLEICHWERTIGE_LOESUNG:
						loesungsListe.add(knoten.getProblem().getLoesung());
						break;
					case VERZWEIGUNG_NOETIG:
						BranchAndBoundKnoten links = erg2.getLinks();
						knoten.setLinks(links);
						mubQueue.add(links);
						links.getProblem().loeseAllgemein();
						BranchAndBoundKnoten rechts = erg2.getRechts();
						mubQueue.add(rechts);
						knoten.setRechts(rechts);
						rechts.getProblem().loeseAllgemein();
						steuerung.naechsterSchritt(queueOld, knoten); // verdeutlicht Wahl des nächsten Knotens
						knotenCounter += 2;
						break;
					}
				}
				break;
			}

			loesungsListe.add(gesamtloesung);
			roundErrors();
			flagKnotenAlsBester();
			this.solved = true;
			steuerung.setLoesung(loesungsListe, queueOld);
		}
		return this.loesungsListe;
	}

	private int getKnotenMitMaxZf() {
		int size = this.mubQueue.size();
		int maxIndex = 0;
		double zfMax = -Double.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			double tempZfWert = mubQueue.get(i).getProblem().getZFWert();
			if (tempZfWert > zfMax) {
				maxIndex = i;
				zfMax = tempZfWert;
			}
		}
		return maxIndex;
	}

	private int getKnotenMitMinZf() {
		int size = this.mubQueue.size();
		int minIndex = 0;
		double zfMin = Double.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			double tempZfWert = mubQueue.get(i).getProblem().getZFWert();
			if (tempZfWert < zfMin) {
				minIndex = i;
				zfMin = tempZfWert;
			}
		}
		return minIndex;
	}

	/**
	 * Rundet die ganzzahligen Entscheidungsvariablen, die innerhalb des
	 * Toleranzwertes nicht ganzzahlig im Lösungsarray stehen auf einen ganzzahligen
	 * Wert.
	 */
	private void roundErrors() {
		for (int i = 0; i < loesungsListe.size(); i++) {
			double[] temp = loesungsListe.get(i);
			for (int e = 0; e < temp.length - 1; e++) {
				if (this.flagInteger[e]) {
					temp[e] = Math.round(temp[e]); // round NaN ergibt
													// immer 0
				}
			}
		}
	}

	public void printLoesung() {
		for (int i = 0; i < loesungsListe.size(); i++) {
			System.out.println(Arrays.toString(loesungsListe.get(i)));
		}

	}

	private void flagKnotenAlsBester() {
		for (BranchAndBoundKnoten t : this.queueOld) {
			double tempGesamtLoesung = gesamtloesung[gesamtloesung.length - 1];
			double tempTableauloesung = t.getProblem().getLoesung()[t.getProblem().getLoesung().length - 1];
			if (!t.isVerzweigt() && tempGesamtLoesung == tempTableauloesung) {
				t.setBesteLoesung(true);
			}
		}
	}

	public Double getUntereSchranke() {
		return globaleSchranke;
	}

	public List<double[]> getLoesungsListe() {
		return loesungsListe;
	}

	public int getTableauCounter() {
		return knotenCounter;
	}

	public void setTableauCounter(int tableauCounter) {
		this.knotenCounter = tableauCounter;
	}

	public double[] getGesamtloesung() {
		return gesamtloesung;
	}

	public boolean[] getFlagInteger() {
		return flagInteger;
	}

	public void setFlagInteger(boolean[] flagInteger) {
		this.flagInteger = flagInteger;
	}

	public List<BranchAndBoundKnoten> getQueueOld() {
		return queueOld;
	}

	public boolean isMax() {
		return max;
	}

	public BranchAndBoundKnoten getStartKnoten() {
		return startKnoten;
	}

}
