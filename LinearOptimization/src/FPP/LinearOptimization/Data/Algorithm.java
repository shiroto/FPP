package FPP.LinearOptimization.Data;

public enum Algorithm {

	BendersAlgorithm("Bender"),
	DanzigAlgorithm("Dantzig-Wolfe")
	;
	
	private String name;
	
	private Algorithm(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return name;
	}
}
