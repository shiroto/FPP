package FPP.LinearOptimization.Model.BranchAndBound;

import java.text.DecimalFormat;
import java.util.*;

import FPP.LinearOptimization.Model.BranchAndBound.SimplexTableau.FeldId;
import FPP.LinearOptimization.View.Gui.Helper;


//Version Felix
/**
 * Die Tableau Klasse ehält im Konstruktor ein Ausgangsarray, welches die
 * Koeffizientmatrix darstellt.
 * <p>
 * Das Ausgangsarray ist ein zweidimensionales Double array mit m+1 Zeilen. m
 * sei die Anzahl der Restriktionen. In den ersten m Zeilen stehen die
 * Restriktionen, die in der Standardform a1x+a12x... <= b1 vorliegen
 * <p>
 * Sollten im ursprünglichen Problem >= Bedingungen vorliegen, muss diese Zeile
 * durch Multiplikation mit *-1 in die Normalform gebracht werden. In der
 * letzten Zeile steht die Zielfunktion, das letzte Feld der Zeile ist immer 0d.
 * Bei Maximierungsproblemen wird die ursprüngliche Zielfunktion mit *-1
 * multipliziert bei min Problemen kann diese übernommen werden.
 * <p>
 * 
 * <pre>
 *Bsp:
 *maximiere 13x1 + 8x2
 *Subject to:
 *x1 + 2x2 <=10
 *5x1 + 2x2 >=20
 *
 *Bsp Array: Double ar = {
 *	{1d,2d,10d}
 *	{-5d,-2d,-20d}
 *	{-13d, -8d, 0d}
 *}
 * </pre>
 */

public class SimplexProblem {

	private final SimplexTableau initialTableau; // Beinhaltet Ausgangsform des Simplextableaus bei Initialisierung
	private SimplexTableau tableau; // Aktuelles Array, welches den aktuellen Simplexschritt beinhaltet
	
	private double loesung[]; // Loesungsarray wobei letztes Feld den ZF Wert beinhaltet
	private LoesungsInformation loesungsInformation  = LoesungsInformation.EINE_OPTIMALE_LOESUNG; // optimistisch
	private boolean max;
	
	
	
	public SimplexProblem(SimplexTableau tableau, boolean max) {
		loesung = new double[tableau.getQ() + 1];
		this.max =max;
		this.tableau = tableau;
		initialTableau = tableau;
	}
	
	/**
	 * Funktion zum lösen dualer und primaler Simplexschritte. Errechnet den
	 * Optimalwert fuer die Entscheidungsvariablen und den Zielfunktionswert.
	 */
	public void loeseAllgemein() {
		while (isLoesbar() && !tableau.istDualGeloest()) {
			loeseDual();
			//printAll();
		}
		while (isLoesbar() && !tableau.istPrimalGeloest()) {
			loesePrimal();
			//printAll();
		}
		speichereL�sung();
		mehrereOptimale();
		
		//printStepSolution();
	}

	/**
	 * Liest die Lösung aus dem Tableau heraus und speichert diese im Lösungsarray
	 */
	private void speichereL�sung() {
		if (isLoesbar()) {
			int[] orte = new int[tableau.getQ()];
			int z = 0;

			for (int i = 0; i < tableau.getQ() + tableau.getM(); i++) {
				String temp = tableau.getSpaltenname(i);
				char charAt = temp.charAt(0);
				if (charAt == 'x') {
					orte[z] = i; // speichert die Orte im Tableau, an denen Entscheidungsvariablen (x) stehen
					z++;
				}
			}
			for (int i = 0; i < tableau.getQ(); i++) { // speichert Entscheidungsvariablen x1...xn an n-1ter Stelle in Loesungsarray
				String variablenname = tableau.getSpaltenname(orte[i]);
				char koeffizientennummer = variablenname.charAt(1);
				int kNummerInt = koeffizientennummer - '0' - 1; // Konvertierung vom Char ins int Format
				if (orte[i] < tableau.getQ()) {
					loesung[kNummerInt] = 0d;
					
				} else {
					loesung[kNummerInt] = tableau.getWert(orte[i] - tableau.getQ(), tableau.getQ());
					
				}
			}

			if( max) {																				// ZfFeld des Tableaus wird gesetzt
				loesung[tableau.getQ()] = tableau.getWert(tableau.getM(), tableau.getQ());
																				
			}else {
				loesung[tableau.getQ()] = tableau.getWert(tableau.getM(), tableau.getQ()) *-1;
			}
//			System.out.println(Arrays.toString(loesung));
		} else {
			for (int i = 0; i < loesung.length; i++) {
				loesung[i] = Double.NaN;
			}
		}
	}
	
	public SimplexTableau getInitialTableau() {
		return initialTableau;
	}
	
	public SimplexTableau getErgebnisTableau() {
		return tableau;
	}
	
	public double getZFWert() {
		return loesung[tableau.getQ()];
	}
	
	public boolean isLoesbar() {
		return loesungsInformation.isLoesbar();
	}

	/**
	 * Funktion zum Ausführen des dualen Austauschschritts
	 */
	private void loeseDual() {
		int pivotzeile = tableau.findPivotZeileDual();
		if (tableau.getDualLoesbar(pivotzeile)) {
			int pivotspalte = tableau.findePivotSpalteDual(pivotzeile);
			tableau = tableau.austauschen(new FeldId(pivotzeile, pivotspalte));
		} else {
			loesungsInformation = LoesungsInformation.KEINE_ZULAESSIGE_LOESUNG;
		}
	}

	/**
	 * Funktion zum Ausführen des primalen Austauschschritts
	 */

	private void loesePrimal() {
		int pivotspalte = tableau.findPivotSpaltePrimal();
		if (tableau.getPrimaerLoesbar(pivotspalte)) {
			int pivotzeile = tableau.findPivotZeilePrimal(pivotspalte);
			tableau = tableau.austauschen(new FeldId(pivotzeile, pivotspalte));
		} else {
			loesungsInformation= LoesungsInformation.KEINE_OPTIMALE_LOESUNG;
		}
	}

	/**
	 * Prüft ob evtl. mehrere Optimale Loesungen existieren und setzt Information
	 * ins Loesungsarray
	 */
	private void mehrereOptimale() {
		for (int i = 0; i < loesung.length - 1; i++) {
			if (loesung[i] == 0) {
				loesungsInformation= LoesungsInformation.MEHRERE_OPTIMALE_LOESUNGEN;
			}
		}
	}

	public void printArray() {
		tableau.printArray();
	}

	/**
	 * @deprecated Mehtode sollte nicht verwendet werden. printAll() gibt
	 *             Gesamtloesung aus und sollte stattdessen verwendet werden.
	 */
	public void printStepSolution() { // gibt Teillösung aus, evtl redundant
		List<Integer> orte = new ArrayList<>(); // Liste, die Orte zum auslesen speichert
		for (int i = 0; i < tableau.getQ() + tableau.getM(); i++) {
			String temp = tableau.getSpaltenname(i);
			char charAt = temp.charAt(0);
			if (charAt == 'x') {
				orte.add(i); // speichert die Orte im Tableau, an denen Entscheidungsvariablen (x) stehen
			}
		}
		for (Integer e : orte) {
			if (e < tableau.getQ()) {
				System.out.println(tableau.getSpaltenname(e) + ": " + 0);

			} else {
				System.out.println(tableau.getSpaltenname(e) + ": " + tableau.getWert(e - tableau.getQ(), tableau.getQ()));
			}
		}
		System.out.println("Zielfunktionswert: " + tableau.getWert(tableau.getM(), tableau.getQ()));
		if (!isLoesbar()) {
			System.out.println("P hat keine optimale Lösung");
		}
	}

	/**
	 * Gibt Gesamtloesung des Simplexalgorithmus aus
	 */
	public void printAll() {
		tableau.printAll();
	}

	public int getQ() {
		return tableau.getQ();
	}
	public int getM() {
		return tableau.getM();
	}

	public double[] getLoesung() {
		return loesung;
	}
	
	public String printLoesungHTML() {
		String dec = "0.";
		for(int i = 0 ; i <Helper.KOMMASTELLEN; i++ ) {
			dec = dec + "0";
		}
		DecimalFormat df = new DecimalFormat(dec); 
		String html = "<html> <table> ";
		for(int i = 0 ; i < loesung.length; i++) {
			
			html+= "<tr>";
			
			if(i!=loesung.length-1) {
			html += "<td>x" +(i+1)+":</td> <td>"	+df.format(loesung[i]) + "</td>";
			}else {
				html += "<td>Zf:</td> <td>"	+df.format(loesung[i]) + "</td>";
			}
			html+=" </tr>";
			
			
					
					
		}
		html += " </table> </html>";
		
		return html;
	}
	

	public LoesungsInformation getLoesungsInformation() {
		return loesungsInformation;
	}

}
