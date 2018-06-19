package FPP.LinearOptimization.Model.benders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Model.IBendersOptimization;
import de.bb.labor.BranchAndBound;
import de.bb.labor.Tableau;
import de.lip.bb.Simplex;

import static FPP.LinearOptimization.Data.LinearOptimizationDataUtility.*;

/**
 * Class for the benders algorithm implementing {@link IBendersOptimization}.
 */
public class BendersAlgorithm implements IBendersOptimization {

	private static final Double DOUBLE_CORRECTION = 0.000001d;
	private static final Double ADDITIONAL_CUT_MIN_CORRECTION = 9999d;

	protected static final String MASTER_PROBLEM_UNBOUNDED_MESSAGE = 
			"Das Masterproblem ist unbeschränkt, daher kann der Algorithmus keine Berechnung durchführen.";

	@Override
	public IBendersOptimizationSolutionData solve(BendersOptimizationData bendersOptimizationData) {
		// Initialize the solution object
		BendersSolutionData bendersSolution = new BendersSolutionData(bendersOptimizationData);

		// If we have a constant, we substitute it with 0 and add it to the optimal solution in the end
		Double constant = substituteConstant(bendersOptimizationData);

		// Convert all negative variables to not-negatives
		bendersOptimizationData.setSimplexTableau(convertNotNegatives(
				bendersOptimizationData.getSimplexTableau(), 
				bendersOptimizationData.getParamaterNegativeIndices()));

		MasterProblem masterProblem = createMasterProblem(bendersOptimizationData);

		SubProblem subProblem = createSubProblem(bendersOptimizationData);
		Problem dualProblem = new Problem(createDual(subProblem.getSimplexTableau()));
		//		System.out.println("Master: " + Arrays.deepToString(masterProblem.getSimplexTableau()));
		//		System.out.println("Sub:    " + Arrays.deepToString(subProblem.getSimplexTableau()));
		//		System.out.println("Dual:   " + Arrays.deepToString(dualProblem.getSimplexTableau()));

		// Step 0
		int r = 1;
		Double UB = Double.MAX_VALUE;
		Double LB = -Double.MAX_VALUE;

		// u-variables of the dual subproblem
		Double[] u = new Double[dualProblem.getFunction().length - 1];
		Arrays.fill(u, 0d);

		Double[] solution;
		Double[] optimalY = null;

		Double[] yZeroes = new Double[dualProblem.getFunction().length - 1];
		Arrays.fill(yZeroes, 0d);

		Double[] cut;
		try {
			stepZeroCut(masterProblem, subProblem, dualProblem, yZeroes);
		} catch (Exception e) {
			cut = calculateAdditionalCut(dualProblem, bendersOptimizationData.getSimplexTableau());
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
			// Step 1
			BendersStepData stepData = new BendersStepData(r);

			Double[][] masterTableau = splitTheta(masterProblem.getSimplexTableau());
			if (masterProblem.isSolvableWithBAndB()) {
				solution = solveProblem(masterTableau, true, true);
				solution = normalizeTheta(solution, false);
				stepData.setMasterSolution(solution);
			} else {
				solution = solveProblem(masterTableau, false, true);
				solution = normalizeTheta(solution, true);
				// return solution without add info
				stepData.setMasterSolution(extractSimplexSolution(solution));
			}

			Double newUB;
			if (masterProblem.isSolvableWithBAndB()) {
				newUB = solution[solution.length - 1];
			} else {
				newUB = solution[solution.length - 2];
			}

			if (newUB.isNaN()) {
				// MasterProblem is unbound
				bendersSolution.setAddInfo(MASTER_PROBLEM_UNBOUNDED_MESSAGE);
				return bendersSolution;
			} else if (newUB < UB){
				UB = newUB;
			}

			// Set y = yr
			Double[] y = extractSolutionCoefficients(solution, masterProblem.isSolvableWithBAndB());

			// Step 2
			updateSubproblem(subProblem, dualProblem, y);
			stepData.setSubProblem(subProblem.getSimplexTableau());

			solution = solveProblem(dualProblem.getSimplexTableau(),
					dualProblem.isSolvableWithBAndB(), false);
			if (dualProblem.isSolvableWithBAndB()) {
				stepData.setSubSolution(solution);
			} else {
				stepData.setSubSolution(extractSimplexSolution(solution));
			}

			u = extractSolutionCoefficients(solution, dualProblem.isSolvableWithBAndB());

			Double newLB = calculateLowerBound(masterProblem, y, solution[solution.length - 2]);
			if (newLB >= LB || LB.isNaN()) {
				LB = newLB;
			}

			System.out.println("r = " + r + "\t LB = " + LB + "\t UB = " + UB);
			stepData.setLowerBound(LB);
			stepData.setUpperBound(UB);

			// Step 3 
			if (LB + DOUBLE_CORRECTION >= UB) {
				optimalY = y;
				break;
			}

			if (solution[solution.length - 2].equals(Double.NaN)) {
				cut = calculateAdditionalCut(dualProblem, bendersOptimizationData.getSimplexTableau());
				System.out.println("Not solvable");
				addCut(dualProblem, cut);
			} else {
				cut = calculateCut(subProblem, u);
				System.out.println("Add Cut " + Arrays.toString(cut) + "\n");
				addCut(masterProblem, cut);
			}

			stepData.setMasterProblem(masterProblem.getSimplexTableau());
			bendersSolution.addStep(stepData);
			r++;
		}

		if (optimalY != null ) {
			Problem originSubWithY = getOriginSubWithY(subProblem, optimalY);
			solution = new Simplex(originSubWithY.getSimplexTableau(), false).loese();

			Double[] masterFunction = extractFunction(masterProblem.getSimplexTableau());
			Double[] subFunction = extractFunction(subProblem.getSimplexTableau());
			Double[] optimalSolution = buildOptimalSolution(bendersOptimizationData, solution, optimalY, masterFunction,
					subFunction, constant);
			bendersSolution.setOptSolution(optimalSolution);

			u = extractSolutionCoefficients(solution, originSubWithY.isSolvableWithBAndB());

			Double[] optSolution = bendersSolution.getOptSolution();
			System.out.println("\nOptimal Solution = " + optSolution[optSolution.length - 1]);
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

	/**
	 * This method substitutes the constant of the simplex tableau function with 0
	 * and returns that constant.<br>
	 * This is done because Simplex & BAndB can not handle that in the right way.<br><br>
	 * 
	 * If there is no constant in the function, {@link Double}.<code>NaN</code> is returned.<br>
	 * <b>ATTENTION: constant value has to be added to the optimal solution to get the right result!</b>
	 * 
	 * @param bendersOptimizationData
	 * @return constant of given function
	 */
	private Double substituteConstant(BendersOptimizationData bendersOptimizationData) {
		Double[][] simplexTableau = bendersOptimizationData.getSimplexTableau();
		if (simplexTableau[simplexTableau.length-1][simplexTableau[0].length-1] != 0d) {
			Double constant = simplexTableau[simplexTableau.length-1][simplexTableau[0].length-1];
			simplexTableau[simplexTableau.length-1][simplexTableau[0].length-1] = 0d;
			bendersOptimizationData.setSimplexTableau(simplexTableau);
			return constant;
		}
		return Double.NaN;
	}

	/**
	 * This method calculates the first cut in step 0, with all u-values set 
	 * to 0 and adds the cut to the {@link MasterProblem}.<br><br>
	 * 
	 * To set all u-values to 0 all function values are set to 0.<br>
	 * The SimplexTableau is then solved as usual.<br>
	 * 
	 * @param masterProblem {@link MasterProblem}
	 * @param subProblem {@link SubProblem}
	 * @param dualProblem DualProblem as {@link Problem}
	 * @param yZeroes array with 0-values
	 * @throws Exception if not solvable
	 */
	private void stepZeroCut(MasterProblem masterProblem, SubProblem subProblem, Problem dualProblem,
			Double[] yZeroes) throws Exception {

		Problem cutProblem = dualProblem;
		Double[] cutFunction = new Double[cutProblem.getFunction().length];
		Arrays.fill(cutFunction, 0d);
		cutProblem.setFunction(cutFunction);

		Double[] solution = solveProblem(cutProblem.getSimplexTableau(), cutProblem.isSolvableWithBAndB(), false);
		for(Double value : solution) {
			if(value.isNaN()) {
				throw new Exception("Not solvable!");
			}
		}
		Double[] u = extractSolutionCoefficients(solution, cutProblem.isSolvableWithBAndB());
		addCut(masterProblem, calculateCut(subProblem, u));
	}

	/**
	 * Builds the optimal solution found by the BendersAlgorithm.<br><br>
	 * 
	 * <b>Optimal solution format:</b><br>
	 * {4d, 4d, 0d, 0.5d, 1d, 1430d}<br>
	 * The last value represents optimal function value.<br>
	 * The rest represent optimal x and y values on their respective positions.<br>
	 * 
	 * @param bendersOptimizationData {@link BendersOptimizationData} object
	 * @param solution array of optimal SubProblem solution values (with inserted optimal y-values)
	 * @param optimalY array of final optimal y-values
	 * @param functionMasterY function of {@link MasterProblem}
	 * @param functionSubX function of {@link SubProblem}
	 * @param constant the constant of the initial input-function, 0 if there was none
	 * @return array of the optimal solutions
	 */
	private Double[] buildOptimalSolution(BendersOptimizationData bendersOptimizationData, Double[] solution,
			Double[] optimalY, Double[] functionMasterY, Double[] functionSubX, Double constant) {
		Double[] optimalSolution = new Double[solution.length -1 + optimalY.length - 1];
		// if function had a constant, we need to add it here again to return the correct result
		Double optimalFunctionValue = constant.isNaN() ? 0d : constant;

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

		optimalSolution[optimalSolution.length - 1] = optimalFunctionValue;
		return optimalSolution;
	}

	/**
	 * This method calculates and returns a cut serving as variable-boundaries 
	 * for usually not solvable or unbound tableaus.<br>
	 *  
	 * @param {@link Problem}
	 * @param initialTableau the tableau to add the cut to
	 * @return the cut as <code>Double[]</code>
	 */
	private Double[] calculateAdditionalCut(Problem problem, Double[][] initialTableau) {
		// multiply 2 highest values of the initial tableau for the additional cut
		double[] highestValues = findTwoHighestDistinctValues(mode(initialTableau));
		double functionValue = highestValues[0] * highestValues[1];

		for (int i = 1; i < problem.getFunction().length - 1; i++) {
			functionValue *= Math.abs(problem.getFunction()[i]);
		}

		Double[] cut = new Double[problem.getCoefficients()[0].length + 1];
		Arrays.fill(cut, 1d);
		cut[cut.length - 1] = functionValue + ADDITIONAL_CUT_MIN_CORRECTION;
		return cut;
	}

	/**
	 * Solves a SimplexTableau with Simplex or BranchAndBound.<br><br>
	 * 
	 * <b>Solution format:</b><br>
	 * {10d, 0d, 130d, 2d}<br>
	 * Last value represents the solution type.<br>
	 * Second to last represents optimal function value.<br>
	 * The rest represent variable values.<br>
	 * For more information see return of {@link de.lip.bb.Simplex#loese()}.<br>
	 * 
	 * @param simplexTableau
	 * @param solveAbleWithBAndB boolean if solvable with BAndB
	 * @param max <code>true</code> if maximize, <code>false</code> if minimize
	 * @return solution as <code>Double[]</code> 
	 */
	private Double[] solveProblem(Double[][] simplexTableau,
			boolean solveAbleWithBAndB, boolean max) {
		if (solveAbleWithBAndB) {
			Tableau tableau = new Tableau(simplexTableau);

			boolean[] flagInteger = initializeFlagInteger(simplexTableau);

			List<Double[]> solution = new BranchAndBound(tableau, max, flagInteger).solve();
			if (solution.size() > 0) {
				return solution.get(0);
			}
			return new Double[0];
		} else {
			return new Simplex(simplexTableau, max).loese();
		}
	}

	/**
	 * Creates a boolean array depending on the tableau-length with everything <code>true</code> 
	 * except the last two entries. <code>true</code> symbolizes that the value on this position 
	 * is an integer, <code>false</code> for float/double.<br>
	 * 
	 * @param simplexTableau
	 * @return <code>boolean[]</code>-array representing if value at position is boolean
	 */
	private boolean[] initializeFlagInteger(Double[][] simplexTableau) {
		boolean[] flagInteger = new boolean[simplexTableau[0].length];
		Arrays.fill(flagInteger, true);
		flagInteger[flagInteger.length - 1] = false;
		flagInteger[flagInteger.length - 2] = false;
		return flagInteger;
	}

	/**
	 * This functions adds the given cut to the given {@link Problem}.<br><br>
	 * 
	 * <b>Cut format:</b><br>
	 * {1d, 0d, 0d, 7.5d, 50d}<br>
	 * Last value represents the b column.<br>
	 * The rest represent coefficient multiplicators.<br>
	 * The restriction is a <= restriction.<br><br>
	 * 
	 * The given example would look like this:<br>
	 * 1*x1 + 0*x2 + 0*x3 + 7,5*x4 <= 50<br>
	 * 
	 * @param problem Any kind of {@link Problem}
	 * @param cut <code>Double[]</code> array representing a cut
	 */
	private void addCut(Problem problem, Double[] cut) {
		Double[] coefficients = Arrays.copyOf(cut, cut.length - 1);
		Double functionValue = cut[cut.length - 1];

		problem.addRestriction(coefficients, functionValue);
	}

	/**
	 * Calculates a {@link MasterProblem} from the given {@link BendersOptimizationData}-input data.<br>
	 * 
	 * @param bendersOptimizationData algorithm input data
	 * @return {@link MasterProblem} extracted from input
	 */
	private MasterProblem createMasterProblem(BendersOptimizationData bendersOptimizationData) {
		Double[][] originTableau = bendersOptimizationData.getSimplexTableau();
		int[] yIndices = bendersOptimizationData.getYVariableIndices();
		int yCount = yIndices.length;

		Double[] function = extractFunction(originTableau);
		Double[][] masterTableau = new Double[1][yCount + 2];

		// theta
		masterTableau[0][yCount] = -1d;
		// constant of function
		masterTableau[0][yCount + 1] = function[function.length - 1];

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
					//					System.out.println("yIndex " + yIndex);
					//					System.out.println("yIndices[yIndex] " + yIndices[yIndex]);
					//					System.out.println("originTableau[restr][yIndices[yIndex]] " + originTableau[restr][yIndices[yIndex]]);
					//					System.out.println("coefficients[yIndex] " + coefficients[yIndex]);
					coefficients[yIndex] = originTableau[restr][yIndices[yIndex]];
				}
				mp.addRestriction(coefficients, originTableau[restr][originTableau[0].length - 1]);
			}
		}

		return mp;
	}

	/**
	 * Returns <code>true</code> if a restrictions contains only y-values.<br><br>
	 * 
	 * <b>Restriction format:</b><br>
	 * {1d, 0d, 0d, 7.5d, 50d}<br>
	 * Last value represents the b column.<br>
	 * The rest represent coefficient multiplicators.<br>
	 * The restriction is a <= restriction.<br><br>
	 * 
	 * With y at position 1 the given example would look like this:<br>
	 * 1*y1 + 0*x2 + 0*x3 + 7,5*x4 <= 50<br>
	 * 
	 * @param restriction array representing a restriction
	 * @param yIndices array with y-positions of the given restriction
	 * @return <code>true</code> if only y in restriction
	 */
	private boolean restrictionContainsOnlyY(Double[] restriction, int[] yIndices) {
		for (int coefIndex = 0; coefIndex < restriction.length - 1; coefIndex++) {
			if(!arrayContains(yIndices, coefIndex)) {
				if(restriction[coefIndex] != 0d) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Calculates a {@link SubProblem} from the given {@link BendersOptimizationData}-input data.<br>
	 * 
	 * @param bendersOptimizationData algorithm input data
	 * @return {@link SubProblem} extracted from input
	 */
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
		Double[] function = extractFunction(bendersOptimizationData.getSimplexTableau());		// function y coefficients
		int xColCount = 0;
		for (int col = 0; col < function.length - 1; col++) {
			if(!arrayContains(yIndices, col)) {
				subTableau[originTableau.length - 1][xColCount] = function[col];
				xColCount++;
			}
		}

		// constant of function, always 0
		subTableau[originTableau.length - 1][originTableau[0].length - yCount - 1] = 0d;

		return new SubProblem(subTableau, coefficientsY);
	}

	/**
	 * Inserts optimal y-values into a given {@link Subproblem} and return 
	 * the solution as a new {@link Problem}. 
	 * 
	 * @param sProblem {@link Subproblem} to solve
	 * @param optimalY array with optimal y-values
	 * @return new {@link Problem} with inserted y-values
	 */
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

	/**
	 * Extracts and returns the coefficient-values from a Simplex or BranchAndBound solution.
	 * 
	 * @param simplexSolution array with Simplex or BAndB solution
	 * @param bAndB <code>true</code> if BAndB, <code>false</code> if Simplex
	 * @return array with solution coefficient-values
	 */
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

	/**
	 * Returns the lower bound of a {@link MasterProblem} with {@link SubProblem}
	 * -function-value wy and y-values.
	 * 
	 * @param mProblem {@link MasterProblem}
	 * @param y y-values
	 * @param wy solution of {@link SubProblem} function
	 * @return lower bound
	 */
	private Double calculateLowerBound(MasterProblem mProblem, Double[] y, Double wy) {
		Double lowerBound = wy;
		for (int i = 0; i < y.length - 1; i++) { //exclude the theta
			lowerBound -= mProblem.getFunction()[i] * y[i];
		}

		return lowerBound;
	}

	/**
	 * Calculates and returns a cut of a {@link SubProblem} by a given array of u-values.<br><br>
	 * 
	 * <b>Cut format:</b><br>
	 * {1d, 0d, 0d, 7.5d, 50d}<br>
	 * Last value represents the b column.<br>
	 * The rest represent coefficient multiplicators.<br>
	 * The restriction is a <= restriction.<br><br>
	 * 
	 * The given example would look like this:<br>
	 * 1*x1 + 0*x2 + 0*x3 + 7,5*x4 <= 50<br>
	 * 
	 * @param sProblem {@link SubProblem}
	 * @param u array of u-values
	 * @return cut array
	 */
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

	/**
	 * Inserts y-values into function of the DualProblem so that the u-values can be calculated later.
	 * 
	 * @param subProblem {@link SubProblem}
	 * @param dualProblem the DualProblem as {@link Problem}
	 * @param y array of y-values
	 */
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

	/**
	 * Finds and returns the two highest values of a double-list.
	 * 
	 * @param array double-list of values to search in
	 * @return array of the two highest values
	 */
	private double[] findTwoHighestDistinctValues(List<Double> array)
	{
		double max = Double.MIN_VALUE;
		double secondMax = -Double.MAX_VALUE;
		for (double value:array)
		{
			if (value > max)
			{
				secondMax = max;
				max = value;
			}
			else if (value > secondMax && value < max)
			{
				secondMax = value;
			}
		}
		return new double[] { max, secondMax };
	}

	/**
	 * Converts a 2 dimensional array to a list (1d array).
	 * 
	 * @param arr 2 dimensional array
	 * @return list of all values
	 */
	public List<Double> mode(Double[][] arr) {
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) { 
				list.add(arr[i][j]); 
			}
		}
		return list;
	}
}