package FPP.LinearOptimization.Model.BranchAndBound;

/**
 * Die Klasse BranchAndBoundKnoten bildet die jweiligen Knoten im zu lösenden
 * BranchAndBound Problem ab. Der Knoten enthält die Kinder nach der Verzweigung
 * im Auslotungsergebnis. Der direkte Verweis auf die Kinder wird erst in der
 * Klasse BranchAndBound vollzogen.
 * 
 * @author stf34140
 *
 */

public class BranchAndBoundKnoten {

	private static final double TOLERANZ = 0.000001; // Toleranzwert, in dessen Rahmen double Rundungsfehler korrigiert
														// werden

	private final SimplexProblem problem;

	private int nummer; // Gibt die Nummer des jeweiligen Knotens an, Initialknoten besitzt Nummer 0
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

	private double bessereLoesung; // wird für GUI Erstellung benoetigt und gibt an, welchen Wert die zu dem
									// Zeitpunkt bessere Loesung hatte

	private boolean max;

	public BranchAndBoundKnoten(SimplexProblem problem, BranchAndBound branchAndBound, int nummer) {
		this.problem = problem;
		this.nummer = nummer;
		this.branchAndBound = branchAndBound;

		this.max = branchAndBound.isMax();
	}

	/**
	 * Algorithmus, der den Auslotungs- und Verzweigungsschritt abbildet. Das
	 * Simplexproblem kann nicht loesbar oder die lokale obere Schranke kann
	 * ungünstiger als die globale Schranke sein. In diesem Fall wird nicht weiter
	 * ausgelotet. Wenn die gefundene Loesung zulaessig ist, d.h. den
	 * Ganzzahligkeitsbedingungen entspricht, und der gefundene lokale Wert
	 * günstiger als die globale untere Schranke ist, wird die globale untere
	 * Schranke angepasst und die Loesung gespeichert. Sollte eine Loesung gefunden
	 * werden, die nicht der Ganzzahligkeitsbedingung entspricht, wird verzweigt.
	 * Hierzu wird erst der Index ermittelt, an dem die Ganzzahligkeitsbedingung
	 * verletzt wird. <br>
	 * Anschließend wird auf- bzw. abgerundet und mit den Werten die neue
	 * Restriktion in Form eines Double Arrays hinzugefügt. Anschließend werden zwei
	 * neue Tableaus mit den zwei neu entstandene Restriktionen erstellt und auf
	 * diese als linker und rechter Nachfolger verwiesen.
	 * 
	 */
	public Auslotungsergebnis auslotung(double globaleSchranke, boolean[] flagInteger, int tableauCounter) {
		if (max) {
			// Auslotungsschritte
			if (!problem.isLoesbar()) {
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.schlechtereLoesung();
			} else if (problem.getZFWert() < globaleSchranke) {
				this.bessereLoesung = globaleSchranke;
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.schlechtereLoesung();
			} else if (problem.getZFWert() >= globaleSchranke && werteZulaessig(flagInteger)) {
				ausgelotet = true;
				if (problem.getZFWert() > globaleSchranke) {
					this.auslotungsErgebnis = Auslotungsergebnis.bessereLoesung();
				} else {
					this.auslotungsErgebnis = Auslotungsergebnis.gleichwertigeLoesung();
				}
			} else { // Verzweigungsschritt
				verzweigt = true;
				auslotungsIndex = ermittleReellwertigenIndex(flagInteger);
				double abgerundet = Math.floor(problem.getLoesung()[auslotungsIndex]);
				auslotungAbgerundet = abgerundet;
				double aufgerundet = abgerundet + 1;
				auslotungAufgerundet = aufgerundet;

				double neu[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, abgerundet, 0);
				SimplexTableau neues2d1 = problem.getInitialTableau().erstelle2dArray(neu);
				BranchAndBoundKnoten links = new BranchAndBoundKnoten(new SimplexProblem(neues2d1, max), branchAndBound,
						tableauCounter + 1);

				double neu2[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, aufgerundet, 1);
				SimplexTableau neues2d2 = problem.getInitialTableau().erstelle2dArray(neu2);
				BranchAndBoundKnoten rechts = new BranchAndBoundKnoten(new SimplexProblem(neues2d2, max),
						branchAndBound, tableauCounter + 2);

				this.auslotungsErgebnis = Auslotungsergebnis.verzweigungNoetig(links, rechts);
			}

		} else { // Minimierung
			if (!problem.isLoesbar()) {
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.schlechtereLoesung();
			} else if (problem.getZFWert() > globaleSchranke) {
				this.bessereLoesung = globaleSchranke;
				ausgelotet = true;
				this.auslotungsErgebnis = Auslotungsergebnis.schlechtereLoesung();
			} else if (problem.getZFWert() <= globaleSchranke && werteZulaessig(flagInteger)) {
				ausgelotet = true;
				if (problem.getZFWert() < globaleSchranke) {
					this.auslotungsErgebnis = Auslotungsergebnis.bessereLoesung();
				} else {
					this.auslotungsErgebnis = Auslotungsergebnis.gleichwertigeLoesung();
				}
			} else { // Verzweigungsschritt
				verzweigt = true;
				auslotungsIndex = ermittleReellwertigenIndex(flagInteger);
				double abgerundet = Math.floor(problem.getLoesung()[auslotungsIndex]);
				auslotungAbgerundet = abgerundet;
				double aufgerundet = abgerundet + 1;
				auslotungAufgerundet = aufgerundet;

				double neu[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, abgerundet, 0);
				SimplexTableau neues2d1 = problem.getInitialTableau().erstelle2dArray(neu);
				BranchAndBoundKnoten links = new BranchAndBoundKnoten(new SimplexProblem(neues2d1, max), branchAndBound,
						tableauCounter + 1);

				double neu2[] = problem.getInitialTableau().neueRestriktion(auslotungsIndex, aufgerundet, 1);
				SimplexTableau neues2d2 = problem.getInitialTableau().erstelle2dArray(neu2);
				BranchAndBoundKnoten rechts = new BranchAndBoundKnoten(new SimplexProblem(neues2d2, max),
						branchAndBound, tableauCounter + 2);

				this.auslotungsErgebnis = Auslotungsergebnis.verzweigungNoetig(links, rechts);
			}
		}
		return auslotungsErgebnis;
	}

	public static enum AuslotungsergebnisTyp {
		BESSERE_LOESUNG, GLEICHWERTIGE_LOESUNG, SCHLECHTERE_LOESUNG, VERZWEIGUNG_NOETIG
	}

	public static class Auslotungsergebnis {

		private final AuslotungsergebnisTyp typ;
		private final BranchAndBoundKnoten links;
		private final BranchAndBoundKnoten rechts;

		public Auslotungsergebnis(AuslotungsergebnisTyp typ, BranchAndBoundKnoten links, BranchAndBoundKnoten rechts) {
			this.typ = typ;
			this.links = links;
			this.rechts = rechts;
		}

		private static Auslotungsergebnis bessereLoesung() {
			return new Auslotungsergebnis(AuslotungsergebnisTyp.BESSERE_LOESUNG, null, null);
		}

		private static Auslotungsergebnis gleichwertigeLoesung() {
			return new Auslotungsergebnis(AuslotungsergebnisTyp.GLEICHWERTIGE_LOESUNG, null, null);
		}

		private static Auslotungsergebnis schlechtereLoesung() {
			return new Auslotungsergebnis(AuslotungsergebnisTyp.SCHLECHTERE_LOESUNG, null, null);
		}

		private static Auslotungsergebnis verzweigungNoetig(BranchAndBoundKnoten links, BranchAndBoundKnoten rechts) {
			return new Auslotungsergebnis(AuslotungsergebnisTyp.VERZWEIGUNG_NOETIG, links, rechts);
		}

		public AuslotungsergebnisTyp getTyp() {
			return typ;
		}

		public BranchAndBoundKnoten getLinks() {
			return links;
		}

		public BranchAndBoundKnoten getRechts() {
			return rechts;
		}

	}

	public void setLinks(BranchAndBoundKnoten links) {
		this.links = links;
	}

	public void setRechts(BranchAndBoundKnoten rechts) {
		this.rechts = rechts;
	}

	/**
	 * Ermittelt Index der Loesung an dem Ganzzahligkeitsbedingung nicht erfuellt
	 * ist.
	 */
	private int ermittleReellwertigenIndex(boolean[] flagInteger) {
		for (int i = 0; i < problem.getLoesung().length - 1; i++) {
			if (flagInteger[i]) { // Pruefung ob Entscheidungsvariable ganzzahlig sein muss. Relevant bei Gemischt
									// Ganzzahligen Problemen
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

	public boolean isAusgelotet() {
		return ausgelotet;
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

	// methode nur für tests
	public void setBessereLoesung(double l) {
		this.bessereLoesung = l;
	}

}
