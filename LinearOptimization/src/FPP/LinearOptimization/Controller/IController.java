package FPP.LinearOptimization.Controller;

import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Data.LinearOptimizationData;

public interface IController {

	void createAlgorithm(LinearOptimizationData problemData, Algorithm algorithm);

}
