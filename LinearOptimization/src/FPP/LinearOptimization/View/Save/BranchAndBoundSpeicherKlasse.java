package FPP.LinearOptimization.View.Save;

import java.io.Serializable;
import java.util.Date;

public class BranchAndBoundSpeicherKlasse implements SaveableIF, Serializable {

	int numVar;
	int numRestr;
	Double[] function; // Laenge numVar;
	boolean max;
	boolean[] binaerVariablenListe; // Laenge numVar
	boolean[] ganzzahl; // Laenge numVar
	Double array[][]; // in Standarform mit <= Restriktionen
	Date time;

	public BranchAndBoundSpeicherKlasse() {
		this.time = new Date();
	}

	public void setNumVar(int numVar) {
		this.numVar = numVar;
	}

	public void setNumRestr(int numRestr) {
		this.numRestr = numRestr;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public void setBin(boolean[] bin) {
		this.binaerVariablenListe = bin;
	}

	public void setGanzzahl(boolean[] ganzzahl) {
		this.ganzzahl = ganzzahl;
	}

	public void setArray(Double[][] array) {
		this.array = array;
	}

	public int getNumVar() {
		return numVar;
	}

	public int getNumRestr() {
		return numRestr;
	}

	public Double[] getfunction() {
		return function;
	}

	public boolean isMax() {
		return max;
	}

	public boolean[] getBin() {
		return binaerVariablenListe;
	}

	public boolean[] getGanzzahl() {
		return ganzzahl;
	}

	public Double[][] getArray() {
		return array;
	}

	@Override
	public Double[] getFunction() {
		return this.function;
	}

	@Override
	public void setFunction(Double[] function) {
		this.function = function;
		
	}

}
