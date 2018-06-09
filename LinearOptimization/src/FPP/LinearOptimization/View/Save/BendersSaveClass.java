package FPP.LinearOptimization.View.Save;

import java.io.Serializable;

import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;

public class BendersSaveClass implements SaveableIF, Serializable {

	private Double[][] array;
	private int numVar;
	private int numRest;
	private int[] yVariables;
	private BendersMasterCoefficientType[] bendersMasterCoefficientType;
	private int[] parameterNegativeIndices;
	private Double[] function;

	@Override
	public void setArray(Double[][] array) {
		this.array = array;
		
	}

	@Override
	public Double[][] getArray() {
		return array;
	}

	@Override
	public void setNumVar(int numVar) {
		this.numVar = numVar;
		
	}

	@Override
	public void setNumRestr(int numRestr) {
		this.numRest = numRestr;
		
	}

	@Override
	public int getNumVar() {
		return numVar;
		
	}

	@Override
	public int getNumRestr() {
		return numRest;
		
	}

	public void setYVariableIndices(int[] yVariables) {
		this.yVariables = yVariables;
		
	}

	public void setBendersMasterCoefficientTypes(BendersMasterCoefficientType[] bendersMasterCoefficientType) {
		this.bendersMasterCoefficientType = bendersMasterCoefficientType;
		
	}

	public void setParamNegIndices(int[] parameterNegativeIndices) {
		this.parameterNegativeIndices = parameterNegativeIndices;
	}

	@Override
	public Double[] getFunction() {
		return this.function;
	}

	@Override
	public void setFunction(Double[] function) {
		this.function = function;
		
	}

	public int getNumRest() {
		return numRest;
	}

	public void setNumRest(int numRest) {
		this.numRest = numRest;
	}

	public int[] getyVariables() {
		return yVariables;
	}

	public void setyVariables(int[] yVariables) {
		this.yVariables = yVariables;
	}

	public BendersMasterCoefficientType[] getBendersMasterCoefficientType() {
		return bendersMasterCoefficientType;
	}

	public void setBendersMasterCoefficientType(BendersMasterCoefficientType[] bendersMasterCoefficientType) {
		this.bendersMasterCoefficientType = bendersMasterCoefficientType;
	}

	public int[] getParameterNegativeIndices() {
		return parameterNegativeIndices;
	}

	public void setParameterNegativeIndices(int[] parameterNegativeIndices) {
		this.parameterNegativeIndices = parameterNegativeIndices;
	}

}
