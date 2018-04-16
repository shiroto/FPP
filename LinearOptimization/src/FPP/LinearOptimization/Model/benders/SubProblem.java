package FPP.LinearOptimization.Model.benders;

public class SubProblem extends Problem {

	private double[][] A = {
			{-2},
			{-2},
			{-0.5},
			{-1},
	};
	
	private double[] b = {-10, -10, -2, -6};
	
	private double[] y;
	
	public SubProblem(Double[] function) {
		super(function);
	}
	
	public double[][] getA() {
		return A;
	}
	
	public double[] getb() {
		return b;
	}
}