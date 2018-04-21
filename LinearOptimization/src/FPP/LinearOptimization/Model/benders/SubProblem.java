package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubProblem extends Problem {
	
	private List<Double[]> coefficientsY = new ArrayList<>();
	
	/**
	 * Initializes a sub problem by a given simplex tableau.<br><br>
	 * 
	 * Tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b>
	 * 
	 * @param simplexTableau
	 */
	public SubProblem(Double[][] simplexTableau, List<Double[]> coefficientsY) {
		super(simplexTableau);
		this.coefficientsY = coefficientsY;
	}
	
	public Double[][] getCoefficientsY() {
		// check if there are any coefficients
		if (coefficientsY.isEmpty()) {
			return new Double[0][0];
		}

		// define size of coefficients array
		int coefficientCnt = coefficientsY.size();
		int coefficientLength = coefficientsY.get(0).length;

		// initialize new array for coefficients
		Double[][] coefficientsArray = new Double[coefficientCnt][coefficientLength];

		// fill coefficients array
		for (int i = 0; i < coefficientCnt; i++) {
			for (int j = 0; j < coefficientLength; j++) {
				coefficientsArray[i][j] = coefficientsY.get(i)[j];
			}
		}
		return coefficientsArray;
	}

}
