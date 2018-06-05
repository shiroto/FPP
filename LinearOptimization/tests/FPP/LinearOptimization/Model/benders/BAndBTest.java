package FPP.LinearOptimization.Model.benders;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.bb.labor.BranchAndBound;
import de.bb.labor.Tableau;

public class BAndBTest {
	
	/**
	 * ILOG:
	 * -235
	 * x = [40, 1, 1, 192, 0]
	 * 
	 */
	@Test
	public void TestBandB() {
		Double[][] simplexTableau = {
				{1.0, 0.0, 0.0, 0.0, 0.0, 40.0},
				{0.0, 1.0, 0.0, 0.0, 0.0, 1.0},
				{0.0, 0.0, 1.0, 0.0, 0.0, 1.0},
				{-3.0, -3.0, 0.0, 1.0, -1.0, 90.0},
				{-2.5, -2.75, 0.0, 1.0, -1.0, 90.0},
				{-1.0, -1.0, -2.0, -1.0, 1.0, 0.0}
		};
		
		Tableau tableau = new Tableau(simplexTableau);
		List<Double[]> solution = new BranchAndBound(tableau, false).solve();
		for (Double[] doubles : solution) {
			System.out.println(Arrays.deepToString(doubles));
		}
	}
	
	
}
