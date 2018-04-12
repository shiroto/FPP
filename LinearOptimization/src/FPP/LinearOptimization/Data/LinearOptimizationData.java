package FPP.LinearOptimization.Data;

public class LinearOptimizationData {

	public boolean maximize;
	public double[] coefficients;
	public Restrictions restriction;

	/**
	 * cx = anzahl der x variablen. anzahl der y variablen = coefficients - cx
	 */
	public int cx;

	public int GetCX() {
		return cx;
	}

	public int GetCY() {
		return coefficients.length - cx;
	}
	
}
