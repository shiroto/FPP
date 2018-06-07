package FPP.LinearOptimization.Model.BranchAndBound;

import java.util.List;

/**
 * Interface, welches die zu implementierenden Methoden zur Steuerung vorgibt.
 * 
 * @author stf34140
 *
 */
public interface BranchAndBoundSteuerung {

	void algorithmusBeginnt();

	void naechsterSchritt(List<BranchAndBoundKnoten> knoten, BranchAndBoundKnoten aktuellerKnoten);

	void setLoesung(List<double[]> loesung, List<BranchAndBoundKnoten> knoten);

}
