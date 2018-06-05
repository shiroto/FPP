package FPP.LinearOptimization.Model.BranchAndBound;

public enum LoesungsInformation {
	
	KEINE_ZULAESSIGE_LOESUNG 	(false), 
	KEINE_OPTIMALE_LOESUNG 		(false),
	MEHRERE_OPTIMALE_LOESUNGEN 	(true), 
	EINE_OPTIMALE_LOESUNG 		(true);
	
	private final boolean loesbar;
	
	private LoesungsInformation(boolean loesbar) {
		this.loesbar = loesbar;
	}
	
	public boolean isLoesbar() {
		return loesbar;
	}
	
}
