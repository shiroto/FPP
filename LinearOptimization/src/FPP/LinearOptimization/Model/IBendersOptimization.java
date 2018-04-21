package FPP.LinearOptimization.Model;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;

public interface IBendersOptimization {
	
	IBendersOptimizationSolutionData solve(BendersOptimizationData bendersOptimizationData);

}