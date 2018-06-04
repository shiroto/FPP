package FPP.LinearOptimization.Controller;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.ILinearOptimization;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;

public class BendersAlgorithmAdapter implements ILinearOptimization{

	@Override
	public ILinearOptimizationSolutionData solve(LinearOptimizationData linearOptimizationData) {
		BendersAlgorithm impl = new BendersAlgorithm();
		return impl.solve((BendersOptimizationData) linearOptimizationData);
	}

}
