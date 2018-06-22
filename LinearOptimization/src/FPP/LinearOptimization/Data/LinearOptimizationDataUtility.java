package FPP.LinearOptimization.Data;

public class LinearOptimizationDataUtility {

	/**
	 * Converts a given Problem as SimplexTableau to its dual form and returns the new SimplexTableau.
	 * 
	 * @param simplexTableau
	 * @return simplexTableau
	 */
	public static Double[][] createDual(Double[][] simplexTableau) {
		// transpose matrix
		Double[][] dualTableau = new Double[simplexTableau[0].length][simplexTableau.length];
        for (int row = 0; row < simplexTableau.length; row++) {
        	for (int col = 0; col < simplexTableau[0].length; col++) {
        		dualTableau[col][row] = simplexTableau[row][col];
        	}
        }
        
        // change coefficient prefix
        for (int row = 0; row < dualTableau.length - 1; row++) {
        	for (int col = 0; col < dualTableau[0].length - 1; col++) {
        		dualTableau[row][col] = dualTableau[row][col] * -1d;
        	}
        }
        
        return dualTableau;
	}

	/**
	 * Converts all variables that are <= into >= variables.
	 * 
	 * @param simplexTableau
	 * @param paramaterNegativeIndices indices of variables that can be negative
	 * @return simplexTableau
	 */
	public static Double[][] convertNotNegatives(Double[][] simplexTableau, int[] paramaterNegativeIndices) {
		int tableauLength = simplexTableau.length;
		Double[][] positiveTableau = new Double[tableauLength][simplexTableau[0].length];
		
		for (int col = 0; col < simplexTableau[0].length; col++) {
			if(arrayContains(paramaterNegativeIndices, col)) {
				// parameter-column is <= 0
				for (int row = 0; row < tableauLength; row++) {
					positiveTableau[row][col] = -1 * simplexTableau[row][col];
				}
			} else {
				// parameter-column is >= 0
				for (int row = 0; row < tableauLength; row++) {
					positiveTableau[row][col] = simplexTableau[row][col];
				}
			}
		}
		return positiveTableau;
	}
	
	/**
	 * Extracts and returns the function of a SimplexTableau.
	 * 
	 * @param simplexTableau
	 * @return function as array
	 */
	public static Double[] extractFunction(Double[][] simplexTableau) {
		int functionLength = simplexTableau[0].length;

		Double[] function = new Double[functionLength];
		for (int i = 0; i < functionLength; i++) {
			function[i] = simplexTableau[simplexTableau.length-1][i];
		}
		return function;
	}
	
	/**
	 * Checks if an array contains a specific value.
	 * 
	 * @param array array to search in
	 * @param v value to be searched
	 * @return <code>true</code> if in array or <code>false</code> if not
	 */
	public static boolean arrayContains(final int[] array, final int v) {
		boolean result = false;
		for (int i : array) {
			if (i == v) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * This method normalizes a solution array which contains
	 * a substitution for theta.<br>
	 * 
	 * <b>example:</b><br>
	 * <i>[0.0, 0.0, 1.0, <b>8192.0, 8192.0</b>, -33.00000000002339]</i> contains <b>theta1, theta2</b><br>
	 * will be replaced by:<br>
	 * <i>[0.0, 0.0, 1.0, <b>0.0</b>, -33.00000000002339]</i> contains <b>theta</b><br>
	 * because theta can be substituted by following:<br>
	 * <b>theta = theta1 - theta2</b>
	 * 
	 * @param solution
	 * @return
	 */
	public static Double[] normalizeTheta(Double[] solution, boolean isSimplex) {
		// init return solution
		int c = 0;
		if(isSimplex) {
			c += 1;
		}
		Double[] newSolution = new Double[solution.length-1];
		System.arraycopy(solution, 0, newSolution, 0, newSolution.length-2);
		
		// determine theta1 & theta2
		Double theta1 = solution[solution.length- (3 + c)];
		Double theta2 = solution[solution.length- (2 + c)];
		
		// resubstitute theta
		newSolution[newSolution.length- (2 + c)] = theta1 - theta2;
		
		// copy "zielfunktionswert"
		System.arraycopy(solution, newSolution.length, newSolution, newSolution.length-1, 1);
		if(isSimplex) {
			System.arraycopy(solution, newSolution.length-1, newSolution, newSolution.length-2, 1);
		}
		solution = newSolution;
		return solution;
	}

	/**
	 * This method substitutes theta in an simplex tableau.<br>
	 * <b>Starting tableau:</b><br>
	 * <i>{{1.0d, 0.0d, 0.0d, <b>0.0d</b>, 1.0d},</i><br>
	 * <i>{0.0d, 1.0d, 0.0d, <b>0.0d</b>, 1.0d},</i><br>
	 * <i>{0.0d, 0.0d, 1.0d, <b>0.0d</b>, 1.0d},</i><br>
	 * <i>{0.0d, 0.0d, 0.0d, <b>-1.0d</b>, 0.0d},</i><br>
	 * <i>{-74997.5d, -40004.0d, -39988.0d, <b>-1.0d</b>, -34997.5d},</i><br>
	 * <i>{-49995.0d, 0.0d, -79992.0d, <b>-1.0d</b>, -29997.0d},</i><br>
	 * <i>{42.0d, 18.0d, 33.0d, <b>1.0d</b>, 0.0d}}</i><br><br>
	 * 
	 *  will <b>theta</b> will be substituted by following tableau:<br><br>
	 *  
	 * <i>{1.0d, 0.0d, 0.0d, <b>0.0d, 0.0d</b>, 1.0d}, </i><br>
	 * <i>{0.0d, 1.0d, 0.0d, <b>0.0d, 0.0d</b>, 1.0d},</i><br>
	 * <i>{0.0d, 0.0d, 1.0d, <b>0.0d, 0.0d</b>, 1.0d},</i><br>
	 * <i>{0.0d, 0.0d, 0.0d, <b>-1.0d, 1.0d</b>, 0.0d},</i><br>
	 * <i>{-74997.5d, -40004.0d, -39988.0d, <b>-1.0d, 1.0d</b>, -34997.5d},</i><br>
	 * <i>{-49995.0d, 0.0d, -79992.0d, <b>-1.0d, 1.0d</b>,-29997.0d},</i><br>
	 * <i>{42.0d, 18.0d, 33.0d, <b>1.0d, -1.0d</b>, 0.0d}}</i><br>
	 * so theta will be replaced by <b>theta1</b> and <b>theta2</b>:<br>
	 * <b>theta = theta1 - theta2</b>
	 * 
	 * @param simplexTableau
	 * @return
	 */
	public static Double[][] splitTheta(Double[][] simplexTableau) {
		// init calculation variables
		int tableauLength = simplexTableau.length;
		int tableauWidth = simplexTableau[0].length;
		Double[][] thetaTableau = new Double[tableauLength][tableauWidth+1];
		
		// determine theta
		Double[] function = LinearOptimizationDataUtility.extractFunction(simplexTableau);

		// substitute restrictions
		for (int i = 0; i < tableauLength-1; i++) {
			double theta = simplexTableau[i][tableauWidth-2];
			Double[] restriction = simplexTableau[i];
			thetaTableau[i] = substituteTheta(restriction, -theta);
		}
		// substitute function
		double theta = function[tableauWidth-2];
		thetaTableau[tableauLength-1] = substituteTheta(function, -theta);
		return thetaTableau;
	}
	
	/**
	 * Substitutes in the restrictions.
	 * 
	 * @param restriction
	 * @param theta
	 * @return
	 */
	private static Double[] substituteTheta(Double[] restriction, Double theta) {
		int restrictionLength = restriction.length;
		Double[] result = new Double[restrictionLength+1];
		System.arraycopy(restriction, 0,
				result, 0, restrictionLength-1);
		result[restrictionLength-1] = theta;
		System.arraycopy(restriction, restrictionLength-1,
				result, restrictionLength, 1);
		return result;
	}

	/**
	 * Returns a new solution array without the additional info of the simplex return.<br>
	 * So the solution is similar to the Branch and Bound output.
	 * 
	 * @param solution
	 * @return
	 */
	public static Double[] extractSimplexSolution(Double[] solution) {
		Double[] newSolution = new Double[solution.length-1];
		System.arraycopy(solution, 0, newSolution, 0, newSolution.length);
		return newSolution;
	}
}