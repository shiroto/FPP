package FPP.LinearOptimization.Model.benders;

import java.util.LinkedList;
import java.util.List;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
/**
 * Returns the complete solution data for benders algorithm.<br>
 * Description for the variables in the {@link IBendersOptimizationSolutionData}.
 * 
 * @author Stefan
 */
public class BendersSolutionData implements IBendersOptimizationSolutionData {

	public BendersOptimizationData bendersOptimizationData;
	public List<BendersStepData> steps = new LinkedList<BendersStepData>();
	public Double[] optSolution;
	public String addInfo;
	
	public BendersSolutionData(BendersOptimizationData bendersOptimizationData) {
		this.bendersOptimizationData = bendersOptimizationData;
	}
	
	@Override
	public BendersOptimizationData getBendersOptimizationData() {
		return bendersOptimizationData;
	}
	
	@Override
	public Double[] getOptSolution() {
		return optSolution;
	}
	
	public void setOptSolution(Double[] optSolution) {
		this.optSolution = optSolution;
	}
	
	@Override
	public List<BendersStepData> getSteps() {
		return steps;
	}
	
	public void addStep(BendersStepData stepData) {
		this.steps.add(stepData);
	}
	
	@Override
	public String getAddInfo() {
		return addInfo;
	}
	
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
}