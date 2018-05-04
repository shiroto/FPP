package FPP.tests.Model.benders;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;
import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;

public class BendersFullTest {
	
	/**
	 * All restrictions have to be <=
	 * Function must be minimizer
	 */
	
	@Test
	void test_example_powerPoint() {
		System.out.println("test_example_powerPoint");
		Double[] function = {200d, 50d, 80d, 500d, 180d, 0d};
		Double[][] simplexTableau = {
				{-1d, -1d, 0d, 0d, -2d, -10d},
				{-2d, 0d, 0d, 0d, -2d, -10d},
				{0d, 0d, -1d, -3d, -0.5d, -2d},
				{0d, 0d, 0d, -10d, -1d, -6d},
				function,};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 25;
		Double[] parameterResults = {-50d, 0d, 0d, 0d, 0.1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_01() {
		System.out.println("test_example_01");
		// prepare test data
		Double[] function = {-1d, -2d, -3d, -1d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, 1d, 1d, 10d, 20d},
			{1d, -3d, 1d, 0d, 30d},
			{0d, 0d, 0d, -3.5d, 0d},
			{0d, 0d, 0d, 3.5d, 0d},
			{1d, 0d, 0d, 0d, 40d},
			{0d, 0d, 0d, -1d, -2d},
			{0d, 0d, 0d, 1d, 3d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {3};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -122.5d;
		Double[] parameterResults = {40d, 10.5d, 19.5d, 3d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_02() {
		System.out.println("test_example_02");
		// prepare test data
		Double[] function = {-8d, -6d, 2d, 42d, 18d, 33d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{2d, 1d, -1d, -10d, -8d, 0d, -4d},
			{1d, 1d, 1d, -5d, 0d, -8d, -3d},
			{0d, 0d, 0d, 1d, 0d, 0d, 1d},
			{0d, 0d, 0d, 0d, 1d, 0d, 1d},
			{ 0d, 0d, 0d, 0d, 0d, 1d, 1d}, 
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {3, 4, 5};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer,
				BendersMasterCoefficientType.Integer, BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 25d;
		Double[] parameterResults = {0d, 4.5d, 0.5d, 0d, 1d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_03() {
		System.out.println("test_example_03");
		// prepare test data
		Double[] function = {-1d, -1d, -3d, -2d, -2d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, -1d, 1d, 1d, 0d, 30d},
			{1d, 0d, 1d, -3d, 0d, 30d},
			{1d, 0d, 0d, 0d, 0d, 40d},
			{0d, 1d, 0d, 0d, 0d, 1d},
			{0d, 0d, 0d, 0d, 1d, 1d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {0, 1, 4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer, 
				BendersMasterCoefficientType.Integer, BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -235.75d;
		Double[] parameterResults = {40d, 1d, 50.75d, 20.25d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_04() {
		System.out.println("test_example_04");
		// prepare test data
		Double[] function = {-1d, 1d, -3d, -2d, -2d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, -1d, 1d, 1d, 0d, 30d},
			{1d, 0d, 1d, -3d, 0d, 30d},
			{0d, -1d, 1d, 0d, 0d, 30d},
			{1d, 0d, 0d, 0d, 0d, 40d},
			{0d, 1d, 0d, 0d, 0d, 1d},
			{0d, 0d, 0d, 0d, 1d, 1d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {1, 4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer, 
				BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -214d;
		Double[] parameterResults = {40d, 1d, 31d, 40d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_05() {
		System.out.println("test_example_05");
		// prepare test data
		Double[] function = {-3d, -2d, -1d, -20d};
		Double[][] simplexTableau = new Double[][] {
			{1d, 1d, 1d, 7d},
			{4d, 2d, 1d, 12d},
			{-4d, -2d, -1d, -12d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {2};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -32d;
		Double[] parameterResults = {0d, 6d, 0d};
		
		testInput(inputData, result, parameterResults);
	}
	
	void testInput(BendersOptimizationData input, double expectedResult, Double[] expectedParameterResults) {
		IBendersOptimizationSolutionData solutionData = new BendersAlgorithm().solve(input);
		Double[] optSolution = solutionData.getOptSolution();
		double result = optSolution[optSolution.length - 2];
		Double[] parameterResults = Arrays.copyOfRange(optSolution, 0, optSolution.length - 2);
		
		assertArrayEquals(expectedParameterResults, parameterResults);
		assertEquals(expectedResult, result);
	}
}
