package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.List;

public class Problem {

	private Double[] function = new Double[0];
	private List<Double[]> coefficients = new ArrayList<>();
	private List<Double> b = new ArrayList<Double>();
	
	public Problem(Double[][] simplexTableau) {
		int restrictionCNT = simplexTableau.length-1;
		int coefficientLength = simplexTableau[0].length-1;
		int functionLength = simplexTableau[0].length;
		
		// extract function of simplex tableau
		Double[] function = new Double[functionLength];
		for (int i = 0; i < functionLength; i++) {
			function[i] = simplexTableau[restrictionCNT][i];
		}
		this.function = function;
		
		if (restrictionCNT > 0) {
			/*
			 * Restrictions are given, so add them to the problem,
			 * and divide them into coefficients and objective function values (b)
			 */
			for (int i = 0; i < restrictionCNT; i++) {
				Double[] c = new Double[coefficientLength];
				for (int j = 0; j < coefficientLength; j++) {
					c[j] = simplexTableau[i][j];
				}
				this.coefficients.add(c);
				this.b.add(simplexTableau[i][functionLength-1]);
			}
		}
	}
	
	public Double[] getFunction() {
		return function;
	}
	
	public void setFunction(Double[] function) {
		this.function = function;
	}
	
	public Double[] getB() {
		Double[] returnValue = new Double[b.size()];
		for (int i = 0; i < b.size(); i++) {
			returnValue[i] = b.get(i);
		}
		return returnValue;
	}
	
	public Double[][] getCoefficients() {
		if (coefficients.isEmpty()) {
			return new Double[0][0];
		}
		int coefficientLength = coefficients.get(0).length;
		Double[][] returnValue = new Double[coefficients.size()][coefficientLength];
		int i = 0;
		for (Double[] coefficient : coefficients) {
			for(int j = 0; j < coefficientLength; j++) {
				returnValue[i][j] = coefficient[j];
			}
			i++;
		}
		return returnValue;
	}
	
	public Double[][] getRestrictions() {
		if (coefficients.isEmpty()) {
			return new Double[0][0];
		}
		// objective function values have to be added to the coefficients too
		int restrictionLength = coefficients.get(0).length+1;
		
		// define size of return value
		Double[][] returnValue = new Double[coefficients.size()][restrictionLength];
		int i = 0;
		for (Double[] restriction : coefficients) {
			for(int j = 0; j < restrictionLength-1; j++) {
				returnValue[i][j] = restriction[j];
			}
			returnValue[i][restrictionLength-1] = b.get(i);
			i++;
		}
		return returnValue;
	}
	
	public void addRestriction(Double[] coefficients, Double b) {
		this.coefficients.add(coefficients);
		this.b.add(b);
	}
	
	public Double[][] getSimplexTableau() {
		// define size of tableau
		int tableauLength = coefficients.size() + 1;
		int tableauWidth = function.length;
		
		// init empty tableau
		Double[][] simplexTableau = new Double[tableauLength][tableauWidth];
		
		// fill with restrictions
		Double[][] restrictions = getRestrictions();
	    for (int i = 0; i < coefficients.size(); i++) {
	        System.arraycopy(restrictions[i], 0, simplexTableau[i], 0, restrictions[i].length);
	    }
	    // add function add last position of tableau
	    System.arraycopy(function, 0, simplexTableau[tableauLength-1], 0, function.length);
		return simplexTableau;
	}
	
	public boolean isSolveableWithBAndB() {
		return false;
	}
}