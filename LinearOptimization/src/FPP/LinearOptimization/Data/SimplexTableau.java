package FPP.LinearOptimization.Data;

public class SimplexTableau {
	private Double[][] tableau;
	private int iteration;
	private Algorithm algorithm;
	// TODO Textdatei für Erklärung (SimplexTableau DTO)
	
	/**
	 * Data Object for transfer between GUI and algorithm<br>
	 * <br>
	 * Format [n+1]["4"]:<br>
	 * Restriction 1 -> {{x, x, x, x, x},<br>
	 * Restriction n -> {x, x, x, x, x},<br>
	 * Function -> {x, x, x, x, x}}
	 * 
	 * @param	tableau		simplex tableau
	 * @param	iteration	step of algorithm
	 * @param	algorithm	algorithm used for calculation
	 */
	public SimplexTableau(Double[][] tableau, int iteration, Algorithm algorithm) {
		super();
		this.tableau = tableau;
		this.iteration = iteration;
		this.algorithm = algorithm;
	}

	public Double[][] getTableau() {
		return tableau;
	}

	public void setTableau(Double[][] tableau) {
		this.tableau = tableau;
	}

	public int getIteration() {
		return iteration;
	}

	public void addIteration() {
		this.iteration =+ 1;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}
}
