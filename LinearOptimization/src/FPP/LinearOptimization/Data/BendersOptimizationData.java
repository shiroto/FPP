package FPP.LinearOptimization.Data;

import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;

public class BendersOptimizationData extends LinearOptimizationData {
	
	private int[] yVariableIndices;
	private BendersMasterCoefficientType[] yTypes;
	
	/**
	 * Initializes input data for bender algorithm.
	 * 
	 * Simplex tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 2d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 0d}} <b>function</b>
	 * Last column of restrictions is result
	 * Last column of function is constant
	 * All restrictions have to be <=
	 * Function must be minimizer
	 * 
	 * @param simplexTableau
	 * @param yVariables
	 * @param yTypes
	 */
	public BendersOptimizationData(Double[][] simplexTableau, 
			int[] yVariableIndices, BendersMasterCoefficientType[] yTypes) {
		super(simplexTableau);
		this.yVariableIndices = yVariableIndices;
		this.yTypes = yTypes;
	}

	public BendersMasterCoefficientType[] getYTypes() {
		return yTypes;
	}

	public void setYTypes(BendersMasterCoefficientType[] yTypes) {
		this.yTypes = yTypes;
	}

	public int[] getYVariableIndices() {
		return yVariableIndices;
	}

	public void setYVariableIndices(int[] yVariableIndices) {
		this.yVariableIndices = yVariableIndices;
	}

}
