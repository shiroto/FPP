package FPP.LinearOptimization.Model.benders;

import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.ILinearOptimization;
import de.lip.bb.Simplex;

public class BendersAlgorithm implements ILinearOptimization {

	@Override
	public ILinearOptimizationSolutionData solve(LinearOptimizationData linearOptimizationData) {
		// TODO convert linearOptimizationData to master and dual sub problem
		MasterProblem mProblem = new MasterProblem(new Double[] {-180d});
		// dual subproblem
		SubProblem sProblem = new SubProblem(new Double[] {0d, 0d, 0d, 0d, 0d});
		sProblem.addRestriction(new Double[]{-1d, -2d, 0d, 0d, -200d});
		sProblem.addRestriction(new Double[]{-1d, 0d, 0d, 0d, -50d});
		sProblem.addRestriction(new Double[]{0d, 0d, -1d, 0d, -80d});
		sProblem.addRestriction(new Double[]{0d, 0d, -3d, -10d, -500d});
		
		// step 0
		int r = 1;
		Long UB = Long.MAX_VALUE;
		Long LB = Long.MIN_VALUE;
		Simplex step0 = new Simplex(sProblem.getSimplexArray(), true);
		step0.loese();
		mProblem.addRestriction(new Double[0]); // add cut
		
		// step 1
		Simplex step1 = new Simplex(mProblem.getSimplexArray(), true);
		step1.loese();
		
		// step 2
		
		// step 3
		
		return new BendersSolutionData();
	}
}