package FPP.LinearOptimization.Model.benders;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import FPP.LinearOptimization.Data.LinearOptimizationDataUtility;
import de.bb.labor.BranchAndBound;
import de.bb.labor.Tableau;
import de.lip.bb.Simplex;

public class SimplexTest {
	
	@Test
	public void test() {
		Double[][] simplexTableau = {
				{0d, 1d, 0d},
				{-316d, 1d, -1566d},
				{-300d, 1d, -1550d},
				{-50d, 1d, -300d},
				{ 180d, -1d, 0d}
		};
		
		Double[][] simplexTableau2 = {
				{0d, 1d, -1d, 0d},
				{-316d, 1d, -1d, -1566d},
				{-300d, 1d, -1d, -1550d},
				{-50d, 1d, -1d, -300d},
				{ 180d, -1d, 1d, 0d}
		};
		
		Double[] solution = new Simplex(simplexTableau2, true).loese();
		Double[] expected = {5d, -50d, -950d};
		System.out.println("Actual: \t" + Arrays.toString(solution));
		System.out.println("Expected: \t" + Arrays.toString(expected));
	}
	
	@Test
	public void test_nullpointerTest() {
		System.out.println("--------------------------------------------------------");
		Double[] function = {0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
		Double[][] simplexTableau = {
				{-6d, 0d, 1d, 0d, -1d, 1d, -0d, 0d, 0d, 0d, 0d, 0d, -9d},
				{1d,  1d, 1d, 1d,  1d, 1d,  1d, 1d, 1d, 1d, 1d, 1d,  9999d},
				function
		};
		
		Double[] solution = new Simplex(simplexTableau, true).loese();
		System.out.println(Arrays.toString(solution));
	}
}
