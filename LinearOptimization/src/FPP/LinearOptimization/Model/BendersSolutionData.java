package FPP.LinearOptimization.Model;

import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;

public class BendersSolutionData implements ILinearOptimizationSolutionData {

	public LinearOptimizationData linearOptimizationData;
	public StepData[] steps;
	public double[] solutionValues;

}
