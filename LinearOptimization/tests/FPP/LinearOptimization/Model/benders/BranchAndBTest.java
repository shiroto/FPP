package FPP.LinearOptimization.Model.benders;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import FPP.LinearOptimization.Data.LinearOptimizationDataUtility;
import de.bb.labor.BranchAndBound;
import de.bb.labor.Tableau;

public class BranchAndBTest {

	@Test
	public void test() {
		System.out.println("--------------------------------------------------------");
		Double[] function = { 42d, 18d, 33d, -1d, 0d};
		Double[][] simplexTableau = {
				{-74997.5d, -40004d, -39988d, 1d, -34997.5d},
				{-40d, -16d, -32d, 1d, -20d},
				{-50d, -32d, -16d, 1d, -22d},
				{1d, 0d, 0d, 0d, 1d},
				{0d, 1d, 0d, 0d, 1d},
				{0d, 0d, 1d, 0d, 1d},
				function
		};
		
		Double[][] masterTableau = LinearOptimizationDataUtility.splitTheta(simplexTableau);
		Tableau tableau = new Tableau(masterTableau);
		List<Double[]> solution = new BranchAndBound(tableau, false).solve();
		if (solution.size() > 0) {
			System.out.println(Arrays.deepToString(LinearOptimizationDataUtility.normalizeTheta(solution.get(0))));
		}
	}
	
	@Test
	public void testSplitTheta() {
		System.out.println("--------------------------------------------------------");
		Double[] function = { 42d, 18d, 33d, -1d, 1d, 0d};
		Double[][] simplexTableau = {
				{-74997.5d, -40004d, -39988d, 1d, -1d, -34997.5d},
				{-40d, -16d, -32d, 1d, -1d, -20d},
				{-50d, -32d, -16d, 1d, -1d, -22d},
				{1d, 0d, 0d, 0d, 0d, 1d},
				{0d, 1d, 0d, 0d, 0d, 1d},
				{0d, 0d, 1d, 0d, 0d, 1d},
				function
		};
		
		Tableau tableau = new Tableau(simplexTableau);
		List<Double[]> solution = new BranchAndBound(tableau, false).solve();
		if (solution.size() > 0) {
			System.out.println("Splitted:" + Arrays.deepToString(solution.get(0)));
			System.out.println("Normalized:" + Arrays.deepToString(
					LinearOptimizationDataUtility.normalizeTheta(solution.get(0))));
		}
	}
	
	@Test
	public void test_ThetaConstant() {
		System.out.println("--------------------------------------------------------");
		Double[] function = { 42d, 18d, 33d, 4d};
		Double[][] simplexTableau = {
				{-74997.5d, -40004d, -39988d, -34993.5d},
				{-40d, -16d, -32d, -16d},
				{-50d, -32d, -16d, -18d},
				{1d, 0d, 0d, 1d},
				{0d, 1d, 0d, 1d},
				{0d, 0d, 1d, 1d},
				function
		};
		
		Tableau tableau = new Tableau(simplexTableau);
		List<Double[]> solution = new BranchAndBound(tableau, false).solve();
		if (solution.size() > 0) {
			System.out.println("Constant:" + Arrays.deepToString(solution.get(0)));
		}
	}	
}