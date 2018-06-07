package FPP.LinearOptimization.View.Gui.BranchAndBound;

import org.abego.treelayout.NodeExtentProvider;

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten;

public class BranchAndBoundNodeExtendProvider implements NodeExtentProvider<BranchAndBoundKnoten> {

	@Override
	public double getHeight(BranchAndBoundKnoten arg0) {
		return 140; // Höhe der Knoten
	}

	@Override
	public double getWidth(BranchAndBoundKnoten arg0) { //Breite der Knoten
		return 140;
	}

}
