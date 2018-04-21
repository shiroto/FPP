package FPP.LinearOptimization.Data;

public class LinearOptimizationDataUtility {

	public static Double[][] createDual(Double[][] simplexTableau) {
		// TODO input LinearOpimizationData
		
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
	
	public static Double[][] convertNotNegativity(Double[][] simplexTableau) {
		// TODO input LinearOpimizationData
		throw new UnsupportedOperationException("The method is not implemented yet.");
	}
	
	public static Double[] extractFunction(Double[][] simplexTableau) {
		int functionLength = simplexTableau[0].length;

		Double[] function = new Double[functionLength];
		for (int i = 0; i < functionLength; i++) {
			function[i] = simplexTableau[simplexTableau.length-1][i];
		}
		return function;
	}

}