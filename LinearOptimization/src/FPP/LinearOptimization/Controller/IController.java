package FPP.LinearOptimization.Controller;

import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;

public interface IController {

	ILinearOptimizationSolutionData createAlgorithm(LinearOptimizationData problemData, Algorithm algorithm);

}
