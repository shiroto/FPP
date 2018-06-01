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
	private static final Double ADDITIONAL_CUT_MIN_CORRECTION = 9999d;
	
	@Override
	public IBendersOptimizationSolutionData solve(BendersOptimizationData bendersOptimizationData) {
		// initialize the solution object
		BendersSolutionData bendersSolution = new BendersSolutionData(bendersOptimizationData);

		// convert to not-negatives
		bendersOptimizationData.setSimplexTableau(LinearOptimizationDataUtility.convertNotNegatives(
				bendersOptimizationData.getSimplexTableau(), 
				bendersOptimizationData.getParamaterNegativeIndices()));
		
		MasterProblem masterProblem = createMasterProblem(bendersOptimizationData);
		
		SubProblem subProblem = createSubProblem(bendersOptimizationData);
		Problem dualProblem = new Problem(LinearOptimizationDataUtility.createDual(subProblem.getSimplexTableau()));
		System.out.println("Master: " + Arrays.deepToString(masterProblem.getSimplexTableau()));
		System.out.println("Sub:    " + Arrays.deepToString(subProblem.getSimplexTableau()));
		System.out.println("Dual:   " + Arrays.deepToString(dualProblem.getSimplexTableau()));
		
		// Step 0
		int r = 1;
		Double UB = Double.MAX_VALUE;
		Double LB = -Double.MAX_VALUE;

		Double[] u = new Double[dualProblem.getFunction().length - 1];
		Arrays.fill(u, 0d);
		
		Double[] solution;
		Double[] optimalY = null;
		
		int yCount = bendersOptimizationData.getYVariableIndices().length;
		Double[] yZeroes = new Double[dualProblem.getFunction().length - 1];
		Arrays.fill(yZeroes, 0d);
		
		Double[] cut;
		try {
			stepZeroCut(masterProblem, subProblem, dualProblem, yZeroes);
		} catch (Exception e) {
			cut = calculateAdditionalCut(dualProblem);
			System.out.println("Step 0: not solvable");
			addCut(dualProblem, cut);
			System.out.println(" -> Add additional Cut " + Arrays.toString(cut));
			try {
				stepZeroCut(masterProblem, subProblem, dualProblem, yZeroes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		while (true) {
			BendersStepData stepData = new BendersStepData(r);
			
			// step 1
			if (masterProblem.isSolvableWithBAndB()) {
				Double[][] masterTableau = LinearOptimizationDataUtility.splitTheta(masterProblem.getSimplexTableau());
				solution = solveProblem(masterTableau, true, true);
				solution = LinearOptimizationDataUtility.normalizeTheta(solution);			
			} else {
				Double[][] masterTableau = masterProblem.getSimplexTableau();
				for(Double[] row : masterTableau) {
					System.out.println(Arrays.toString(row));
				}
				solution = solveProblem(masterProblem.getSimplexTableau(), false, true);
				System.out.println("Solution: " + Arrays.toString(solution));
			}
			
			//check for valid solution
			//if (solution[solution.length - 1] == 0) {
				//TODO what should happen here?
				//no solution existing
			//	break;
			//}
			stepData.setMasterSolution(solution);
			
			Double newUB;
			if (masterProblem.isSolvableWithBAndB()) {
				newUB = solution[solution.length - 1];
			} else {
				newUB = solution[solution.length - 2];
			}
			
			if (newUB.isNaN()) {
				UB = Double.MAX_VALUE;
			} else if (newUB < UB){
				UB = newUB;
			}
			
			Double[] y = extractSolutionCoefficients(solution, masterProblem.isSolvableWithBAndB());
			
			// Step 2
			updateSubproblem(subProblem, dualProblem, y);
			stepData.setSubProblem(subProblem.getSimplexTableau());
			
			solution = solveProblem(dualProblem.getSimplexTableau(),
					dualProblem.isSolvableWithBAndB(), false);
			stepData.setSubSolution(solution);
			
			u = extractSolutionCoefficients(solution, dualProblem.isSolvableWithBAndB());
			
			Double newLB = calculateLowerBound(masterProblem, y, solution[solution.length - 2]);
			if (newLB >= LB || LB.isNaN()) {
				LB = newLB;
			}
			
			// Step 3 
			//add output
			System.out.println("r = " + r + "\t LB = " + LB + "\t UB = " + UB);
			stepData.setLowerBound(LB);
			stepData.setUpperBound(UB);
			
			if (LB + DOUBLE_CORRECTION >= UB) {
				optimalY = y;
				break;
			}
			
			if (solution[solution.length - 2].equals(Double.NaN)) {
				cut = calculateAdditionalCut(dualProblem);
				System.out.println("not solvable");
				addCut(dualProblem, cut);
			} else {
				cut = calculateCut(subProblem, u);
				System.out.println("Add Cut " + Arrays.toString(cut) + "\n\n");
				addCut(masterProblem, cut);
			}
			
			stepData.setMasterProblem(masterProblem.getSimplexTableau());
			bendersSolution.addStep(stepData);
			r++;
		}
		
		if (optimalY != null ) {
			Problem originSubWithY = getOriginSubWithY(subProblem, optimalY);
			solution = new Simplex(originSubWithY.getSimplexTableau(), false).loese();
			
			Double[] masterFunction = LinearOptimizationDataUtility.extractFunction(masterProblem.getSimplexTableau());
			Double[] subFunction = LinearOptimizationDataUtility.extractFunction(subProblem.getSimplexTableau());
			Double[] optimalSolution = buildOptimalSolution(bendersOptimizationData, solution, optimalY, masterFunction,
					subFunction);
			bendersSolution.setOptSolution(optimalSolution);

			u = extractSolutionCoefficients(solution, originSubWithY.isSolvableWithBAndB());
			
			Double[] optSolution = bendersSolution.getOptSolution();
			System.out.println("\n\nOptimal Solution = " + optSolution[optSolution.length - 1]);
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

	private void stepZeroCut(MasterProblem masterProblem, SubProblem subProblem, Problem dualProblem,
			Double[] yZeroes) throws Exception {
		updateSubproblem(subProblem, dualProblem, yZeroes);
		Double[] solution = solveProblem(dualProblem.getSimplexTableau(), dualProblem.isSolvableWithBAndB(), false);
		for(Double value : solution) {
			if(value.isNaN()) {
				throw new Exception("Not solvable!");
			}
		}
		Double[] u = extractSolutionCoefficients(solution, dualProblem.isSolvableWithBAndB());
		addCut(masterProblem, calculateCut(subProblem, u));
	}

	private Double[] buildOptimalSolution(BendersOptimizationData bendersOptimizationData, Double[] solution,
			Double[] optimalY, Double[] functionMasterY, Double[] functionSubX) {
		Double[] optimalSolution = new Double[solution.length -1 + optimalY.length - 1];
		Double optimalFunctionValue = 0d;
		
		// Y-variables
		for (int i = 0; i < optimalY.length - 1; i++) {
			optimalSolution[bendersOptimizationData.getYVariableIndices()[i]] = optimalY[i];
			optimalFunctionValue += optimalY[i] * functionMasterY[i];
		}
		
		// X-variables
		for (int i = 0, j = 0; i < solution.length - 2; j++) {
			if (optimalSolution[j] == null) {
				optimalSolution[j] = solution[i];
				optimalFunctionValue += solution[i] * functionSubX[i];
				i++;
			}
		}
		
//		optimalSolution[optimalSolution.length - 1] = (-1) * solution[solution.length - 2];
		optimalSolution[optimalSolution.length - 1] = optimalFunctionValue;
		return optimalSolution;
	}
	
	private Double[] calculateAdditionalCut(Problem problem) {
		double functionValue = Math.abs(problem.getFunction()[0]);
		for (int i = 1; i < problem.getFunction().length - 1; i++) {
			functionValue *= Math.abs(problem.getFunction()[i]);
		}

		Double[] cut = new Double[problem.getCoefficients()[0].length + 1];
		Arrays.fill(cut, 1d);
//		cut[cut.length - 1] = functionValue + ADDITIONAL_CUT_MIN_CORRECTION;
		cut[cut.length - 1] = ADDITIONAL_CUT_MIN_CORRECTION;
		return cut;
	}

	private Double[] solveProblem(Double[][] simplexTableau,
			boolean solveAbleWithBAndB, boolean max) {
		if (solveAbleWithBAndB) {
			Tableau tableau = new Tableau(simplexTableau);
			
			List<Double[]> solution = new BranchAndBound(tableau, max).solve();
			if (solution.size() > 0) {
				return solution.get(0);
			}
			//TODO what happens if no solution can be found?
			return new Double[0];
		} else {
			return new Simplex(simplexTableau, max).loese();
		}
	}
	
	private void addCut(Problem problem, Double[] cut) {
		Double[] coefficients = Arrays.copyOf(cut, cut.length - 1);
		Double functionValue = cut[cut.length - 1];
		
		problem.addRestriction(coefficients, functionValue);
	}

	private MasterProblem createMasterProblem(BendersOptimizationData bendersOptimizationData) {
		Double[][] originTableau = bendersOptimizationData.getSimplexTableau();
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;
		
		Double[] function = LinearOptimizationDataUtility.extractFunction(originTableau);
		Double[][] masterTableau = new Double[1][yCount + 2];

		// theta
		masterTableau[0][yCount] = -1d;
		// constant of function
		masterTableau[0][yCount + 1] = function[function.length - 1];
		
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
		
		// add restrictions for binary values
		// e.g. 1 * y3 <= 1
		for (int i = 0; i < yIndices.length; i++) {
			if (bendersOptimizationData.getYTypes()[i] == BendersMasterCoefficientType.Binary) {				
				Double[] coefficients = new Double[yCount + 1];
				Arrays.fill(coefficients, 0d);
				coefficients[i] = 1d;
				
				mp.addRestriction(coefficients, 1d);
			}
		}
		
		// add restrictions to master from original problem that only contain y coefficients
		int restrictionCNT = originTableau.length - 1;
		for (int restr = 0; restr < restrictionCNT; restr++) {
			if(restrictionContainsOnlyY(originTableau[restr], yIndices)) {
				Double[] coefficients = new Double[yCount + 1];
				Arrays.fill(coefficients, 0d);
				for (int yIndex = 0; yIndex < coefficients.length - 1; yIndex++) {
					System.out.println("yIndex " + yIndex);
					System.out.println("yIndices[yIndex] " + yIndices[yIndex]);
					System.out.println("originTableau[restr][yIndices[yIndex]] " + originTableau[restr][yIndices[yIndex]]);
					System.out.println("coefficients[yIndex] " + coefficients[yIndex]);
					coefficients[yIndex] = originTableau[restr][yIndices[yIndex]];
				}
				mp.addRestriction(coefficients, originTableau[restr][originTableau[0].length - 1]);
			}
		}
		
		return mp;
	}
	
	private boolean restrictionContainsOnlyY(Double[] restriction, int[] yIndices) {
		for (int coefIndex = 0; coefIndex < restriction.length - 1; coefIndex++) {
			if(!LinearOptimizationDataUtility.arrayContains(yIndices, coefIndex)) {
				if(restriction[coefIndex] != 0d) {
					return false;
				}
			}
		}
		return true;
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
		Arrays.fill(cut, 0d);
		
		for (int i = 0; i < u.length; i++) {
			for (int j = 0; j < sProblem.getCoefficientsY()[i].length; j++) {
				cut[j] -= sProblem.getCoefficientsY()[i][j] * u[i]; //convert >= to <=
			}
			cut[sProblem.getCoefficientsY()[i].length + 1] += sProblem.getB()[i] * u[i];
		}
		
		cut[cut.length - 2] = 1d; //coefficient for theta
		
		return cut;
	}

	private void updateSubproblem(SubProblem subProblem, Problem dualProblem, Double[] y) {
		Double[] function = new Double[dualProblem.getFunction().length];
		
		for (int i = 0; i < function.length - 1; i++) {
			double ayMult = 0d;
			for (int j = 0; j < subProblem.getCoefficientsY()[i].length; j++) {
				ayMult += subProblem.getCoefficientsY()[i][j] * y[j];
			}
			function[i] = subProblem.getB()[i] + ayMult;
		}
		
		function[function.length - 1] = dualProblem.getFunction()[function.length - 1]; //set constant to function
		
		dualProblem.setFunction(function);
	}
}