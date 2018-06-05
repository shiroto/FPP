package FPP.LinearOptimization.Model.BranchAndBound;

import java.util.*;
//Version Felix

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten.Auslotungsergebnis;

/**
 * Die BranchAndBound Klasse stellt den allgemeinen Algorithmusablauf dar. Sie implementiert unter anderem die solve Funktion mit der 
 * gemischt ganzzahlige Probleme gelöst werden können.
 * Sie erhält im Konstruktor ein 2dimensionales
 * Doublearray, welches das Ausgangsproblem darstellt. Der zweite Parameter gibt
 * an ob maximiert oder minimiert werden soll: Bei max. TRUE, bei min. FALSE
 * <br>
 * Die Funktion solve liefert eine eine Arraylist<Double[]>. Die jeweiligen
 * Elemente der Array List sind alle optimalen Lösungen. <br>
 * Wird beispielsweise das Array [1.0, 3.0 ,0, 1777.87] zurückgegeben, bedeutet
 * das, dass x1=1, x2=3, x3=0 die Entscheidungsvariablen sind und der optimale
 * Zielfunktionswert 1777.87 ist. In diesem Fall gibt es nur eine optimale
 * Lösung. Möglich ist auch, dass mehrere Double Arrays zurückgegeben werden,
 * wenn mehrere Lösungen exitieren.
 */
public class BranchAndBound {

	private Deque<BranchAndBoundKnoten> queue; // enthält alle unausgeloteten Teilprobleme im 
	private List<BranchAndBoundKnoten> mubQueue; //Liste die im Maximum Upper Bound Verfahren benoetigt wird
	private List<BranchAndBoundKnoten> queueOld = new ArrayList<>();
	private double untereSchranke; // globale untere Schranke
	private double[] gesamtloesung; // enthält erste Loesung
	private List<double[]> loesungsListe; // enthält alternative Loesungen, nach
											// Ablauf der solve Funktion alle
											// Loesungen
	private boolean solved = false; // Gibt an ob BranchAndBound Problem geloest
									// wurde
	private boolean[] flagInteger; // Gibt an welche der Entscheidungsvariablen
									// ganzzahlig sein muessen, true wenn
									// ganzzahlig, false wenn reellwertig
	private boolean max;			//TRUE wenn maximierung vorliegt
	

	private int tableauCounter; // Anzahl der im Branch and Bound berechneten
								// Tableaus -1
	private final BranchAndBoundKnoten startKnoten;



	public BranchAndBound(SimplexTableau a, boolean max, boolean[] flagInteger) {
		this.setTableauCounter(0);
		this.flagInteger = flagInteger;
		this.max = max;
		this.queue = new ArrayDeque<>();
		startKnoten = new BranchAndBoundKnoten(new SimplexProblem(a, this.max),this, 0);
		this.queue.add(startKnoten); // Starttableau wird Liste abzuarbeitender Probleme
															// hinzugefuegt
		this.mubQueue = new ArrayList<>();
		this.mubQueue.add(startKnoten);
		this.gesamtloesung = new double[a.getQ() + 1];
		Arrays.fill(gesamtloesung, Double.NaN);
		if(max) {
		this.untereSchranke = -Double.MAX_VALUE;
		}else {
			this.untereSchranke = Double.MAX_VALUE;
		}
		loesungsListe = new ArrayList<>();

	}

	/**
	 * Funktion löst die BranchAndBound Aufgabenstellung nach dem Maximum Upper Bound Prinzip. Vorraussetzung ist,
	 * dass ein zweidimensionales double Array dem Konstruktor uebergeben wurde.
	 * Die ArrayList vom Typ double[] mit den Loesungen des ganzzahligen
	 * Problems wird zurueckgegeben.
	 */
	
	public List<double[]> solveMaximumUpperBound() {
		if(!solved) {
			BranchAndBoundKnoten start = mubQueue.get(0);
			//BranchAndBoundKnoten start = mubQueue.remove(0);
		//	start.getProblem().printAll();
			start.getProblem().loeseAllgemein();
			Auslotungsergebnis erg1 = start.auslotung(untereSchranke, flagInteger, tableauCounter);
			switch(erg1) {
			case SCHLECHTERE_LOESUNG:
				queueOld.add(start);
				mubQueue.remove(0); //new
				break;
			case BESSERE_LOESUNG:
				untereSchranke = start.getProblem().getZFWert(); // Untere Schranke des Branch and Bound Problems wird aktualisiert
				gesamtloesung = start.getProblem().getLoesung(); // Lösung des BranchAndBound Problems wird gespeichert
				queueOld.add(start);
				mubQueue.remove(0); //new
				break;
			case GLEICHWERTIGE_LOESUNG:
				loesungsListe.add(start.getProblem().getLoesung());
				queueOld.add(start);
				mubQueue.remove(0); //new
				break;
			case VERZWEIGUNG_NOETIG:
				//mubQueue.add(start);  alt, jetzt nichts tun
				break;
			}
			
			
			
			while(!mubQueue.isEmpty()) {
				BranchAndBoundKnoten knoten;
				if(max) {
				 knoten = mubQueue.remove(getKnotenMitMaxZf());
				}else {
					knoten = mubQueue.remove(getKnotenMitMinZf());
				}
				queueOld.add(knoten);
				//knoten.getProblem().printAll();
				//knoten.getProblem().loeseAllgemein();
				Auslotungsergebnis erg2 = knoten.auslotung(untereSchranke, flagInteger, tableauCounter);
				switch(erg2) {
				case SCHLECHTERE_LOESUNG:
					// nichts zu tun
					break;
				case BESSERE_LOESUNG:
					untereSchranke = knoten.getProblem().getZFWert(); // Untere Schranke des Branch and Bound Problems wird aktualisiert
					gesamtloesung = knoten.getProblem().getLoesung(); // Lösung des BranchAndBound Problems wird gespeichert
					
					break;
				case GLEICHWERTIGE_LOESUNG:
					loesungsListe.add(knoten.getProblem().getLoesung());
					break;
				case VERZWEIGUNG_NOETIG:
					mubQueue.add(knoten.getLinks());
					knoten.getLinks().getProblem().loeseAllgemein();
					//knoten.getLinks().getProblem().printAll();
					mubQueue.add(knoten.getRechts());
					knoten.getRechts().getProblem().loeseAllgemein();
					//knoten.getRechts().getProblem().printAll();
					tableauCounter += 2;
					break;
				}
			}
			loesungsListe.add(gesamtloesung);
			manipulateErrors();
			flagTableausWithBest();
			this.solved = true;
			
		}
		return this.loesungsListe;
	}
	
	
	@Deprecated
	public List<double[]> solve() {
		if (!solved) {
			while (!queue.isEmpty()) {
				BranchAndBoundKnoten knoten = queue.removeLast();
				//knoten.getProblem().printAll();
				knoten.getProblem().loeseAllgemein();
				Auslotungsergebnis ergebnis = knoten.auslotung(untereSchranke, flagInteger, tableauCounter);
				switch (ergebnis) {
				case SCHLECHTERE_LOESUNG:
					// nichts zu tun
					break;
				case BESSERE_LOESUNG:
					untereSchranke = knoten.getProblem().getZFWert(); // Untere Schranke des Branch and Bound Problems wird aktualisiert
					gesamtloesung = knoten.getProblem().getLoesung(); // Lösung des BranchAndBound Problems wird gespeichert
					
					break;
				case GLEICHWERTIGE_LOESUNG:
					loesungsListe.add(knoten.getProblem().getLoesung());
					break;
				case VERZWEIGUNG_NOETIG:
					queue.addLast(knoten.getLinks());
					queue.addLast(knoten.getRechts());
					tableauCounter += 2;
					break;
				}
				queueOld.add(knoten);
//				System.out.println(" NUMMER: "+ knoten.getNummer());
//				System.out.println("******");
			}
			loesungsListe.add(gesamtloesung);
			manipulateErrors();
			flagTableausWithBest();
			this.solved = true;
		}
		return this.loesungsListe;

	}

	private int getKnotenMitMaxZf() {
		int size = this.mubQueue.size();
		int  maxIndex =0;
		double zfMax = mubQueue.get(0).getProblem().getZFWert();
		for ( int i = 0 ; i<size; i++) {
			double tempZfWert = mubQueue.get(i).getProblem().getZFWert();
			if( tempZfWert > zfMax) {
				maxIndex = i;
				zfMax= tempZfWert;
			}
		}
		return maxIndex;
	}
	private int getKnotenMitMinZf() {
		int size = this.mubQueue.size();
		int  minIndex =0;
		double zfMin = mubQueue.get(0).getProblem().getZFWert();
		for ( int i = 0 ; i<size; i++) {
			double tempZfWert = mubQueue.get(i).getProblem().getZFWert();
			if( tempZfWert < zfMin) {
				minIndex = i;
				zfMin= tempZfWert;
			}
		}
		return minIndex;
	}
	

	/**
	 * Rundet die ganzzahligen Entscheidungsvariablen, die innerhalb des
	 * Toleranzwertes nicht ganzzahlig im Lösungsarray stehen auf einen
	 * ganzzahligen Wert.
	 */
	private void manipulateErrors() {
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

	private void flagTableausWithBest() {
		for (BranchAndBoundKnoten t : this.queueOld) {
			double tempGesamtLoesung = gesamtloesung[gesamtloesung.length - 1];
			double tempTableauloesung = t.getProblem().getLoesung()[t.getProblem().getLoesung().length - 1];
			if (!t.isVerzweigt() && tempGesamtLoesung == tempTableauloesung) {
				t.setBesteLoesung(true);
			}
		}
	}

	public Double getUntereSchranke() {
		return untereSchranke;
	}

	public List<double[]> getLoesungsListe() {
		return loesungsListe;
	}

	public int getTableauCounter() {
		return tableauCounter;
	}

	public void setTableauCounter(int tableauCounter) {
		this.tableauCounter = tableauCounter;
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

	public Deque<BranchAndBoundKnoten> getQueue() {
		return queue;
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
