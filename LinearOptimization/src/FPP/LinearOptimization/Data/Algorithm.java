package FPP.LinearOptimization.Data;

public enum Algorithm {

	BendersAlgorithm("Benders"),
	DantzigAlgorithm("Dantzig-Wolfe"), BranchBoundAlgorithm("Branch & Bound")
	;
	
	private String name;
	
	private Algorithm(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return name;
	}
}
