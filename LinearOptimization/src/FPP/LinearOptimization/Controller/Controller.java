package FPP.LinearOptimization.Controller;

import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Data.ILinearOptimizationSolutionData;
import FPP.LinearOptimization.Data.LinearOptimizationData;
import FPP.LinearOptimization.Model.AlgorithmFactory;
import FPP.LinearOptimization.Model.ILinearOptimization;
import FPP.LinearOptimization.View.Gui.MainFrame;


public class Controller implements IController {

	public static void main(String[] args) {
		try {
			Controller c = new Controller();
			new MainFrame(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILinearOptimizationSolutionData createAlgorithm(LinearOptimizationData problemData, Algorithm algorithm) {
		ILinearOptimization algorithmImplementation = AlgorithmFactory.createAlgorithm(algorithm);
		return algorithmImplementation.solve(problemData);
	}

}
