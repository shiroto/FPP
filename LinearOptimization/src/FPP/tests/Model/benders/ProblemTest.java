package FPP.tests.Model.benders;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Model.benders.Problem;

class ProblemTest {

	/**
	 * Tests the standard data structure of {@link Problem}.<br>
	 * Checks if a given simplex tableau is represented the right way<br>
	 * and data can be accessed correct.
	 */
	@Test
	void test() {
		// prepare test data
		Double[] function = {1d, 2d, 3d, 4d, 5d};
		Double[][] coefficients = new Double[][]{
			{1d, 2d, 0d, 0d},
			{1d, 0d, 0d, 0d},
			{0d, 0d, 1d, 0d},
			{0d, 0d, 3d, 10d}};
		Double[][] simplexTableau = new Double[][] {
			{1d, 2d, 0d, 0d, 200d},
			{1d, 0d, 0d, 0d, 50d},
			{0d, 0d, 1d, 0d, 80d},
			{0d, 0d, 3d, 10d, 500d},
			function};
			
		// init problem
		Problem problem = new Problem(simplexTableau);
		
		// check if everything is handeled right
		assertTrue(!problem.isSolvableWithBAndB());
		assertArrayEquals(function, problem.getFunction());
		System.out.println(Arrays.toString(problem.getFunction()));
		assertArrayEquals(new Double[] {200d, 50d, 80d, 500d}, problem.getB());
		System.out.println(Arrays.toString(problem.getB()));
		int i = 0;
		for (Double[] coefficient : problem.getCoefficients()) {
			assertArrayEquals(coefficients[i], coefficient);
			i++;
		}
		System.out.println(Arrays.deepToString(problem.getCoefficients()));
		i = 0;
		for (Double[] simplex : problem.getSimplexTableau()) {
			assertArrayEquals(simplexTableau[i], simplex);
			i++;
		}
		System.out.println(Arrays.deepToString(problem.getSimplexTableau()));
	}
	
	/**
	 * Check if data is updated correctly in {@link Problem}.
	 */
	@Test
	void test_UpdateFunctions() {
		// prepare test data
		Double[] function = {0d, 0d, 0d, 0d, 0d};
		Double[] updatedFunction = {1d, 2d, 3d, 4d, 5d};
		Double[][] simplexTableau = {function}; // tableau without restrictions
		Double[][] restrictions = new Double[][]
				{{1d, 2d, 0d, 0d, 200d},
				{1d, 0d, 0d, 0d, 50d},
				{0d, 0d, 1d, 0d, 80d},
				{0d, 0d, 3d, 10d, 500d}};
		
		// init problem & update function & add restrictions
		Problem problem = new Problem(simplexTableau);
		problem.setFunction(updatedFunction);
		for (Double[] doubles : restrictions) {
			Double[] coefficient = new Double[doubles.length-1];
			System.arraycopy(doubles, 0, coefficient, 0, (doubles.length-1));
			problem.addRestriction(coefficient, doubles[doubles.length-1]);
		}
		
		// check data
		assertArrayEquals(updatedFunction, problem.getFunction());
		System.out.println(Arrays.toString(problem.getFunction()));
		
		assertArrayEquals(restrictions, problem.getRestrictions());
		System.out.println(Arrays.toString(problem.getRestrictions()));
	}
	
	/**
	 * Check if {@link Problem} can handle a tableau where only the function is given.<br>
	 */
	@Test
	void test_WithoutRestrictions() {
		// prepare test data
		Double[] function = {1d, 2d, 3d, 4d, 5d};
		Double[][] simplexTableau = {function}; // tableau without restrictions
		
		// init problem
		Problem problem = new Problem(simplexTableau);
		
		// check methods
		assertArrayEquals(function, problem.getFunction());
		System.out.println(Arrays.toString(problem.getFunction()));
		assertArrayEquals(new Double[0][0], problem.getRestrictions());
		System.out.println(Arrays.toString(problem.getRestrictions()));
		assertArrayEquals(new Double[0], problem.getCoefficients());
		System.out.println(Arrays.toString(problem.getCoefficients()));
		assertArrayEquals(new Double[0], problem.getB());
		System.out.println(Arrays.toString(problem.getB()));
	}
}