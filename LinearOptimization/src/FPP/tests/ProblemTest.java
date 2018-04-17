package FPP.tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Model.benders.Problem;

class ProblemTest {

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
}