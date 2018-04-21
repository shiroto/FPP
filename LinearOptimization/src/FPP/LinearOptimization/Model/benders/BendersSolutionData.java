package FPP.LinearOptimization.Model.benders;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Model.StepData;

public class BendersSolutionData implements IBendersOptimizationSolutionData {

	public BendersOptimizationData bendersOptimizationData;
	public StepData[] steps;
	public double[] solutionValues;

}
