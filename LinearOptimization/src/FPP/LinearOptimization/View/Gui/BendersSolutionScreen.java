package FPP.LinearOptimization.View.Gui;

import javax.swing.JPanel;

import FPP.LinearOptimization.Model.benders.BendersSolutionData;

public class BendersSolutionScreen extends JPanel{
	private MainFrame mainFrame;
	private BendersSolutionData bendersSolutionObject;
	public BendersSolutionScreen(MainFrame mainFrame, BendersSolutionData bendersSolutionObject) {
		this.mainFrame = mainFrame;
		this.bendersSolutionObject = bendersSolutionObject;
		initializeScreen();
	}

	private void initializeScreen() {
		// TODO Auto-generated method stub
		
	}

}
