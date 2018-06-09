package FPP.LinearOptimization.View.Save;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Laden {

	String path;
	BufferedReader reader;
	BranchAndBoundSpeicherKlasse bAndB;

	public Laden(String path) {

		this.path = path;

	}

	public BranchAndBoundSpeicherKlasse lese() {

		int numRestr;
		int numVar;
		boolean max;
		Double[] zielfunktion;
		boolean[] binFlag;
		boolean[] ganzzahlFlag;
		Double[][] restriktionen;
		BranchAndBoundSpeicherKlasse bAndB;

		try {
			this.reader = new BufferedReader(new FileReader(path));
			String input = reader.readLine();
			String[] teile = input.split("#");
			numRestr = Integer.valueOf(teile[0]);
			numVar = Integer.valueOf(teile[1]);
			restriktionen = new Double[numRestr][numVar + 1];
			zielfunktion = new Double[numVar];
			binFlag = new boolean[numVar];
			if (Integer.valueOf(teile[2]) == 1) {
				max = true;
			} else {
				max = false;
			}

			String[] zfTeile = teile[3].split("\\*"); // muss mit zwei Klammern vorangestellt werden um nach * zu
														// teilen
			for (int i = 0; i < numVar; i++) {

				zielfunktion[i] = Double.valueOf(zfTeile[i]);

			}
			String[] binTeile = teile[4].split("\\*");
			for (int i = 0; i < numVar; i++) {
				if (Integer.valueOf(binTeile[i]) == 1)
					binFlag[i] = true;
				else
					binFlag[i] = false;

			}

			String[] flagIntTeile = teile[5].split("\\*");
			ganzzahlFlag = new boolean[numVar];
			for (int i = 0; i < numVar; i++) {
				if (Integer.valueOf(flagIntTeile[i]) == 1) {
					ganzzahlFlag[i] = true;
				} else {
					ganzzahlFlag[i] = false;
				}
			}
			String[] arrTeile = teile[6].split("\\*");
			int z = 0;
			for (int i = 0; i < numRestr; i++) {
				for (int e = 0; e < numVar + 1; e++) {
					restriktionen[i][e] = Double.valueOf(arrTeile[z]);
					z++;
				}
			}
			bAndB = new BranchAndBoundSpeicherKlasse();
			bAndB.setNumRestr(numRestr);
			bAndB.setNumVar(numVar);
			bAndB.setMax(max);
			bAndB.setFunction(zielfunktion);
			bAndB.setBin(binFlag);
			bAndB.setGanzzahl(ganzzahlFlag);
			bAndB.setArray(restriktionen);

			return bAndB;
		} catch (IOException e) {

		} finally {
			try {
				reader.close();
			} catch (IOException e) {

			}
		}

		return null;

	}

}
