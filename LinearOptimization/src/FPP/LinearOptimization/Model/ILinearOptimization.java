package FPP.LinearOptimization.Model;

import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;

public interface ILinearOptimization {

	ILinearOptimizationSolutionData solve(LinearOptimizationData linearOptimizationData);

}
