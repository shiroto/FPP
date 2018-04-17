package FPP.LinearOptimization.Model.benders;

public class MasterProblem extends Problem {

	private BendersMasterCoefficientType[] types;
	
	public MasterProblem(Double[][] simplexTableau) {
		super(simplexTableau);
	}
	
	@Override
	public boolean isSolveableWithBAndB() {
		for (Double f : getFunction()) {
			// we check if we have floats or ints 
			int sqrt = (int) Math.round(Math.sqrt(f));
			if ((sqrt*sqrt) != f) {
				// one is a float number, so no branch and bound allowed
				return false;
			}
		}
		// only ints, so branch and bound is allowed here 
		return true;
	}
	
	public BendersMasterCoefficientType[] getTypes() {
		return types;
	}
	
	public void setTypes(BendersMasterCoefficientType[] types) {
		this.types = types;
	}
}