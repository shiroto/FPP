package FPP.LinearOptimization.View.Gui.BranchAndBound;

import java.awt.Container;

import javax.swing.*;

import org.abego.treelayout.*;
import org.abego.treelayout.util.DefaultConfiguration;

import FPP.LinearOptimization.Model.BranchAndBound.*;

public class MeineSwingDemo {

	BranchAndBoundTreePane panel;
	
	public void initialisiere(BranchAndBound bAndB) {
		
		TreeForTreeLayout<BranchAndBoundKnoten> tree =  SampleTreeFactory.createTree(bAndB); 
		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		DefaultConfiguration<BranchAndBoundKnoten> configuration = new DefaultConfiguration<>(gapBetweenLevels,
				gapBetweenNodes);
		
		BranchAndBoundNodeExtendProvider BranchAndBoundExtentProvider = new BranchAndBoundNodeExtendProvider();
		
		TreeLayout<BranchAndBoundKnoten> treeLayout = new TreeLayout<>(tree,
				BranchAndBoundExtentProvider, configuration);
		
		 panel = new BranchAndBoundTreePane(treeLayout);
		 
		//showInFrame(new JScrollPane(panel));
	}
	
	public static void showInFrame(JComponent panel) {
		JFrame jf = new JFrame();
		Container contentPane  = jf.getContentPane();
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
				10, 10, 10, 10));
		contentPane.add(panel);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public BranchAndBoundTreePane getPanel() {
		return panel;
	}
}
