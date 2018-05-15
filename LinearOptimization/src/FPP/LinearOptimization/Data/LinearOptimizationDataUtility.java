package FPP.LinearOptimization.Data;

public class LinearOptimizationDataUtility {

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
	
	public static Double[] extractFunction(Double[][] simplexTableau) {
		int functionLength = simplexTableau[0].length;

		Double[] function = new Double[functionLength];
		for (int i = 0; i < functionLength; i++) {
			function[i] = simplexTableau[simplexTableau.length-1][i];
		}
		return function;
	}
	
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

}