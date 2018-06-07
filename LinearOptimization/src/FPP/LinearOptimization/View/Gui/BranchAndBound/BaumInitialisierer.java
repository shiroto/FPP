package FPP.LinearOptimization.View.Gui.BranchAndBound;

import java.util.List;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten;

public class BaumInitialisierer {

	BranchAndBoundTreePane panel;

	public void initialisiere(List<BranchAndBoundKnoten> knoten, BranchAndBoundKnoten aktuellerKnoten) {

		TreeForTreeLayout<BranchAndBoundKnoten> tree = SampleTreeFactory.createTree(knoten);
		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		DefaultConfiguration<BranchAndBoundKnoten> configuration = new DefaultConfiguration<>(gapBetweenLevels,
				gapBetweenNodes);

		BranchAndBoundNodeExtendProvider BranchAndBoundExtentProvider = new BranchAndBoundNodeExtendProvider();

		TreeLayout<BranchAndBoundKnoten> treeLayout = new TreeLayout<>(tree, BranchAndBoundExtentProvider,
				configuration);

		panel = new BranchAndBoundTreePane(treeLayout, aktuellerKnoten);

	}

	public BranchAndBoundTreePane getPanel() {
		return panel;
	}
}
