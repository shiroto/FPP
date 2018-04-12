package FPP.LinearOptimization.Model.benders;

import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.StepData;

public class BendersSolutionData implements ILinearOptimizationSolutionData {

	public LinearOptimizationData linearOptimizationData;
	public StepData[] steps;
	public double[] solutionValues;

}
