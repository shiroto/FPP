package FPP.LinearOptimization.Model;

import FPP.LinearOptimization.Controller.BendersAlgorithmAdapter;
import FPP.LinearOptimization.Data.Algorithm;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;

public class AlgorithmFactory {

	public static ILinearOptimization createAlgorithm(Algorithm algorithm) {
		switch (algorithm) {
		case BendersAlgorithm:
			return new BendersAlgorithmAdapter();
		default:
			throw new RuntimeException("unimplemented algorithm " + algorithm);
		}
	}

}
