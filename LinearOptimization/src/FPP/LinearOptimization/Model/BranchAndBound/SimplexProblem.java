package FPP.LinearOptimization.Model.BranchAndBound;

import java.text.DecimalFormat;
import java.util.*;

import FPP.LinearOptimization.Model.BranchAndBound.SimplexTableau.FeldId;
import FPP.LinearOptimization.View.Gui.InputScreenBB;
import FPP.LinearOptimization.View.Gui.BranchAndBound.*;

/**
 * Die SimplexProblem Klasse ehält im Konstruktor ein SimplexTableau, welches
 * die Koeffizientmatrix darstellt. Das Simplex Problem enthält Start,
 * Endtableau, und die Lösung.
 */

public class SimplexProblem {

	private final SimplexTableau initialTableau; // Beinhaltet die Ausgangsform des Simplextableaus bei Initialisierung
	private SimplexTableau tableau; // Aktuelles Array, welches den aktuellen Simplexschritt beinhaltet

	private double[] loesung; // Loesungsarray wobei das letzte Feld den ZF Wert beinhaltet
	private LoesungsInformation loesungsInformation = LoesungsInformation.EINE_OPTIMALE_LOESUNG;
	private boolean max;
	

	public SimplexProblem(SimplexTableau tableau, boolean max) {
		loesung = new double[tableau.getQ() + 1];
		this.max = max;
		this.tableau = tableau;
		initialTableau = tableau;
	}

	/**
	 * Funktion zum lösen dualer und primaler Simplexschritte. Errechnet den
	 * Optimalwert für die Entscheidungsvariablen und den Zielfunktionswert.
	 */

	public void loeseAllgemein() {
		while (isLoesbar() && !tableau.istDualGeloest()) {
			loeseDual();
			tableau.korrigiereGleitkommaFehler();
			tableau.printAll();
		}
		while (isLoesbar() && !tableau.istPrimalGeloest()) {
			loesePrimal();
			tableau.korrigiereGleitkommaFehler();
			tableau.printAll();
		}
		speichereLoesung();
		pruefeMehrereOptimale();
	}
	
	

	/**
	 * Liest die Lösung aus dem Tableau heraus und speichert diese im Lösungsarray
	 */
	private void speichereLoesung() {
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
			for (int i = 0; i < tableau.getQ(); i++) { // speichert Entscheidungsvariablen x1...xn an n-1ter Stelle in
														// Loesungsarray
				String variablenname = tableau.getSpaltenname(orte[i]);
				char koeffizientennummer = variablenname.charAt(1);
				int kNummerInt = koeffizientennummer - '0' - 1; // Konvertierung vom Char ins int Format
				if (orte[i] < tableau.getQ()) {
					loesung[kNummerInt] = 0d;

				} else {
					loesung[kNummerInt] = tableau.getWert(orte[i] - tableau.getQ(), tableau.getQ());

				}
			}

			if (max) { // ZfFeld des Tableaus wird gesetzt
				loesung[tableau.getQ()] = tableau.getWert(tableau.getM(), tableau.getQ());

			} else {
				loesung[tableau.getQ()] = tableau.getWert(tableau.getM(), tableau.getQ()) * -1;
			}

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
		int pivotzeile = tableau.findePivotZeileDual();
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
		int pivotspalte = tableau.findePivotSpaltePrimal();
		if (tableau.getPrimalLoesbar(pivotspalte)) {
			int pivotzeile = tableau.findePivotZeilePrimal(pivotspalte);
			tableau = tableau.austauschen(new FeldId(pivotzeile, pivotspalte));
		} else {
			loesungsInformation = LoesungsInformation.KEINE_OPTIMALE_LOESUNG;
		}
	}

	/**
	 * Prüft ob evtl. mehrere Optimale Loesungen existieren und setzt Information
	 * ins Loesungsarray
	 */
	private void pruefeMehrereOptimale() {
		for (int i = 0; i < loesung.length - 1; i++) {
			if (loesung[i] == 0) {
				loesungsInformation = LoesungsInformation.MEHRERE_OPTIMALE_LOESUNGEN;
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
	public void printStepSolution() { // gibt Teillösung aus
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
				System.out.println(
						tableau.getSpaltenname(e) + ": " + tableau.getWert(e - tableau.getQ(), tableau.getQ()));
			}
		}
		System.out.println("Zielfunktionswert: " + tableau.getWert(tableau.getM(), tableau.getQ()));
		if (!isLoesbar()) {
			System.out.println("P hat keine optimale L�sung");
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

		DecimalFormat df = InputScreenBB.getDecimalFormat();
		String html = "<html> <table> ";
		for (int i = 0; i < loesung.length; i++) {

			html += "<tr>";

			if (i != loesung.length - 1) {
				html += "<td>x" + (i + 1) + ":</td> <td>" + df.format(loesung[i]) + "</td>";
			} else {
				html += "<td>Zf:</td> <td>" + df.format(loesung[i]) + "</td>";
			}
			html += " </tr>";

		}
		html += " </table> </html>";

		return html;
	}

	public LoesungsInformation getLoesungsInformation() {
		return loesungsInformation;
	}

}
