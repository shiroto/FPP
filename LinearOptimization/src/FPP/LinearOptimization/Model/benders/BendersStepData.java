package FPP.LinearOptimization.Model.benders;

/**
 * This object saves the interim solutions for the benders algorithm.<br>
 * 
 * @author Stefan
 */
public class BendersStepData {

	/**
	 * Tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3 or the added cut!</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b>
	 */
	private Double[][] masterProblem;
	
	/**
	 * Tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b>
	 */
	private Double[][] subProblem;
	
	/**
	 * Example for solution object:<br>
	 * {0d, 0d, 3d, 10d, 500d}<br><br>
	 * 
	 * 500d is the objective function value<br>
	 */
	private Double[] masterSolution;
	
	/**
	 * Example for solution object:<br>
	 * {0d, 0d, 3d, 10d, 500d}<br><br>
	 * 
	 * 500d is the objective function value<br>
	 */
	private Double[] subSolution;
	
	/**
	 * Iteration number
	 */
	private int r;
	
	/**
	 * Lower Bound for this iteration step.
	 */
	private double lowerBound;
	
	/**
	 * Upper Bound for this iteration step.
	 */
	private double upperBound;
	
	public BendersStepData(int r) {
		this.r = r;
	}
	
	public Double[][] getMasterProblem() {
		return masterProblem;
	}
	
	public void setMasterProblem(Double[][] masterProblem) {
		this.masterProblem = masterProblem;
	}
	
	public Double[][] getSubProblem() {
		return subProblem;
	}
	
	public void setSubProblem(Double[][] subProblem) {
		this.subProblem = subProblem;
	}
	
	public Double[] getMasterSolution() {
		return masterSolution;
	}
	
	public void setMasterSolution(Double[] masterSolution) {
		this.masterSolution = masterSolution;
	}
	
	public Double[] getSubSolution() {
		return subSolution;
	}
	
	public void setSubSolution(Double[] subSolution) {
		this.subSolution = subSolution;
	}
	
	public double getLowerBound() {
		return lowerBound;
	}
	
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	public double getUpperBound() {
		return upperBound;
	}
	
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
	
	public int getR() {
		return r;
	}
}