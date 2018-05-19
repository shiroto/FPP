package FPP.LinearOptimization.Model.benders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
import FPP.LinearOptimization.Model.benders.BendersAlgorithm;
import FPP.LinearOptimization.Model.benders.BendersMasterCoefficientType;

public class BendersAlgorithmTest {
	
	private static final double DELTA = 1e-12;
	
	/**
	 * All restrictions have to be <=
	 * Function must be minimizer
	 */
	
	@Test
	void test_example_powerPoint() {
		System.out.println("test_example_powerPoint_B");
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
		
		double result = -1250;
		double[] parameterResults = {4d, 4d, 0d, 0.5d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	@Test
	void test_example_powerPoint_2() {
		System.out.println("test_example_powerPoint_F");
		Double[] function = {200d, 50d, 80d, 500d, 180d, 0d};
		Double[][] simplexTableau = {
				{-1d, -1d, 0d, 0d, -2d, -10d},
				{-2d, 0d, 0d, 0d, -2d, -10d},
				{0d, 0d, -1d, -3d, -0.5d, -2d},
				{0d, 0d, 0d, -10d, -1d, -6d},
				function,};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Float};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -50d;
		double[] parameterResults = {0d, 0d, 0d, 0.1, 5d};
		
		testInput(inputData, result, parameterResults);
	}
	
//	@Test
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
		double[] parameterResults = {40d, 10.5d, 19.5d, 3d};
		
		testInput(inputData, result, parameterResults);
	}
	
	// TODO Martin 
//	@Test
	void test_example_02() {
		System.out.println("test_example_02");
		// prepare test data
		Double[] function = {-8d, -6d, 2d, 42d, 18d, 33d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{2d, 1d, -1d, -10d, -8d, 0d, -4d},
			{1d, 1d, 1d, -5d, 0d, -8d, -3d},
//			{0d, 0d, 0d, 1d, 0d, 0d, 1d},
//			{0d, 0d, 0d, 0d, 1d, 0d, 1d},
//			{ 0d, 0d, 0d, 0d, 0d, 1d, 1d}, 
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {3, 4, 5};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary,
				BendersMasterCoefficientType.Binary, BendersMasterCoefficientType.Binary};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 25d;
		double[] parameterResults = {0d, 4.5d, 0.5d, 0d, 1d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
//	@Test
	void test_example_03() {
		System.out.println("test_example_03");
		// prepare test data
		Double[] function = {-1d, -1d, -3d, -2d, -2d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, -1d, 1d, 1d, 0d, 30d},
			{1d, 0d, 1d, -3d, 0d, 30d},
//			{1d, 0d, 0d, 0d, 0d, 40d},
//			{0d, 1d, 0d, 0d, 0d, 1d},
//			{0d, 0d, 0d, 0d, 1d, 1d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {0, 1, 4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer, 
				BendersMasterCoefficientType.Integer, BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -235.75d;
		double[] parameterResults = {40d, 1d, 50.75d, 20.25d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
//	@Test
	void test_example_04() {
		System.out.println("test_example_04");
		// prepare test data
		Double[] function = {-1d, 1d, -3d, -2d, -2d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, -1d, 1d, 1d, 0d, 30d},
			{1d, 0d, 1d, -3d, 0d, 30d},
			{0d, -1d, 1d, 0d, 0d, 30d},
//			{1d, 0d, 0d, 0d, 0d, 40d},
//			{0d, 1d, 0d, 0d, 0d, 1d},
//			{0d, 0d, 0d, 0d, 1d, 1d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {1, 4};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer, 
				BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -214d;
		double[] parameterResults = {40d, 1d, 31d, 40d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
//	@Test
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
		double[] parameterResults = {0d, 6d, 0d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/*
	 * Branch and Bound Minimierung
	 * Keine Deklaration von Y Variablen!
	 *
	 * Double [][] array= {
				{-4d,-4d,-10d},
				{-2d,-11d,-11d},
				{-4d,2d,1d},
				{2d,1d,0d}};
	 * --> 1,2, Zielfunktionswert=4
	 *
	 * https://www.ingenieurkurse.de/operations-research-2/ganzzahlige-optimierung/branch-and-bound-verfahren/minimierungsprobleme/branch-and-bound-am-minimierungsproblem-optimale-loesung/beispiel-branch-and-bound-am-minimierungsproblem-optimale-loesung-3.html
	 */
//	@Test
	void test_example_06() {
		System.out.println("test_example_06");
		// prepare test data
		
		Double[] function = {-8d, -6d, 2d, 42d, 18d, 33d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-4d,-4d,-10d},
			{-2d,-11d,-11d},
			{-4d,2d,1d},
			{2d,1d,0d}, 
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {};
		BendersMasterCoefficientType[] yTypes = {};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 4d;
		double[] parameterResults = {1d, 2d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/*
	 * 0,1 Problem
	 * 		Double [][] array= {    
				{8d,8d,6d,6d,14d},
				{1d,0d,0d,0d,1d},
				{-1d,0d,0d,0d,0d},
				{0d,1d,0d,0d,1d},
				{0d,-1d,0d,0d,0d},
				{0d,0d,1d,0d,1d},
				{0d,0d,-1d,0d,0d},
				{0d,0d,0d,1d,1d},
				{0d,0d,0d,-1d,0d},
				{-14d,-20d,-18d, -26d,0d}};
	 * --> [0.0, 1.0, 0.0, 1.0, 46.0]
	 * 
	 * https://www.ingenieurkurse.de/operations-research-2/ganzzahlige-optimierung/branch-and-bound-verfahren/branch-and-bound-knapsack-problem.html  
	 */
//	@Test
	void test_example_07() {
		System.out.println("test_example_07");
		// prepare test data
		Double[] function = {-14d,-20d,-18d, -26d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{8d,8d,6d,6d,14d},
			{1d,0d,0d,0d,1d},
			{-1d,0d,0d,0d,0d},
			{0d,1d,0d,0d,1d},
			{0d,-1d,0d,0d,0d},
			{0d,0d,1d,0d,1d},
			{0d,0d,-1d,0d,0d},
			{0d,0d,0d,1d,1d},
			{0d,0d,0d,-1d,0d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {};
		BendersMasterCoefficientType[] yTypes = {};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 46d;
		double[] parameterResults = {0d, 1d, 0d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/*
	 * 0 1 Problem
	 * 
	 * 	Double [][] array= {
				{30d,50d,20d,70d},
				{1d,0d,0d,1d},
				{-1d,0d,0d,0d},
				{0d,1d,0d,1d},
				{0d,-1d,0d,0d},
				{0d,0d,1d,1d},
				{0d,0d,-1d,0d},
				{-3d,-5d,-2d,0d}};
	 * --> [0.0, 1.0, 1.0, 7.0]		
	 */
//	@Test
	void test_example_08() {
		System.out.println("test_example_08");
		// prepare test data
		Double[] function = {-3d,-5d,-2d,0d};
		Double[][] simplexTableau = new Double[][] {
			{30d,50d,20d,70d},
			{1d,0d,0d,1d},
			{-1d,0d,0d,0d},
			{0d,1d,0d,1d},
			{0d,-1d,0d,0d},
			{0d,0d,1d,1d},
			{0d,0d,-1d,0d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {2};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Float};
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 7d;
		double[] parameterResults = {0d, 1d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/*
	 * BINÄR
		Double [][] array= {
				{6d,3d,5d,2d,10d},
				{0d,0d,1d,1d,1d},
				{-1d,0d,1d,0d,0d},
				{0d,-1d,0d,1d,0d},
				{1d,0d,0d,0d,1d},		
				{-1d,0d,0d,0d,0d},
				{0d,1d,0d,0d,1d},
				{0d,-1d,0d,0d,0d},
				{0d,0d,1d,0d,1d},
				{0d,0d,-1d,0d,0d},
				{0d,0d,0d,1d,1d},
				{0d,0d,0d,-1d,0d},
				
				{-9d,-5d,6d,4d,0d}};
	 * --> [1.0, 1.0, 0.0, 0.0, 14.0]
	 * https://members.loria.fr/CRingeissen/files/master2/solving-lp-ip.pdf
	 */
//	@Test
	void test_example_09() {
		System.out.println("test_example_09");
		// prepare test data
		Double[] function = {-9d,-5d,6d,4d,0d};
		Double[][] simplexTableau = new Double[][] {
			{6d,3d,5d,2d,10d},
			{0d,0d,1d,1d,1d},
			{-1d,0d,1d,0d,0d},
			{0d,-1d,0d,1d,0d},
			{1d,0d,0d,0d,1d},		
			{-1d,0d,0d,0d,0d},
			{0d,1d,0d,0d,1d},
			{0d,-1d,0d,0d,0d},
			{0d,0d,1d,0d,1d},
			{0d,0d,-1d,0d,0d},
			{0d,0d,0d,1d,1d},
			{0d,0d,0d,-1d,0d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {};
		BendersMasterCoefficientType[] yTypes = {};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 14d;
		double[] parameterResults = {1d, 1d, 0d, 0d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/*
	 * https://members.loria.fr/CRingeissen/files/master2/solving-lp-ip.pdf
	 */
//	@Test
	void test_example_10() {
		System.out.println("test_example_10");
		// prepare test data
		Double[] function = {11d,14d,0d,0d,0d, 0d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{1d, 1d, 1d, 0d, 0d, 0d, 17d},
			{3d, 7d, 0d, 1d, 0d, 0d, 63d},
			{3d, 5d, 0d, 0d, 1d, 0d, 48d},
			{3d, 1d, 0d, 0d, 0d, 1d, 30d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {2, 3, 4, 5};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary,
					BendersMasterCoefficientType.Binary,BendersMasterCoefficientType.Binary,
					BendersMasterCoefficientType.Binary};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			
			double result = 0d;
			double[] parameterResults = {0d, 0d, 0d, 0d, 0d, 0d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/*
	 * ganzzahliges problem 
		Double [][] array= {
				{2d,3d,0d,0d,0d,5d},				
				{0d,0d,3d,4d,0d,7d},
				{-2d,0d,-2d,0d,-2d,-7d},
				{7d,-3d,5d,-4d, 2d,0d},
		};
	 *http://www.hs-augsburg.de/informatik/projekte/mebib/emiel/entw_inf/or_verf/ganzopt_bab.html
	 */
//	@Test
	void test_example_11() {
		System.out.println("test_example_11");
		// prepare test data
		Double[] function = {7d,-3d,5d,-4d, 2d,0d};
		Double[][] simplexTableau = new Double[][] {
			{2d,3d,0d,0d,0d,5d},				
			{0d,0d,3d,4d,0d,7d},
			{-2d,0d,-2d,0d,-2d,-7d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {2};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			new BendersAlgorithm().solve(inputData);
			double result = -1d;
			double[] parameterResults = {0d, 1d, 0d, 1d, 4d};
			
			testInput(inputData, result, parameterResults);
	}
	/*
Double [][] array= {											     6 ebenen baum
			{6d,4d,15d},
			{-21d,-11d,0d}
		};
	 * -> 1 2 43
	 * https://www.wiwi.uni-frankfurt.de/profs/ohse/lehre/downloads/qmbwl_folien/Microsoft%2BWord%2B-%2BKapitel5OR3.pdf
	 */
//	@Test
	void test_example_12() {
		System.out.println("test_example_12");
		// prepare test data
		Double[] function = {-21d,-11d,0d};
		Double[][] simplexTableau = new Double[][] {
			{6d,4d,15d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {0};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			new BendersAlgorithm().solve(inputData);
			double result = 43d;
			double[] parameterResults = {1d, 2d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/*
	Beispiel  max, x1 = 12 x2 = 24   zf= 28000, Baum mit 4 ebenen
	{100d,50d,2425d},
			{0d, 20d, 510d},
			{-1000d,-700d,0d}
		};
	 * https://kops.uni-konstanz.de/bitstream/handle/123456789/40602/Jaekle_2--19g2w6rkcqybq4.pdf?sequence=5
	 */
//	@Test
	void test_example_13() {
		System.out.println("test_example_13");
		// prepare test data
		Double[] function = {-1000d,-700d,0d};
		Double[][] simplexTableau = new Double[][] {
			{100d,50d,2425d},
			{0d, 20d, 510d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {1};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			new BendersAlgorithm().solve(inputData);
			double result = 29250d;
			double[] parameterResults = {11.75d, 25d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/*
	 * max problem
		Double [][] array= {
						{3d,2d,6d},
						{5d,2d,8d},
						{-2d,-1d,0d}
		};
	 * 	[1.0, 1.0, 3.0]
	 *	Alternative Lösungen:
	 *	[0.0, 3.0, 3.0]
	 * https://www.ingenieurkurse.de/operations-research-2/ganzzahlige-optimierung/branch-and-bound-verfahren/maximierungsprobleme/branch-and-bound-am-maximierungsproblem-optimale-loesung/beispiel-branch-and-bound-am-maximierungsproblem-optimale-loesung.html
	 */
//	@Test
	void test_example_14() {
		System.out.println("test_example_14");
		// prepare test data
		Double[] function = {-2d,-1d,0d};
		Double[][] simplexTableau = new Double[][] {
			{3d,2d,6d},
			{5d,2d,8d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {1};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Float};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			new BendersAlgorithm().solve(inputData);
			double result = 3d;
			double[] parameterResults = {1d, 1d};
			
			testInput(inputData, result, parameterResults);
	}
	
/*
 *   2, 2, ZF= 6; 
		Double [][] array= {					
				
				{-10d,20d,22d},
				{5d,10d,49d},
				{1d,0d,5d},
				
				{1d,-4d,0d}
	https://www.ie.bilkent.edu.tr/~mustafap/courses/bb.pdf
 */
//	@Test
	void test_example_15() {
		System.out.println("test_example_15");
		// prepare test data
		Double[] function = {1d,-4d,0d};
		Double[][] simplexTableau = new Double[][] {
			{-10d,20d,22d},
			{5d,10d,49d},
			{1d,0d,5d},
			function};
			int[] paramaterNegativeIndices = {};
			int[] yVariableIndices = {0};
			BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
			
			BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
					paramaterNegativeIndices, yVariableIndices, yTypes);
			new BendersAlgorithm().solve(inputData);
			double result = 6d;
			double[] parameterResults = {2d, 2d};
			
			testInput(inputData, result, parameterResults);
	}
	
	void testInput(BendersOptimizationData input, double expectedResult, double[] expectedParameterResults) {
		IBendersOptimizationSolutionData solutionData = new BendersAlgorithm().solve(input);
		Double[] optSolution = solutionData.getOptSolution();
		double result = optSolution[optSolution.length - 1];
		Double[] parameterResults = Arrays.copyOfRange(optSolution, 0, optSolution.length - 1);
		double[] parameterResultUnboxed = Stream.of(parameterResults).mapToDouble(Double::doubleValue).toArray();

		assertArrayEquals(expectedParameterResults, parameterResultUnboxed, DELTA);
		assertEquals(expectedResult, result, DELTA);
	}
}
