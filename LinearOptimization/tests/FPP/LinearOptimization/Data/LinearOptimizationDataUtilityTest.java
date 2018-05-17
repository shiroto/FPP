package FPP.LinearOptimization.Data;


import static org.junit.Assert.assertArrayEquals;

import java.util.stream.Stream;

import org.junit.Test;

public class LinearOptimizationDataUtilityTest {
	
	private static final double DELTA = 1e-12;
	
	@Test
	public void test_SplitTheta() {
		double[][] expectedTableau = {
				{1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d}, 
				{0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d},
				{0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d},
				{0.0d, 0.0d, 0.0d, -1.0d, 1.0d, 0.0d},
				{-74997.5d, -40004.0d, -39988.0d, -1.0d, 1.0d, -34997.5d},
				{-49995.0d, 0.0d, -79992.0d, -1.0d, 1.0d,-29997.0d},
				{42.0d, 18.0d, 33.0d, 1.0d, -1.0d, 0.0d}
		};
		
		Double[][] simplexTableau = {
				{1.0d, 0.0d, 0.0d, 0.0d, 1.0d}, 
				{0.0d, 1.0d, 0.0d, 0.0d, 1.0d},
				{0.0d, 0.0d, 1.0d, 0.0d, 1.0d},
				{0.0d, 0.0d, 0.0d, -1.0d, 0.0d},
				{-74997.5d, -40004.0d, -39988.0d, -1.0d, -34997.5d},
				{-49995.0d, 0.0d, -79992.0d, -1.0d, -29997.0d},
				{42.0d, 18.0d, 33.0d, 1.0d, 0.0d}
		};
		
		Double[][] resultTableau = LinearOptimizationDataUtility.splitTheta(simplexTableau);
		testInput(expectedTableau, resultTableau);
	}
	
	@Test
	public void test_NormalizeTheta() {
		double[] expectedSolution = {0.0d, 0.0d, 1.0d, 0.0d, -33.00000000002339d};
		Double[] solution = {0.0d, 0.0d, 1.0d, 8192.0d, 8192.0d, -33.00000000002339d};
		Double[] resultSolution = LinearOptimizationDataUtility.normalizeTheta(solution);
		testInput(expectedSolution, resultSolution);
	}
	
	void testInput(double[] expectedResult, Double[] result) {
		double[] line = Stream.of(result).mapToDouble(Double::doubleValue).toArray();
		assertArrayEquals(expectedResult, line, DELTA);
	}
	
	void testInput(double[][] expectedResult, Double[][] result) {
		int i = 0;
		for (Double[] ds : result) {
			double[] line = Stream.of(ds).mapToDouble(Double::doubleValue).toArray();
			assertArrayEquals(expectedResult[i], line, DELTA);
			i++;
		}
	}
	
}
