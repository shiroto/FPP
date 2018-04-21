package FPP.LinearOptimization.Data;

public class LinearOptimizationData {

	private Double[][] simplexTableau;
	
	/**
	 * Initializes input data for linear optimization with a simplex tableau.
	 * 
	 * Simplex tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 2d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 0d}} <b>function</b>
	 * Last column of restrictions is result
	 * Last column of function is constant
	 * All restrictions have to be <=
	 * Function must be minimizer
	 * 
	 * @param simplexTableau
	 */
	public LinearOptimizationData(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;
	}

	public Double[][] getSimplexTableau() {
		return simplexTableau;
	}

	public void setSimplexTableau(Double[][] simplexTableau) {
		this.simplexTableau = simplexTableau;
	}
}