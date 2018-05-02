package FPP.LinearOptimization.Data;

import java.util.List;

import FPP.LinearOptimization.Model.benders.BendersStepData;
/**
 * Interface for benders algorithm solution.
 * 
 * @author Stefan
 */
public interface IBendersOptimizationSolutionData {

	/**
	 * This is the actual problem which is given to the algorithm.
	 * 
	 * @return BendersOptimizationData
	 */
	BendersOptimizationData getBendersOptimizationData();
	
	/**
	 * This gives you the optimal solution for the given problem.<br>	 
	 * <b>Example for solution object:</b><br>
	 * {0d, 0d, 3d, 10d, 500d, 3.0}<br><br>
	 * 
	 * 500d is the objective function value<br>
	 * last value (here 3.0) is an additional info about the solution.<br>
	 * <p><ul>
	 * <li>0.0 <b>no solution</b>
	 * <li>1.0 <b>no optimal solution</b>
	 * <li>2.0 <b>more than one solution</b>
	 * <li>3.0 <b>optimal solution</b>
	 * </ul><p>
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