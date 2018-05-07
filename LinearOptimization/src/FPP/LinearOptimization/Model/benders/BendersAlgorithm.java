package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationDataUtility;
import FPP.LinearOptimization.Model.IBendersOptimization;
import de.bb.labor.BranchAndBound;
import de.bb.labor.Tableau;
import de.lip.bb.Simplex;

public class BendersAlgorithm implements IBendersOptimization {

	private static final Double DOUBLE_CORRECTION = 0.000001d;

	@Override
	public IBendersOptimizationSolutionData solve(BendersOptimizationData bendersOptimizationData) {
		// initialize the solution object
		BendersSolutionData bendersSolution = new BendersSolutionData(bendersOptimizationData);

		// convert to not-negatives
		bendersOptimizationData.setSimplexTableau(LinearOptimizationDataUtility.convertNotNegatives(
				bendersOptimizationData.getSimplexTableau(), 
				bendersOptimizationData.getParamaterNegativeIndices()));
		
		MasterProblem masterProblem = createMasterProblem(bendersOptimizationData);
		// TODO Dominik: ???
//		masterProblem.addRestriction(new Double[]{-1d, 0d}, 0d); // add y >= 0 condition

		SubProblem subProblem = createSubProblem(bendersOptimizationData);
		Problem dualProblem = new Problem(LinearOptimizationDataUtility.createDual(subProblem.getSimplexTableau()));
		System.out.println("Master: " + Arrays.deepToString(masterProblem.getSimplexTableau()));
		System.out.println("Sub:    " + Arrays.deepToString(subProblem.getSimplexTableau()));
		System.out.println("Dual:   " + Arrays.deepToString(dualProblem.getSimplexTableau()));
		
		// step 0
		int r = 1;
		Double UB = Double.MAX_VALUE;
		Double LB = Double.MIN_VALUE;

		Double[] u = new Double[dualProblem.getFunction().length - 1];
		for (int i = 0; i < u.length; i++) {
			u[i] = 0d;
		}
		
		Double[] cut = calculateCut(subProblem, u);
		addCut(masterProblem, cut);
		
		Double[] solution;
		Double[] optimalY = null;
		while (true) {
			BendersStepData stepData = new BendersStepData(r);
			
			//TODO Dominik: simplextableau sind doch immer minimierer -> solveProblem(masterProblem, true); -> false?
			
			// step 1
			solution = solveProblem(masterProblem, true);
			
			//check for valid solution
			//if (solution[solution.length - 1] == 0) {
				//TODO what should happen here?
				//no solution existing
			//	break;
			//}
			stepData.setMasterSolution(solution);
			
			if (masterProblem.isSolvableWithBAndB()) {
				UB = solution[solution.length - 1];
			} else {
				UB = solution[solution.length - 2];
			}
			
			Double[] y = extractSolutionCoefficients(solution, masterProblem.isSolvableWithBAndB());
			
			// step 2
			updateSubproblem(subProblem, dualProblem, y);
			stepData.setSubProblem(subProblem.getSimplexTableau());
			
			solution = solveProblem(dualProblem, false);
			stepData.setSubSolution(solution);
			
			u = extractSolutionCoefficients(solution, dualProblem.isSolvableWithBAndB());
			
			// step 3 
			LB = calculateLowerBound(masterProblem, y, solution[solution.length - 2]);

			//add output
			System.out.println("r = " + r + "\t LB = " + LB + "\t UB = " + UB);
			stepData.setLowerBound(LB);
			stepData.setUpperBound(UB);
			
			if (LB + DOUBLE_CORRECTION >= UB) {
				optimalY = y;
				break;
			}
			
			cut = calculateCut(subProblem, u);
			System.out.println("Add Cut " + Arrays.toString(cut) + "\n\n");
			addCut(masterProblem, cut);
			
			stepData.setMasterProblem(masterProblem.getSimplexTableau());
			bendersSolution.addStep(stepData);
			r++;
		}
		
		if (optimalY != null ) {
			Problem originSubWithY = getOriginSubWithY(subProblem, optimalY);
			solution = new Simplex(originSubWithY.getSimplexTableau(), false).loese();
			bendersSolution.setOptSolution(solution);

			u = extractSolutionCoefficients(solution, originSubWithY.isSolvableWithBAndB());
			
			System.out.println("\n\nOptimal Solution = " + solution[solution.length - 2] * (-1));
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
		return bendersSolution;
	}
	
	private Double[] solveProblem(Problem problem, boolean max) {
		if (problem.isSolvableWithBAndB()) {
			Tableau tableau = new Tableau(problem.getSimplexTableau());
			
			List<Double[]> solution = new BranchAndBound(tableau, max).solve();
			if (solution.size() > 0) {
				return solution.get(0);
			}
			//TODO what happens if no solution can be found?
			return new Double[0];
		} else {
			return new Simplex(problem.getSimplexTableau(), max).loese();
		}
	}
	
	private void addCut(MasterProblem masterProblem, Double[] cut) {
		Double[] coefficients = Arrays.copyOf(cut, cut.length - 1);
		Double functionValue = cut[cut.length - 1];
		
		masterProblem.addRestriction(coefficients, functionValue);
	}

	private MasterProblem createMasterProblem(BendersOptimizationData bendersOptimizationData) {
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;
		Double[] function = LinearOptimizationDataUtility.extractFunction(bendersOptimizationData.getSimplexTableau());
		Double[][] masterTableau = new Double[1][yCount + 2];
		
		// constant of function
		masterTableau[0][yCount] = 1d;
		masterTableau[0][yCount + 1] = function[function.length - 1];
		
//		// TODO new try, constant not 1
//		Double[][] masterTableau = new Double[1][yCount + 1];
//		
//		// constant of function
//		masterTableau[0][yCount] = function[function.length - 1];
		
		
		
		// function y coefficients
		int yColCount = 0;
		for (int col = 0; col < function.length - 1; col++) {
			if(LinearOptimizationDataUtility.arrayContains(yIndices, col)) {
				masterTableau[0][yColCount] = function[col];
				yColCount++;
			}
		}
		
		MasterProblem mp = new MasterProblem(masterTableau);
		mp.setTypes(bendersOptimizationData.getYTypes());
		return mp;
	}
	
	private SubProblem createSubProblem(BendersOptimizationData bendersOptimizationData) {
		Double[][] originTableau = bendersOptimizationData.getSimplexTableau();
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;
		int restrictionCNT = originTableau.length - 1;
		
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
				} else if(LinearOptimizationDataUtility.arrayContains(yIndices, col)) {
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
			if(!LinearOptimizationDataUtility.arrayContains(yIndices, col)) {
				subTableau[originTableau.length - 1][xColCount] = function[col];
				xColCount++;
			}
		}
		
		// constant of function, always 0
		subTableau[originTableau.length - 1][originTableau[0].length - yCount - 1] = 0d;
		
		return new SubProblem(subTableau, coefficientsY);
	}

	private Problem getOriginSubWithY(SubProblem sProblem, Double[] optimalY) {
		Double[][] originSubWithY = sProblem.getSimplexTableau();
		
		//calculate values for b
		for (int i = 0; i < originSubWithY.length - 1; i++) {
			double result = sProblem.getB()[i];
			for (int j = 0; j < sProblem.getCoefficientsY()[i].length; j++) {
				result += sProblem.getCoefficientsY()[i][j] * optimalY[j];
			}
			
			if (Math.abs(result) < DOUBLE_CORRECTION) {
				result = 0d;
			}
			originSubWithY[i][originSubWithY[i].length - 1] = result;
		}
		
		return new Problem(originSubWithY);
	}

	private Double[] extractSolutionCoefficients(Double[] simplexSolution, boolean bAndB) {
		int length;
		if (bAndB) {
			length = simplexSolution.length - 1;
		} else {
			length = simplexSolution.length - 2;
		}
		Double[] u = new Double[length]; //solution and solution type not needed
		System.arraycopy(simplexSolution, 0, u, 0, length);
		return u;
	}
	
	private Double calculateLowerBound(MasterProblem mProblem, Double[] y, Double wy) {
		Double lowerBound = wy;
		for (int i = 0; i < y.length - 1; i++) { //exclude the theta
			lowerBound -= mProblem.getFunction()[i] * y[i];
		}
		
		return lowerBound;
	}

	private Double[] calculateCut(SubProblem sProblem, Double[] u) {
		Double[] cut = new Double[sProblem.getCoefficientsY()[0].length + 2];
		for (int i = 0; i < cut.length; i++) {
			cut[i] = 0d;
		}
		
		for (int i = 0; i < u.length; i++) {
			for (int j = 0; j < sProblem.getCoefficientsY()[i].length; j++) {
				cut[j] -= sProblem.getCoefficientsY()[i][j] * u[i]; //convert >= to <=
			}
			cut[sProblem.getCoefficientsY()[i].length + 1] += sProblem.getB()[i] * u[i];
		}
		
		cut[cut.length - 2] = -1d; //coefficient for theta
		
		return cut;
	}

	private void updateSubproblem(SubProblem subProblem, Problem dualProblem, Double[] y) {
		Double[] function = new Double[dualProblem.getFunction().length];
		
		for (int i = 0; i < function.length - 1; i++) {
			double ayMult = 0d;
			for (int j = 0; j < subProblem.getCoefficientsY()[i].length; j++) {
				ayMult = subProblem.getCoefficientsY()[i][j] * y[j];
			}
			function[i] = subProblem.getB()[i] + ayMult;
		}
		
		function[function.length - 1] = dualProblem.getFunction()[function.length - 1]; //set constant to function
		
		dualProblem.setFunction(function);
	}
	
	public static void main(String[] args) {
		Double[][] simplexTableau = {
				{-1d, -1d, 0d, 0d, -2d, -10d},
				{-2d, 0d, 0d, 0d, -2d, -10d},
				{0d, 0d, -1d, -3d, -0.5d, -2d},
				{0d, 0d, 0d, -10d, -1d, -6d},
				{200d, 50d, 80d, 500d, 180d, 0d},};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Float};
		
		// TODO variable index starting from 0 to length-1
		int[] yVariableIndices = {4};
		// TODO Dominik: specify paramaterNegativeIndices
		int[] paramaterNegativeIndices = {};
		BendersOptimizationData testBenders = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		//
		new BendersAlgorithm().solve(testBenders);
//		new BendersAlgorithm().testMasterSubSplit();
	}
	
	public void testMasterSubSplit() {
		Double[][] simplexTableau = {
				{-1d, -1d, 0d, 0d, -2d, -10d},
				{-2d, 0d, 0d, 0d, -2d, -10d},
				{0d, 0d, -1d, -3d, -0.5d, -2d},
				{0d, 0d, 0d, -10d, -1d, -6d},
				{200d, 50d, 80d, 500d, 180d, 0d},};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary};
		// TODO variable index starting from 0 to length-1
		int[] yVariableIndices = {4};
		int[] paramaterNegativeIndices = {};
		// TODO Dominik: specify paramaterNegativeIndices
		BendersOptimizationData testBenders = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);

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