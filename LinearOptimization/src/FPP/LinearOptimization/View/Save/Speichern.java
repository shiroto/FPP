package FPP.LinearOptimization.View.Save;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Speichert Objekte in der Form int numRestr <br>
 * int numVar<br>
 * boolean max<br>
 * double [] Zielfunktion<br>
 * 
 * boolean[]bin<br>
 * boolean [] ganzzahl<br>
 * restriktionen[numRes][numVar+1]<br>
 * 
 * @author stf34140
 *
 */

public class Speichern {
	String path;
	BranchAndBoundSpeicherKlasse bAndB;

	Writer w;

	public Speichern(String path, BranchAndBoundSpeicherKlasse bAndB) {
		this.path = path;
		this.bAndB = bAndB;

	}

	public boolean speichern() {

		try {
			w = new BufferedWriter(new FileWriter(path));
			schreibeBedingungen();
			w.close();
		} catch (IOException e) {

			return false;
		}

		return true;
	}

	private void schreibeBedingungen() throws IOException {
		int m = bAndB.getNumRestr();
		int q = bAndB.getNumVar();

		String ln = System.getProperty("line.separator");
		w.write(String.valueOf(m)); // AnzahlRestriktionen
		w.write("#");
		w.write(String.valueOf(q)); // Anzahl Entscheidungsvariablen
		w.write("#");
		if (bAndB.isMax()) { // min max Info
			w.write(String.valueOf(1));
		} else {
			w.write(String.valueOf(0));
		}
		w.write("#");
		Double[] zf = bAndB.getfunction();
		for (int i = 0; i < zf.length; i++) { // Zf
			w.write(String.valueOf(zf[i]));
			if (i != q - 1)
				w.write("*");

		}
		w.write("#");
		boolean[] bin = bAndB.getBin();
		for (int i = 0; i < q; i++) {
			if (bin[i])
				w.write(String.valueOf(1));
			else
				w.write(String.valueOf(0));
			if (i != q - 1)
				w.write("*");

		}
		w.write("#");
		for (int i = 0; i < q; i++) {
			if (bAndB.getGanzzahl()[i] == true) { // FlagInteger
				w.write(String.valueOf(1));
				if (i != q - 1)
					w.write("*");
			} else {
				w.write(String.valueOf(0));
				if (i != q - 1)
					w.write("*");
			}
		}
		w.write("#");
		Double werte[][] = bAndB.getArray();
		for (int i = 0; i < m; i++) { // Restriktionen [numRest][NumVar+1s]
			for (int e = 0; e < q + 1; e++) {
				w.write(String.valueOf(werte[i][e]));
				if (e != q)
					w.write("*");
			}
			if (i != m - 1)
				w.write("*");
		}
		w.write(ln);

	}

}
