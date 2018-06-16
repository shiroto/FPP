package FPP.LinearOptimization.Model.BranchAndBound;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import FPP.LinearOptimization.View.Gui.InputScreenBB;
import FPP.LinearOptimization.View.Gui.BranchAndBound.*;

/**
 * Die SimplexTableau Klasse enthält das jeweilige Tableau inklusive
 * Spaltenbezeichnungen. Das Ausgangsarray ist ein zweidimensionales Double
 * array mit m+1 Zeilen. Dabei sei m sei die Anzahl der Restriktionen. In den
 * ersten m Zeilen stehen die Restriktionen, die in der Standardform a1x+a12x...
 * <= b1 vorliegen
 * <p>
 * Sollten im ursprünglichen Problem >= Bedingungen vorliegen, muss diese Zeile
 * durch Multiplikation mit *-1 in die Normalform gebracht werden. In der
 * letzten Zeile steht die Zielfunktion, das letzte Feld der Zeile ist immer 0d.
 * Bei Maximierungsproblemen wird die ursprüngliche Zielfunktion mit *-1
 * multipliziert bei min Problemen kann diese übernommen werden. Diese Schritte
 * geschehen bei Eingabe durch die GUI automatisch, müssen bei alleiniger
 * Verwendung der Klasse allerdings manuell durchgeführt werden.
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
 * 
 * @author stf34140
 *
 */

public class SimplexTableau {

	private Double[][] array; // Array welches die Restriktionen und die Zielfunktion beinhaltet
	private String[] spalten; // Beinhaltet die Spaltenbezeichnungen
	private int m; // Anzahl Restriktionen des tableaus
	private int q; // Anzahl Entscheidungsvariablen
	private final double TOLERANZ = 0.000000001;

	public SimplexTableau(Double[][] arrayIn) {
		q = arrayIn[0].length - 1;
		m = arrayIn.length - 1;
		spalten = new String[q + m];
		array = new Double[arrayIn.length][arrayIn[0].length];
		for (int i = 0; i < arrayIn.length; i++) {
			array[i] = Arrays.copyOf(arrayIn[i], arrayIn[i].length);
		}
		for (int i = 0; i < (q + m); i++) { // Spaltenbezeichnungen werden initialisiert
			if (i < q) {
				spalten[i] = "x" + (i + 1);
			} else {
				spalten[i] = "y" + (i - q + 1);
			}
		}
	}

	private SimplexTableau(Double[][] array, int q, int m, String[] spalten) {
		this.array = array;
		this.q = q;
		this.m = m;
		this.spalten = spalten;
	}

	/**
	 *Rundet Gleitkomma Werte innerhalb Toleranzwert auf bzw. ab
	 */
	
	public void korrigiereGleitkommaFehler(){
				for(int i = 0; i< m+1 ;i++) {
					for ( int e = 0; e< q+1; e++) {
						if(  Math.abs(array[i][e]- Math.round(array[i][e]))< TOLERANZ ) {
							array[i][e]= (double) Math.round(array[i][e]);
						}
					}
				}
	}
	
	public int getM() {
		return m;
	}

	public int getQ() {
		return q;
	}

	/**
	 * Gibt Wert im Tableau zurück
	 * 
	 * @param i
	 *            Zeilenindex
	 * @param j
	 *            Spaltenindex
	 */
	public double getWertAt(int i, int j) {

		return this.array[i][j];
	}

	public Double[] getZielfunktion() {
		return array[m];
	}

	/**
	 * Prüft ob alle Zielfunktionskoeffizienten positiv , d.h. die Abbruchbedingung
	 * für primalen Simplexalgorithmus
	 */
	public boolean istPrimalGeloest() {
		boolean solved = true;
		for (int i = 0; i < q; i++) {
			if (array[m][i] < 0) {
				solved = false;
			}
		}
		return solved;
	}

	/**
	 * Prüft Lösbarkeitsbedingung im Primalen, d.h. es muss ein a >0 in Pivotspalte
	 * existieren ansonsten exisitert keine optimale Lösung.
	 */

	public boolean getPrimalLoesbar(int pivotspalte) {
		int anzahlPos = 0;
		for (int i = 0; i < m; i++) {
			if (array[i][pivotspalte] > 0)
				anzahlPos++;
		}
		return anzahlPos > 0;
	}

	/**
	 * Prüft ob alle Werte in letzter Spalte >0 und und dualer Austauschschritt
	 * damit beendet ist
	 */
	public boolean istDualGeloest() {
		for (int i = 0; i < m; i++) {
			if (array[i][q] < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Es muss ein Element in Pivotzeile geben, dass <0 ist ansonsten ist das
	 * Problem nicht loesbar
	 */
	public boolean getDualLoesbar(int pivotzeile) {
		int anzahlPos = 0;
		for (int i = 0; i < q; i++) {
			if (array[pivotzeile][i] < 0) {
				anzahlPos++;
			}
		}
		return anzahlPos > 0;
	}

	/**
	 * Findet Pivotzeile im dualen Austauschschritt
	 */
	public int findePivotZeileDual() {
		double min = Double.MAX_VALUE;
		int pivot = 0;
		for (int i = 0; i < m; i++) {
			if (array[i][q] < min) {
				pivot = i;
				min = array[i][q];
			}
		}
		return pivot;
	}

	/**
	 * Findet Pivotspalte im dualen Austauschschritt
	 */
	public int findePivotSpalteDual(int zeile) {
		int pspalte = 0;
		double[] hilfszeile = new double[q];
		for (int i = 0; i < q; i++) { // schreibt in Hilfszeile
			double result = -Double.MAX_VALUE;
			if (array[zeile][i] < 0) {
				result = array[m][i] / array[zeile][i];
			}
			hilfszeile[i] = result;
		}
		double maxvalue = hilfszeile[0];
		for (int i = 0; i < q; i++) {
			if (hilfszeile[i] > maxvalue) {
				pspalte = i;
				maxvalue = hilfszeile[i];
			}
		}
		return pspalte;
	}

	/**
	 * Findet Pivotzeile im primalen Austauschschritt
	 */
	public int findePivotZeilePrimal(int pivotspalte) {
		double pivotZeileMinimum = Double.MAX_VALUE;
		int zeile = 0;
		double[] hilfsspalte = new double[m];
		for (int i = 0; i < m; i++) {
			if (array[i][pivotspalte] > 0) {
				hilfsspalte[i] = array[i][q] / array[i][pivotspalte];
			} else {
				hilfsspalte[i] = Double.MAX_VALUE;
			}
		}
		for (int i = 0; i < m; i++) {
			if (hilfsspalte[i] < pivotZeileMinimum && hilfsspalte[i] >= 0) {
				pivotZeileMinimum = hilfsspalte[i];
				zeile = i;
			}
		}
		return zeile;
	}

	/**
	 * Findet Pivotspalte im primalen Austauschschritt
	 */
	public int findePivotSpaltePrimal() {
		int spalte = 0;
		Double pivotMinimum = array[m][0];
		for (int i = 0; i < q; i++) {
			if (array[m][i] <= pivotMinimum) {
				pivotMinimum = array[m][i];
				spalte = i;
			}
		}
		return spalte;
	}

	/**
	 * Führt den Austauschschritt durch
	 */
	public SimplexTableau austauschen(FeldId pivotFeld) {
		String[] spaltenNeu = aendereSpaltenBezeichnung(pivotFeld); // aendert Bezeichnungen im Tableau
		Double[][] arrayNeu = new Double[m + 1][q + 1];
		arrayNeu[pivotFeld.zeileIdx][pivotFeld.spalteIdx] = 1 / getWert(pivotFeld);
		aenderePivotSpalte(arrayNeu, pivotFeld);
		aenderePivotZeile(arrayNeu, pivotFeld);
		aendereRestliche(arrayNeu, pivotFeld);
		return new SimplexTableau(arrayNeu, q, m, spaltenNeu);
	}

	private String[] aendereSpaltenBezeichnung(FeldId pivot) {
		String[] spaltenNeu = Arrays.copyOf(spalten, spalten.length);
		spaltenNeu[pivot.zeileIdx + q] = spalten[pivot.spalteIdx];
		spaltenNeu[pivot.spalteIdx] = spalten[pivot.zeileIdx + q];
		return spaltenNeu;
	}

	private void aenderePivotSpalte(Double[][] arrayNeu, FeldId pivotFeld) {
		for (int i = 0; i <= m; i++) {
			if (i != pivotFeld.zeileIdx) {
				arrayNeu[i][pivotFeld.spalteIdx] = array[i][pivotFeld.spalteIdx] / getWert(pivotFeld) * -1;
			}
		}
	}

	private void aenderePivotZeile(Double[][] arrayNeu, FeldId pivotFeld) {
		for (int i = 0; i <= q; i++) {
			if (i != pivotFeld.spalteIdx) {
				arrayNeu[pivotFeld.zeileIdx][i] = array[pivotFeld.zeileIdx][i] / getWert(pivotFeld);
			}
		}
	}

	/**
	 * Aendert alle uerigen Tableau Elemente, die nicht in der Pivotspalte oder
	 * Pivotzeile stehen
	 */
	private void aendereRestliche(Double[][] arrayNeu, FeldId pivotFeld) {
		for (int i = 0; i <= m; i++) {
			for (int e = 0; e <= q; e++) {
				if (i != pivotFeld.zeileIdx && e != pivotFeld.spalteIdx) {
					arrayNeu[i][e] = array[i][e]
							- ((array[pivotFeld.zeileIdx][e] * array[i][pivotFeld.spalteIdx]) / getWert(pivotFeld));
				}
			}
		}
	}

	public double getWert(FeldId feld) {
		return array[feld.zeileIdx][feld.spalteIdx];
	}

	public double getWert(int zeilenIdx, int spaltenIdx) {
		return array[zeilenIdx][spaltenIdx];
	}

	public String getSpaltenname(int spaltenIdx) {
		return spalten[spaltenIdx];
	}

	public void printArray() {
		for (int i = 0; i < m + 1; i++) {
			for (int e = 0; e < q + 1; e++) {
				System.out.print(array[i][e] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Fügt dem Tableau die neue Bedingung hinzu. Diese Bedingung wird der Funktion
	 * in der Form eines Double Arrays als Input uebergeben. Diese neue Bedingung
	 * wird in die vorletzte Zeile des neuen 2D-Arrays eingefügt. Die Zeilen davor
	 * werden aus dem Ausgangstableau übernommen. Die letzte Zeile des neuen
	 * 2D-Arrays entspricht wieder der Zielfunktion.
	 */
	public SimplexTableau erstelle2dArray(double[] neu) {
		Double[][] ar = new Double[m + 2][q + 1];
		for (int i = 0; i < m; i++) {
			for (int e = 0; e < q + 1; e++) {
				ar[i][e] = array[i][e];
			}
		}
		for (int i = 0; i < q + 1; i++) {
			ar[m][i] = neu[i];
		}
		for (int i = 0; i < q + 1; i++) {
			ar[m + 1][i] = array[m][i];
		}
		return new SimplexTableau(ar);
	}

	/**
	 * Erstellt die neue Restriktion und gibt diese als Double Array zurück. Diese
	 * Array hat die Form, dass dieses einfach in das neue Tableau eingefügt werden
	 * kann.
	 * 
	 * @param index
	 *            Gibt an, für welche Entscheidungsvariable die neue Restriktion
	 *            gilt
	 * @param restriktionsWert
	 *            Gibt den abgerundeten oder aufgerundeten Wert für die neue
	 *            Restriktion an
	 * @param flag
	 *            Gibt an ob <= oder >= Bedingungen vorliegen. (0 <= ); (1 >=). Ist
	 *            flag 1 muessen alle Werte mit -1 multipliziert werden um aus
	 *            groesser gleich Bedingung kleiner gleich Bedingung zu machen
	 * @return
	 */
	public double[] neueRestriktion(int index, double restriktionsWert, int flag) {
		double[] neueRestriktion = new double[q + 1];
		for (int i = 0; i < neueRestriktion.length; i++) {
			if (i == index) {
				if (flag == 0) {
					neueRestriktion[i] = 1;
				} else {
					neueRestriktion[i] = -1;
				}
			} else if (i != index && i != q) {
				neueRestriktion[i] = 0d;
			} else {
				if (flag == 0) {
					neueRestriktion[i] = restriktionsWert;
				} else {
					neueRestriktion[i] = restriktionsWert * -1;
				}
			}
		}
		return neueRestriktion;
	}

	/**
	 * Gibt das Tableau aus.
	 */
	public void printAll() {
		for (int i = 0; i <= m + 1; i++) {
			for (int e = 0; e <= q + 1; e++) {
				if (i == 0) {
					if (e == 0 || e > q) {
						System.out.print("-   ");
					} else {
						System.out.print(spalten[e - 1] + " ");
					}
				}
				if (i > 0 && i < m + 1) {
					if (e == 0) {
						System.out.print(spalten[q - 1 + i] + " ");
					} else {
						System.out.print(array[i - 1][e - 1] + " ");
					}
				}
				if (i == m + 1) {
					if (e == 0 || e > q + 1) {
						System.out.print("-   ");
					} else {
						System.out.print(array[i - 1][e - 1] + " ");
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * Stellt das Tableau in HTML Form dar
	 * 
	 * @return
	 */
	public String printHTML() {
		String dec = "0.";
		for (int i = 0; i < InputScreenBB.KOMMASTELLEN; i++) {
			dec = dec + "0";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH); // Decimal Seperator auf . setzen
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern(dec);

		String html = "<html> <table style=\"width:100%\">";
		for (int i = 0; i <= m + 1; i++) {
			html += "<tr>";
			for (int e = 0; e <= q + 1; e++) {
				if (i == 0) {
					if (e == 0 || e > q) {
						html += "<td> - </td>";
					} else {
						html += "<td>" + (spalten[e - 1]) + "</td>";
					}
				}
				if (i > 0 && i < m + 1) {
					if (e == 0) {
						html += "<td>" + (spalten[q - 1 + i]) + "</td>";
					} else {
						html += "<td>" + df.format(array[i - 1][e - 1]) + "</td>";
					}
				}
				if (i == m + 1) {
					if (e == 0 || e > q + 1) {
						html += "<td> -  </td>";
					} else {
						html += "<td>" + df.format(array[i - 1][e - 1]) + "</td>";
					}
				}
			}
			html += "</tr>";
		}
		html += "</table> </html>";

		return html;
	}

	public static class FeldId {

		private final int zeileIdx;
		private final int spalteIdx;

		public FeldId(int zeileIdx, int spalteIdx) {
			this.zeileIdx = zeileIdx;
			this.spalteIdx = spalteIdx;
		}

		public int getZeileIdx() {
			return zeileIdx;
		}

		public int getSpalteIdx() {
			return spalteIdx;
		}

	}

}
