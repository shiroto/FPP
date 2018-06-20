package FPP.LinearOptimization.Model.benders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Data.BendersOptimizationData;
import FPP.LinearOptimization.Data.IBendersOptimizationSolutionData;
/**
 * <h1>This class tests the {@link BendersAlgorithm} in all variants.</h1>
 * <strong>Make sure this tests are working when changing the algorithm!</strong><br>
 * This class contains all known test cases, so if new cases are known you should add them here.
 */
public class BendersAlgorithmTest {
	
	private static final double DELTA = 1e-12;
	
	/**
	 * <h3>1 binary y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * 200 * x1 + 50 * x2 + 80 * x3 + 500 * x4 + 180 * y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - x1 - x2 - 2 * y1 <= -10<br>
	 * - 2 * x1  - 2 * y1 <= -10<br>
	 * - x3 - 3 * x4 - 0,5 * y <= -2<br>
	 * - 10 * x4 - y <= -6<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 1430
	 * <li> coefficients: [4.0, 4.0, 0.0, 0.5, 1.0]</ul><p>
	 */
	@Test
	void test_single_binary_01() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single binary / presentation");
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
		
		double result = 1430;
		double[] parameterResults = {4d, 4d, 0d, 0.5d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 float y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * 200 * x1 + 50 * x2 + 80 * x3 + 500 * x4 + 180 * y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - x1 - x2 - 2 * y1 <= -10<br>
	 * - 2 * x1  - 2 * y1 <= -10<br>
	 * - x3 - 3 * x4 - 0,5 * y <= -2<br>
	 * - 10 * x4 - y <= -6<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 950.0
	 * <li> coefficients: [0.0, 0.0, 0.0, 0.1, 5.0]</ul><p>
	 */
	@Test
	void test_single_float_01() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single float / presentation");
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
		
		double result = 950d;
		double[] parameterResults = {0d, 0d, 0d, 0.1, 5d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 integer y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - x1 - 2 * x2 - 3 * x3 - y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - x1 + x2 + x3 + 10 * y1 <= 20<br>
	 * x1 - 3 * x2 + x3 <= 30<br>
	 * x2 - 3.5 * y1 <= 0<br>
	 * - x2 + 3.5 * y1 <= 0<br>
	 * x1 <= 40<br>
	 * - y1 <= -2<br>
	 * y1 <= 3<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -122.5
	 * <li> coefficients: [40.0, 10.5, 19.5, 3.0]</ul><p>
	 */
	@Test
	void test_single_integer_01() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single integer");
		// prepare test data
		Double[] function = {-1d, -2d, -3d, -1d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{-1d, 1d, 1d, 10d, 20d},
			{1d, -3d, 1d, 0d, 30d},
			{0d, 1d, 0d, -3.5d, 0d},
			{0d, -1d, 0d, 3.5d, 0d},
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
	
	/**
	 * <h3>Multiple binary y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 8 * x1 - 6 * x2 - x3 - 10 * y1 - 8 * y2 + 33 * y3 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 2 * x1 + x2 - x3 - 10 * y1 - 8 * y2 <= -4<br>
	 * x1 + x2 + x3 - 5 * y1 - 8 * y3 <= -3<br>
	 * y1 <= 1<br>
	 * y2 <= 1<br>
	 * y3 <= 1<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 25.0
	 * <li> coefficients: [0.0, 4.5, 0.5, 0.0, 1.0, 1.0]</ul><p>
	 */
	@Test
	void test_multiple_binary_01() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("multiple binary / paper");
		// prepare test data
		Double[] function = {-8d, -6d, 2d, 42d, 18d, 33d, 0d};
		Double[][] simplexTableau = new Double[][] {
			{2d, 1d, -1d, -10d, -8d, 0d, -4d},
			{1d, 1d, 1d, -5d, 0d, -8d, -3d},
			{0d, 0d, 0d, 1d, 0d, 0d, 1d},
			{0d, 0d, 0d, 0d, 1d, 0d, 1d},
			{0d, 0d, 0d, 0d, 0d, 1d, 1d}, 
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
	
	/**
	 * <h3>Multiple integer y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - y1 - y2 - 3 * x1 - 2 * x2 - 2 * y3 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - y1 - y2 + x1 + x2 <= 30<br>
	 * - y1 + x1 - 3 * x2  <= 30<br>
	 * y1 <= 40<br>
	 * y2 <= 1<br>
	 * y3 <= 1<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -235.75
	 * <li> coefficients: [40.0, 1.0, 50.75, 20.25, 1.0]</ul><p>
	 */
	@Test
	void test_multiple_integer_01() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("multiple integer");
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
		double[] parameterResults = {40d, 1d, 50.75d, 20.25d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>Multiple integer y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - x1 - y1 - 3 * x2 - 2 * x3 - 2 * y2 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - x1 - y1 + x2 + x3 <= 30<br>
	 * x1 + x2 - 3 * x3 <= 30<br>
	 * - y1 + x2 <= 30<br>
	 * x1 <= 40<br>
	 * y1 <= 1<br>
	 * y2 <= 1<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -214.0
	 * <li> coefficients: [40.0, 1.0, 31.0, 40.0, 1.0]</ul><p>
	 */
	@Test
	void test_multiple_integer_02() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("multiple integer");
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
		double[] parameterResults = {40d, 1d, 31d, 40d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>Single binary y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 3 * x1 - 2 * x2 - y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * x1 + x2 + y1 <= 7<br>
	 * 4 * x1 + 2 * x2 + y1 <= 12<br>
	 * - 4 * x1 - 2 * x2 - y1 <= -12<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -12.0
	 * <li> coefficients: [0.0, 6.0, 0.0]</ul><p>
	 */
	@Test
	void test_single_binary_02() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single binary");
		// prepare test data
		Double[] function = {-3d, -2d, -1d, 0d};
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
		
		double result = -12d;
		double[] parameterResults = {0d, 6d, 0d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 integer y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * 2 * x1 + y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * - 4 * x1 - 4 * y1 <= -10<br>
	 * - 2 * x1 - 11 * y1 <= -11<br>
	 * - 4 * x1 - 2 * y1 <= 1<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 3.5
	 * <li> coefficients: [0.75, 2.0]</ul><p>
	 */
	@Test
	void test_single_integer_02() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single integer");
		// prepare test data
		Double[] function = {2d,1d,0d};
		Double[][] simplexTableau = new Double[][] {
			{-4d,-4d,-10d},
			{-2d,-11d,-11d},
			{-4d,2d,1d},
			function};
		int[] paramaterNegativeIndices = {};
		int[] yVariableIndices = {1};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = 3.5d;
		double[] parameterResults = {0.75d, 2d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>Multiple mixed y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 14 * x1 - 20 * x2 - 18 * y1 - 26 * y2 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 8 * x1 + 8 * x2 + 6 * y1 + 6 * y2 <= 14<br>
	 * x1 <= 1<br>
	 * - x1 <= 0<br>
	 * x2 <= 1<br>
	 * - x2 <= 0<br>
	 * y1 <= 1<br>
	 * - y1 <= 0<br>
	 * y2 <= 1<br>
	 * - y2 <= 0<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 3.5
	 * <li> coefficients: [0.75, 2.0]</ul><p>
	 */
	@Test
	void test_multiple_mixed_02() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("multiple mixed");
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
		int[] yVariableIndices = {2, 3};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Binary,
				BendersMasterCoefficientType.Integer};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -49d;
		double[] parameterResults = {0d, 0.25d, 1d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 float y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 3 * x1 - 5 * x2 - 2 * y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 30 * x1 + 50 * x2 + 20 * y1 <= 70<br>
	 * x1 <= 1<br>
	 * - x1 <= 0<br>
	 * x2 <= 1<br>
	 * - x2 <= 0<br>
	 * y1 <= 1<br>
	 * - y1 <= 0<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -7.0
	 * <li> coefficients: [0.0, 1.0, 1.0]</ul><p>
	 */
	@Test
	void test_single_float_02() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single float");
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
		
		double result = -7d;
		double[] parameterResults = {0d, 1d, 1d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>Multiple float y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 9 * x1 - 5 * y1 + 6 * y2 + 4 * y3 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 6 * x1 + 3 * y1 + 5 * y2 + 2 * y3 <= 10<br>
	 * y2 + y3 <= 1<br>
	 * - x1 + y2 <= 0<br>
	 * - y1 + y3 <= 0<br>
	 * x1 <= 1<br>
	 * - x1 <= 0<br>
	 * y1 <= 1<br>
	 * - y1 <= 0<br>
	 * y2 <= 1<br>
	 * - y2 <= 0<br>
	 * y3 <= 1<br>
	 * - y3 <= 0<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -14.0
	 * <li> coefficients: [1.0, 1.0, 0.0, 0.0]</ul><p>
	 */
	@Test
	void test_multiple_float() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("multiple float");
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
		int[] yVariableIndices = {1, 2, 3};
		BendersMasterCoefficientType[] yTypes = {BendersMasterCoefficientType.Float, 
				BendersMasterCoefficientType.Float, BendersMasterCoefficientType.Float};
		
		BendersOptimizationData inputData = new BendersOptimizationData(simplexTableau, 
				paramaterNegativeIndices, yVariableIndices, yTypes);
		
		double result = -14d;
		double[] parameterResults = {1d, 1d, 0d, 0d};
		
		testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>No y-variables in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * 11 * x1 + 14 * x2  <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * x1 + x2 + y1 <= 17<br>
	 * 3 * x1 + 7 * x2 + y2 <= 63<br>
	 * 3 * x1 + 5 * x2 + y3 <= 48<br>
	 * 3 * x1 + x2 + y4 <= 30<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 0.0
	 * <li> coefficients: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0]</ul><p>
	 */
	@Test
	void test_no_y() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("no y in master");
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
	
	/**
	 * <h3>1 integer y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * -7 * x1 - 3 * x2 + 5 * y1 - 4 * x3 + 2 * x4 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 2 * x1 + 3 * x2 <= 5<br>
	 * y1 + 4 * x3 <= 7<br>
	 * - 2 * x1 - 2 * y1 - 2 * x4 <= -7<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -5.0
	 * <li> coefficients: [0.0, 5.0/3.0, 0.0, 1.75, 3.5]</ul><p>
	 */
	@Test
	void test_single_integer_03() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single integer");
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
			double result = -5d;
			double[] parameterResults = {0d, 5d/3d, 0d, 1.75d, 3.5d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>master problem unbounded</h3>
	 * <b><u>function:</b></u><br>
	 * - 21 * y1 - 11 * x1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 6 * y1 + 4 * x1 <= 15<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>master problem is unbound!
	 */
	@Test
	void test_master_unbounded() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("master problem unbounded");
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
			
			IBendersOptimizationSolutionData solutionData = new BendersAlgorithm().solve(inputData);
			assertEquals(solutionData.getAddInfo(), BendersAlgorithm.MASTER_PROBLEM_UNBOUNDED_MESSAGE);
			assertNull(solutionData.getOptSolution());
			assertTrue(solutionData.getSteps().isEmpty());
	}
	
	/**
	 * <h3>1 integer y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 1000 * x1 - 700 * y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 100 * x1 + 50 * y1 <= 2425<br>
	 * 20 * y1 <= 510<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 29250.0
	 * <li> coefficients: [11.75, 25.0]</ul><p>
	 */
	@Test
	void test_single_integer_04() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single integer");
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
			double result = -29250d;
			double[] parameterResults = {11.75d, 25d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 float y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * - 2 * x1 - y1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * 3 * x1 + 2 * y1 <= 6<br>
	 * 5 * x1 + 2 * y2 <= 8<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -3.5
	 * <li> coefficients: [1.0, 1.5]</ul><p>
	 */
	@Test
	void test_single_float_03() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single float");
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
			double result = -3.5d;
			double[] parameterResults = {1d, 1.5d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>1 integer y-variable in the master problem</h3>
	 * <b><u>function:</b></u><br>
	 * y1 - 4 * x1 <= 0<br>
	 * <b><u>restrictions:</b></u><br>
	 * -10 * y1 + 20 * x1 <= 22<br>
	 * 5 * y1 + 10 * x1 <= 49<br>
	 * y1 <= 5<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: -7.6
	 * <li> coefficients: [4d, 2.9d]</ul><p>
	 */
	@Test
	void test_single_integer_05() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("single integer");
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
			double result = -7.6d;
			double[] parameterResults = {4d, 2.9d};
			
			testInput(inputData, result, parameterResults);
	}
	
	/**
	 * <h3>constant in problem</h3>
	 * <b><u>function:</b></u><br>
	 * y1 - 4 * x1 <= 7.6<br>
	 * <b><u>restrictions:</b></u><br>
	 * -10 * y1 + 20 * x1 <= 22<br>
	 * 5 * y1 + 10 * x1 <= 49<br>
	 * y1 <= 5<br>
	 * <br><b><u>result:</b></u><p><ul>
	 * <li>optimal solution: 0
	 * <li> coefficients: [4d, 2.9d]</ul><p>
	 */
	@Test
	void test_constant() {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("constant in problem");
		// prepare test data
		Double[] function = {1d,-4d,7.6d};
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
			double result = 0d;
			double[] parameterResults = {4d, 2.9d};
			
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
