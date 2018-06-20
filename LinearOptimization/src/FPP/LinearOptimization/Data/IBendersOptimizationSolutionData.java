package FPP.LinearOptimization.Data;

import java.util.List;

import FPP.LinearOptimization.Model.benders.BendersStepData;
/**
 * Interface for benders algorithm solution.
 * 
 * @author Stefan
 */
public interface IBendersOptimizationSolutionData extends ILinearOptimizationSolutionData {

	/**
	 * This is the actual problem which is given to the algorithm.
	 * 
	 * @return BendersOptimizationData
	 */
	BendersOptimizationData getBendersOptimizationData();
	
	/**
	 * This gives you the optimal solution for the given problem.<br>	 
	 * <b>Example for solution object:</b><br>
	 * {0d, 0d, 3d, 10d, 500d}<br><br>
	 * 
	 * 500d is the objective function value<br>
	 * @return optimal solution
	 */
	Double[] getOptSolution();
	
	/**
	 * This list gives you information about each processed step.<br>
	 * 
	 * @return BendersStepData
	 */
	List<BendersStepData> getSteps();
	
	/**
	 * The additional info could be an error message which occurs<br
	 * while processing the algorithm.<br>
	 * Another additional info is sent, when a solution is ambiguously.
	 * 
	 * @return additional info
	 */
	String getAddInfo();
}