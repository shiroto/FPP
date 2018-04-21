package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.List;

import FPP.LinearOptimization.Data.LinearOptimizationDataUtility;

/**
 * This represents the problem in form of a simplex tableau.<br>
 * Functions, coefficients and the objective function values are saved separately,<br>
 * so you always can generate a simplex tableau of the current problem.
 * 
 * @author Stefan
 */
public class Problem {

	// internal variables
	private Double[] function = new Double[0];
	private List<Double[]> coefficients = new ArrayList<>();
	private List<Double> b = new ArrayList<Double>();
	
	/**
	 * Initializes a problem by a given simplex tableau.<br><br>
	 * 
	 * Tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b>
	 * 
	 * @param simplexTableau
	 */
	public Problem(Double[][] simplexTableau) {
		// define dimension of function and coefficients
		int restrictionCNT = simplexTableau.length-1;
		int coefficientLength = simplexTableau[0].length-1;
		int functionLength = simplexTableau[0].length;
		
		this.function = LinearOptimizationDataUtility.extractFunction(simplexTableau);
		
		if (restrictionCNT > 0) {
			/*
			 * Restrictions are given, so add them to the problem,
			 * and divide them into coefficients and objective function values (b)
			 * (IGNORE last line, because thats already our function)
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
	
	/**
	 * Returns the function of the current problem.<br><br>
	 * 
	 * <b>example tableau:</b><br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b><br><br>
	 * 
	 * In this case only the function line is returned:<br>
	 * <b>{0d, 0d, 3d, 10d, 500d}</b>
	 * 
	 * @return function
	 */
	public Double[] getFunction() {
		return function;
	}
	
	/**
	 * Updates the function of the problem.
	 * 
	 * @param function
	 */
	public void setFunction(Double[] function) {
		this.function = function;
	}
	
	/**
	 * Returns the object function values of the method restrictions.<br><br>
	 * 
	 * <b>example restrictions:</b><br>
	 * {{1d, 2d, 0d, <b>0d</b>},<br>
	 * {1d, 0d, 0d, <b>0d</b>},<br>
	 * {0d, 0d, 1d, <b>0d</b>}}<br><br>
	 * 
	 * Marked values of restrictions are returned this way:<br>
	 * <b>{0d, 0d, 0d}</b>
	 * 
	 * @return object function values
	 */
	public Double[] getB() {
		Double[] returnValue = new Double[b.size()];
		for (int i = 0; i < b.size(); i++) {
			returnValue[i] = b.get(i);
		}
		return returnValue;
	}
	
	/**
	 * This returns all coefficients of the problem restrictions.<br><br>
	 * 
	 * <b>example coefficients:</b><br>
	 * {{1d, 2d, 0d},<br>
	 * {1d, 0d, 0d},<br>
	 * {0d, 0d, 1d}}<br><br>
	 * 
	 * See difference to {@link #getRestrictions()}!
	 * 
	 * @return coefficients array
	 */
	public Double[][] getCoefficients() {
		// check if there are any coefficients
		if (coefficients.isEmpty()) {
			return new Double[0][0];
		}
		
		// define size of coefficients array
		int coefficientCnt = coefficients.size();
		int coefficientLength = coefficients.get(0).length;
		
		// initialize new array for coefficients
		Double[][] coefficientsArray = new Double[coefficientCnt][coefficientLength];
		
		// fill coefficients array
		for (int i = 0; i < coefficientCnt; i++) {
			for(int j = 0; j < coefficientLength; j++) {
				coefficientsArray[i][j] = coefficients.get(i)[j];
			}
		}
		return coefficientsArray;
	}
	
	/**
	 * This returns all restrictions of the problem.<br><br>
	 * 
	 * <b>example restrictions:</b><br>
	 * {{1d, 2d, 0d, 0d},<br>
	 * {1d, 0d, 0d, 0d},<br>
	 * {0d, 0d, 1d, 0d}}<br><br>
	 * 
	 * See difference to {@link #getCoefficients()}!
	 * 
	 * @return restrictions
	 */
	public Double[][] getRestrictions() {
		// check if there are any coefficients
		if (coefficients.isEmpty()) {
			return new Double[0][0];
		}
		// define size of restriction array
		int restrictionLength = coefficients.get(0).length+1;
		int restrictionCnt = coefficients.size();
		
		// initialize new restrictions array
		Double[][] restrictionsArray = new Double[restrictionCnt][restrictionLength];
		
		// fill array with coefficients and objective function values
		for (int i = 0; i < restrictionCnt; i++) {
			for(int j = 0; j < restrictionLength-1; j++) {
				restrictionsArray[i][j] = coefficients.get(i)[j];
			}
			// objective function values have to be added to the coefficients too
			restrictionsArray[i][restrictionLength-1] = b.get(i);
		}
		return restrictionsArray;
	}
	
	/**
	 * This adds a restriction to the problem.<br><br>
	 * 
	 * <b>example restriction:</b><br>
	 * {1d, 2d, 0d, 0d}<br>
     * <p><ul>
     * <li>1d, 2d, 0d => <b>coefficients</b>
     * <li>0d => <b>b</b>
     * </ul><p>
	 * 
	 * @param coefficients
	 * @param b objective function value
	 */
	public void addRestriction(Double[] coefficients, Double b) {
		this.coefficients.add(coefficients);
		this.b.add(b);
	}
	
	/**
	 * Returns a simplex tableau of the current problem.<br><br>
	 * 
	 * <b>example tableau:</b><br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b><br>
	 * 
	 * @return simplex tableau
	 */
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
	
	/**
	 * Defines if this problem is solvable with <b>branch and bound</b> algorithm.<br>
	 * 
	 * @return boolean
	 */
	public boolean isSolvableWithBAndB() {
		return false;
	}
}