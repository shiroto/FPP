package FPP.LinearOptimization.Model.BranchAndBound;

public class BranchAndBoundKnoten {
	
	private static final double TOLERANZ = 0.000001; // Toleranzwert, in dessen Rahmen double Rundungsfehler korrigiert werden
	
	private final SimplexProblem problem;
	
	private int nummer; // Gibt die Nummer des jeweiligen Tableaus an, Initialtableau besitzt Nummer 0
	private BranchAndBoundKnoten links;
	private BranchAndBoundKnoten rechts;
	private boolean verzweigt;
	private boolean ausgelotet;
	private int auslotungsIndex;
	private double auslotungAbgerundet;
	private double auslotungAufgerundet;
	private boolean besteLoesung = false;
	private BranchAndBound branchAndBound;
	private Auslotungsergebnis auslotungsErgebnis;
	
	private double bessereLoesung;   // wird für GUI Erstellung benoetigt und gibt an, welchen Wert die zu dem Zeitpunkt bessere Loesung hatte
	

	private boolean max;



	public BranchAndBoundKnoten(SimplexProblem problem,BranchAndBound branchAndBound, int nummer) {
		this.problem = problem;
		this.nummer = nummer;
		this.branchAndBound = branchAndBound;
		
		this.max= branchAndBound.isMax();
	}
	
	/**
	 * Algorithmus, der den Auslotungs- und Verzweigungsschritt abbildet. Das Simplexproblem kann
	 * nicht loesbar oder die lokale obere Schranke kann ungünstiger als die
	 * globale Schranke sein. In diesem Fall wird nicht weiter ausgelotet.
	 * Wenn die gefundene Loesung zulaessig ist, d.h. den
	 * Ganzzahligkeitsbedingungen entspricht, und der gefundene lokale Wert
	 * günstiger als die globale untere Schranke ist, wird die globale untere
	 * Schranke angepasst und die Loesung gespeichert. Sollte eine Loesung
	 * gefunden werden, die nicht der Ganzzahligkeitsbedingung entspricht, wird
	 * verzweigt. Hierzu wird erst der Index ermittelt, an dem die
	 * Ganzzahligkeitsbedingung verletzt wird. <br>
	 * Anschließend wird auf- bzw. abgerundet und mit den Werten die neue
	 * Restriktion in Form eines Double Arrays hinzugefügt. Anschließend werden
	 * zwei neue Tableaus mit den zwei neu entstandene Restriktionen erstellt und auf diese als linker und rechter Nachfolger 
	 * verwiesen.
	 * 
	 */
	public Auslotungsergebnis auslotung(double untereSchranke, boolean[] flagInteger, int tableauCounter) {
		if(max) {
		//Auslotungsschritte
		if (!problem.isLoesbar()) {
			ausgelotet = true;
			this.auslotungsErgebnis = Auslotungsergebnis.SCHLECHTERE_LOESUNG;
			return Auslotungsergebnis.SCHLECHTERE_LOESUNG;
		}
		if (problem.getZFWert() < untereSchranke) {
			this.bessereLoesung = untereSchranke;
			ausgelotet = true;
			this.auslotungsErgebnis = Auslotungsergebnis.SCHLECHTERE_LOESUNG;
			return Auslotungsergebnis.SCHLECHTERE_LOESUNG;
		}
		if (problem.getZFWert() >= untereSchranke && werteZulaessig(flagInteger)) {
			ausgelotet = true;
			if (problem.getZFWert() > untereSchranke) {
				this.auslotungsErgebnis = Auslotungsergebnis.BESSERE_LOESUNG;
				return Auslotungsergebnis.BESSERE_LOESUNG;
			} else {
				this.auslotungsErgebnis= Auslotungsergebnis.GLEICHWERTIGE_LOESUNG;
				return Auslotungsergebnis.GLEICHWERTIGE_LOESUNG;
			}
		} else { // Verzweigungsschritt
			verzweigt = true;
			auslotungsIndex = getIndexOfDecimal(flagInteger);
			double abgerundet = Math.floor(problem.getLoesung()[auslotungsIndex]);
			auslotungAbgerundet = abgerundet;
			double aufgerundet = abgerundet + 1;
			auslotungAufgerundet = aufgerundet;
			
			double neu[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, abgerundet, 0);
			SimplexTableau neues2d1 = problem.getInitialTableau().create2dArray(neu);
			links = new BranchAndBoundKnoten(new SimplexProblem(neues2d1,max), branchAndBound, tableauCounter + 1);

			double neu2[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, aufgerundet, 1);
			SimplexTableau neues2d2 = problem.getInitialTableau().create2dArray(neu2);
			rechts = new BranchAndBoundKnoten(new SimplexProblem(neues2d2,max), branchAndBound, tableauCounter + 2);
			
			this.auslotungsErgebnis = Auslotungsergebnis.VERZWEIGUNG_NOETIG;
			return Auslotungsergebnis.VERZWEIGUNG_NOETIG;
		}
		
		}else { //Minimierung
			if (!problem.isLoesbar()) {
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.SCHLECHTERE_LOESUNG;
				return Auslotungsergebnis.SCHLECHTERE_LOESUNG;
			}
			if (problem.getZFWert() > untereSchranke) {
				this.bessereLoesung = untereSchranke;
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.SCHLECHTERE_LOESUNG;
				return Auslotungsergebnis.SCHLECHTERE_LOESUNG;
			}
			if (problem.getZFWert() <= untereSchranke && werteZulaessig(flagInteger)) {
				ausgelotet = true;
				if (problem.getZFWert() < untereSchranke) {
					this.auslotungsErgebnis = Auslotungsergebnis.BESSERE_LOESUNG;
					return Auslotungsergebnis.BESSERE_LOESUNG;
				} else {
					this.auslotungsErgebnis= Auslotungsergebnis.GLEICHWERTIGE_LOESUNG;
					return Auslotungsergebnis.GLEICHWERTIGE_LOESUNG;
				}
			} else { // Auslotungsschritt
				verzweigt = true;
				auslotungsIndex = getIndexOfDecimal(flagInteger);
				double abgerundet = Math.floor(problem.getLoesung()[auslotungsIndex]);
				auslotungAbgerundet = abgerundet;
				double aufgerundet = abgerundet + 1;
				auslotungAufgerundet = aufgerundet;
				
				double neu[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, abgerundet, 0);
				SimplexTableau neues2d1 = problem.getInitialTableau().create2dArray(neu);
				links = new BranchAndBoundKnoten(new SimplexProblem(neues2d1,max), branchAndBound, tableauCounter + 1);

				double neu2[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, aufgerundet, 1);
				SimplexTableau neues2d2 = problem.getInitialTableau().create2dArray(neu2);
				rechts = new BranchAndBoundKnoten(new SimplexProblem(neues2d2,max), branchAndBound, tableauCounter + 2);
				
				this.auslotungsErgebnis = Auslotungsergebnis.VERZWEIGUNG_NOETIG;
				return Auslotungsergebnis.VERZWEIGUNG_NOETIG;
			}
		}
	}
	
	public static enum Auslotungsergebnis {
		BESSERE_LOESUNG,
		GLEICHWERTIGE_LOESUNG,
		SCHLECHTERE_LOESUNG,
		VERZWEIGUNG_NOETIG
	}

	/**
	 * Ermittelt Index der Loesung an dem Ganzzahligkeitsbedingung nicht erfuellt
	 * ist.
	 */
	private int getIndexOfDecimal(boolean[] flagInteger) {
		for (int i = 0; i < problem.getLoesung().length - 1; i++) {
			if (flagInteger[i]) { // Pruefung ob Entscheidungsvariable ganzzahlig sein muss. Relevant bei Gemischt
										// Ganzzahligen Problems
				if (Math.abs(Math.round(problem.getLoesung()[i]) - problem.getLoesung()[i]) > TOLERANZ) {
					return i;
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Prueft ob die aktuelle Loesung zulaessig ist
	 **/
	private boolean werteZulaessig(boolean[] flagInteger) {
		for (int i = 0; i < problem.getLoesung().length - 1; i++) {
			if (flagInteger[i]) {
				if (!(Math.abs(Math.round(problem.getLoesung()[i]) - problem.getLoesung()[i]) < TOLERANZ)) {
					return false;
				}
			}
		}
		return true;

	}
	
	public SimplexProblem getProblem() {
		return problem;
	}

	public int getNummer() {
		return nummer;
	}

	public BranchAndBoundKnoten getLinks() {
		return links;
	}

	public BranchAndBoundKnoten getRechts() {
		return rechts;
	}
	
	public boolean isVerzweigt() {
		return verzweigt;
	}

	public int getAuslotungsIndex() {
		return auslotungsIndex;
	}

	public double getAuslotungAbgerundet() {
		return auslotungAbgerundet;
	}
	
	public double getAuslotungAufgerundet() {
		return auslotungAufgerundet;
	}

	public boolean isBesteLoesung() {
		return besteLoesung;
	}

	public void setBesteLoesung(boolean besteLoesung) {
		this.besteLoesung = besteLoesung;
	}
	public BranchAndBound getBranchAndBound() {
		return branchAndBound;
	}

	public Auslotungsergebnis getAuslotungsErgebnis() {
		return auslotungsErgebnis;
	}
	public void setAuslotungsErgebnis(Auslotungsergebnis auslotungsErgebnis) {
		this.auslotungsErgebnis = auslotungsErgebnis;
	}
	
	public double getBessereLoesung() {
		return bessereLoesung;
	}


}
