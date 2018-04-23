package FPP.LinearOptimization.Model.benders;

public class MasterProblem extends Problem {

	// internal variables
	private BendersMasterCoefficientType[] types;
	
	/**
	 * Initializes a master problem by a given simplex tableau.<br><br>
	 * 
	 * Tableau needs to be given in this way:<br>
	 * {{1d, 2d, 0d, 0d, 200d}, <b>restriction 1</b><br>
	 * {1d, 0d, 0d, 0d, 50d}, <b>restriction 2</b><br>
	 * {0d, 0d, 1d, 0d, 80d}, <b>restriction 3</b><br>
	 * {0d, 0d, 3d, 10d, 500d}} <b>function</b>
	 * 
	 * @param simplexTableau
	 */
	public MasterProblem(Double[][] simplexTableau) {
		super(simplexTableau);
	}
	
	@Override
	public boolean isSolvableWithBAndB() {
		/*for (Double f : getFunction()) {
			// we check if we have floats or ints 
			int sqrt = (int) Math.round(Math.sqrt(f));
			if ((sqrt*sqrt) != f) {
				// one is a float number, so no branch and bound allowed
				return false;
			}
		}
		// only integers, so branch and bound is allowed here 
		return true;*/
		//TODO??
		for (BendersMasterCoefficientType type : types) {
			if (type == BendersMasterCoefficientType.Float) return false;
		}
		return true;
	}
	
	/**
	 * Defines the type of the master problem function coefficients.<br><br>
	 * 
	 * <b>example function:</b><br>
	 * {0d, 0d, 3d, 10d, 500d}<br><br>
	 * 
	 * <b>example types:</b><br>
	 * {I, I, I, F}
	 * 
	 * @return coefficient type
	 */
	public BendersMasterCoefficientType[] getTypes() {
		return types;
	}
	
	/**
	 * Set the coefficient type for the master problem function.
	 * 
	 * @param types
	 */
	public void setTypes(BendersMasterCoefficientType[] types) {
		this.types = types;
	}
}