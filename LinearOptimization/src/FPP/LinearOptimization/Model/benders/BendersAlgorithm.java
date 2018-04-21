package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationDataUtility;
import FPP.LinearOptimization.Model.IBendersOptimization;
import de.lip.bb.Simplex;

public class BendersAlgorithm implements IBendersOptimization {

	private static final Double DOUBLE_CORRECTION = 0.000001d;

	@Override
	public IBendersOptimizationSolutionData solve(BendersOptimizationData bendersOptimizationData) {
		// TODO convert not-negativities, not possible to determine whether x is >= or <=
//		bendersOptimizationData.setSimplexTableau(LinearOptimizationDataUtility.convertNotNegativity(bendersOptimizationData.getSimplexTableau()));
		
		MasterProblem masterProblem = createMasterProblem(bendersOptimizationData);
		SubProblem subProblem = createSubProblem(bendersOptimizationData);
		Problem dualProblem = new Problem(LinearOptimizationDataUtility.createDual(subProblem.getSimplexTableau()));
		
		
		MasterProblem mProblem = new MasterProblem(new Double[][] {{180d, 1d, 0d}});
//		mProblem.addRestriction(new Double[]{-1d, 0d, 0d}); // add y >= 0 condition

		// dual subproblem
		Problem sProblem = new Problem(new Double[][] {{0d, 0d, 0d, 0d, 0d}});
		sProblem.addRestriction(new Double[]{1d, 2d, 0d, 0d}, 200d);
		sProblem.addRestriction(new Double[]{1d, 0d, 0d, 0d}, 50d);
		sProblem.addRestriction(new Double[]{0d, 0d, 1d, 0d}, 80d);
		sProblem.addRestriction(new Double[]{0d, 0d, 3d, 10d}, 500d);
		
		// step 0
		int r = 1;
		Double UB = Double.MAX_VALUE;
		Double LB = Double.MIN_VALUE;
		
		
		
		
//		Simplex step0 = new Simplex(sProblem.getSimplexArray(), true);
		Double[] u = new Double[sProblem.getFunction().length - 1];
		for (int i = 0; i < u.length; i++) {
			u[i] = 0d;
		}
		Double[] cut = calculateCut(sProblem, u);
//		mProblem.addRestriction(cut);
		
		Double[] simplexSolution;
		Double[] optimalY = null;
		while (true) {

			// step 1
			Simplex step1 = new Simplex(mProblem.getSimplexTableau(), false);
			simplexSolution = step1.loese();
			
			//check for valid solution
			if (simplexSolution[simplexSolution.length - 1] == 0) {
				//TODO what should happen here?
				//no solution existing
				break;
			}
		
			//TODO workaround for master problem with coefficients 0 a wrong result is returned from simplex
//			for (int i = 0; i < simplexSolution.length; i++) {
//				if (simplexSolution[i].equals(Double.NaN)) {
//					simplexSolution[i] = 0d;
//				}
//			}
			
			UB = simplexSolution[simplexSolution.length - 2] * (-1);
			
			Double[] y = extractSolutionCoefficients(simplexSolution);
			
			// step 2
			updateSubproblem(sProblem, y);
			simplexSolution = new Simplex(sProblem.getSimplexTableau(), false).loese();
		
			u = extractSolutionCoefficients(simplexSolution);
			
			// step 3 
			LB = calculateLowerBound(mProblem, y, simplexSolution[simplexSolution.length - 2]);

			//add output
			System.out.println("r = " + r + "\t LB = " + LB + "\t UB = " + UB);
			
			if (LB + DOUBLE_CORRECTION >= UB) {
				optimalY = y;
				break;
			}
			
			cut = calculateCut(sProblem, u);
			System.out.println("Add Cut " + Arrays.toString(cut) + "\n\n");
			mProblem.addRestriction(cut, 0d); // TODO Martin: brauchen den Zielfunktionswert hier extra
			
			r++;
		}
		
		if (optimalY != null ) {
			Problem originSubWithY = getOriginSubWithY(sProblem, optimalY);
			simplexSolution = new Simplex(originSubWithY.getSimplexTableau(), false).loese();
			
			u = extractSolutionCoefficients(simplexSolution);
			
			System.out.println("\n\nOptimal Solution = " + simplexSolution[simplexSolution.length - 2] * (-1));
			System.out.print("Coefficients = [");
			String coefficients = "";
			for (int i = 0; i < u.length; i++) {
				coefficients += u[i] + ", ";
			}
			
			for (int i = 0; i < optimalY.length - 1; i++) {
				coefficients += optimalY[i] + ", ";
			}
			
			System.out.print(coefficients.substring(0, coefficients.length() - 2) + "]");
		}
		
		return new BendersSolutionData();
	}

	private MasterProblem createMasterProblem(BendersOptimizationData bendersOptimizationData) {
		//TODO test
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;
		Double[] function = LinearOptimizationDataUtility.extractFunction(bendersOptimizationData.getSimplexTableau());
		Double[][] masterTableau = new Double[1][yCount + 1];
		
		// constant of function
		masterTableau[0][yCount] = function[function.length - 1];
		
		// function y coefficients
		int yColCount = 0;
		for (int col = 0; col < function.length - 1; col++) {
			if(arrayContains(yIndices, col)) {
				masterTableau[0][yColCount] = function[col];
				yColCount++;
			}
		}
		
		MasterProblem mp = new MasterProblem(masterTableau);
		mp.setTypes(bendersOptimizationData.getYTypes());
		return mp;
	}
	
	private SubProblem createSubProblem(BendersOptimizationData bendersOptimizationData) {
		//TODO test
		Double[][] originTableau = bendersOptimizationData.getSimplexTableau();
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;
		int restrictionCNT = originTableau.length-1;
		
		Double[][] subTableau = new Double[originTableau.length][originTableau[0].length - yCount];
		List<Double[]> coefficientsY = new ArrayList<>();
		
		// create yRestrictions with empty values
		for (int i = 0; i < restrictionCNT; i++) {
			Double[] yRestriction = new Double[yCount];
			coefficientsY.add(yRestriction);
		}
		
		// split x and y coefficients, x into tableau, y into list
		if (restrictionCNT > 0) {
			int yColCount = 0;
			int xColCount = 0;
			for (int col = 0; col < originTableau[0].length; col++) {
				if(col == originTableau[0].length-1) {
					// column is b result
					for (int row = 0; row < restrictionCNT; row++) {
						subTableau[row][subTableau[0].length-1] = originTableau[row][col];
					}
				} else if(arrayContains(yIndices, col)) {
					// column is y coefficient
					for (int row = 0; row < restrictionCNT; row++) {
						coefficientsY.get(row)[yColCount] = -1 * originTableau[row][col];
					}
					yColCount++;
				} else {
					// column is x coefficient
					for (int row = 0; row < restrictionCNT; row++) {
						subTableau[row][xColCount] = originTableau[row][col];
					}
					xColCount++;
				}
			}
		}
		
		// function x coefficients
		Double[] function = LinearOptimizationDataUtility.extractFunction(bendersOptimizationData.getSimplexTableau());		// function y coefficients
		int xColCount = 0;
		for (int col = 0; col < function.length - 1; col++) {
			if(!arrayContains(yIndices, col)) {
				subTableau[originTableau.length - 1][xColCount] = function[col];
				xColCount++;
			}
		}
		
		// constant of function, always 0
		subTableau[originTableau.length - 1][originTableau.length - yCount] = 0d;
		
		return new SubProblem(subTableau, coefficientsY);
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

	private Problem getOriginSubWithY(Problem sProblem, Double[] optimalY) {
		//TODO implement origin sub should already be accessible..
		Problem originSubWithY = new Problem(new Double[][] {{200d, 50d, 80d, 500d, 0d}});
		final int numOfRestrictions = 4;
		
		Double[][] restrictions = new Double[numOfRestrictions][];
		
		//add restrictions
		restrictions[0] = new Double[]{-1d, -1d, 0d, 0d, null};
		restrictions[1] = new Double[]{-2d, 0d, 0d, -0d, null};
		restrictions[2] = new Double[]{0d, 0d, -1d, -3d, null};
		restrictions[3] = new Double[]{0d, 0d, 0d, -10d, null};
		
		//calculate values for b
		for (int i = 0; i < numOfRestrictions; i++) {
			double result = sProblem.getB()[i];
			for (int j = 0; j < sProblem.getCoefficients()[i].length; j++) {
				result -= sProblem.getCoefficients()[i][j] * optimalY[j];
			}
			
			if (Math.abs(result) < DOUBLE_CORRECTION) {
				result = 0d;
			}
			restrictions[i][restrictions[i].length - 1] = result;
		}
		
		for (Double[] restriction : restrictions) {
			originSubWithY.addRestriction(restriction, 0d); // TODO Martin: Zielfunktionswert b wegspeichern 
		}
		
		return originSubWithY;
	}

	private Double[] extractSolutionCoefficients(Double[] simplexSolution) {
		Double[] u = new Double[simplexSolution.length - 2]; //solution and solution type not needed
		System.arraycopy(simplexSolution, 0, u, 0, simplexSolution.length - 2);
		return u;
	}
	
	private Double calculateLowerBound(MasterProblem mProblem, Double[] y, Double wy) {
		Double lowerBound = wy;
		for (int i = 0; i < y.length - 1; i++) { //exclude the theta
			lowerBound -= mProblem.getFunction()[i] * y[i];
		}
		
		return lowerBound;
	}

	private Double[] calculateCut(Problem sProblem, Double[] u) {
		//TODO clarification: is it possible to use double instead of Double??
		Double[] cut = new Double[sProblem.getCoefficients()[0].length + 2];
		for (int i = 0; i < cut.length; i++) {
			cut[i] = 0d;
		}
		
		for (int i = 0; i < u.length; i++) {
			for (int j = 0; j < sProblem.getCoefficients()[i].length; j++) {
				cut[j] += sProblem.getCoefficients()[i][j] * u[i]; //convert >= to <=
			}
			cut[sProblem.getCoefficients()[i].length + 1] += sProblem.getB()[i] * u[i];
		}
		
		cut[cut.length - 2] = -1d; //coefficient for theta
		
		return cut;
	}

	private void updateSubproblem(Problem sProblem, Double[] y) {
		Double[] function = new Double[sProblem.getFunction().length];
		
		for (int i = 0; i < function.length - 1; i++) {
			double ayMult = 0d;
			for (int j = 0; j < sProblem.getCoefficients()[i].length; j++) {
				ayMult = sProblem.getCoefficients()[i][j] * y[j];
			}
			function[i] = sProblem.getB()[i] - ayMult;
		}
		
		function[function.length - 1] = sProblem.getFunction()[function.length - 1]; //set constant to function
		
		sProblem.setFunction(function);
	}
	
	public static void main(String[] args) {
//		new BendersAlgorithm().solve(null);
		new BendersAlgorithm().testMasterSubSplit();
	}
	
	public void testMasterSubSplit() {
		Double[][] simplexTableau = {
				{-1d, -1d, 0d, 0d, -2d, -10d},
				{-2d, 0d, 0d, 0d, -2d, -10d},
				{0d, 0d, -1d, -3d, -0.5d, -2d},
				{0d, 0d, 0d, -10d, -1d, -6d},
				{200d, 50d, 80d, 500d, 180d, 0d},};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binaer};
		// TODO variable index starting from 0 to length-1
		int[] yVariableIndices = {4};
		BendersOptimizationData testBenders = new BendersOptimizationData(simplexTableau, yVariableIndices , yTypes);
		
		System.out.println("Input SimplexTableau");
		System.out.println(Arrays.deepToString(simplexTableau));
		
		System.out.println("\nMasterProblem");
		MasterProblem mb = createMasterProblem(testBenders);
		System.out.println("Tableau: " + Arrays.deepToString(mb.getSimplexTableau()));
//		System.out.println("F: " + Arrays.toString(mb.getFunction()));
		
		SubProblem sb = createSubProblem(testBenders);
		System.out.println("\nSubpProblem");
		System.out.println("Tableau: " + Arrays.deepToString(sb.getSimplexTableau()));
//		System.out.println("F: " + Arrays.toString(sb.getFunction()));
//		System.out.println("X: " + Arrays.deepToString(sb.getCoefficients()));
		System.out.println("Y: " + Arrays.deepToString(sb.getCoefficientsY()));
//		System.out.println("b: " + Arrays.toString(sb.getB()));
		
		Problem dp = new Problem(LinearOptimizationDataUtility.createDual(sb.getSimplexTableau()));
		System.out.println("\nDual Problem");
		System.out.println("Tableau: " + Arrays.deepToString(dp.getSimplexTableau()));
	}
}