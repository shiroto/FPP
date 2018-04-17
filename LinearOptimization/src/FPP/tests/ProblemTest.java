package FPP.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import FPP.LinearOptimization.Model.benders.MasterProblem;
import FPP.LinearOptimization.Model.benders.SubProblem;

@SuppressWarnings("deprecation")
class ProblemTest {

	@Test
	void test_NoRestriction() {
		SubProblem problem = new SubProblem(new Double[] {-200d, -50d, -80d, -500d});
		Double[][] resultRestrictions = problem.getRestrictions();
		assertEquals(new Double[0][0], resultRestrictions);
	}
	
	@Test
	void test_WrongRestriction() {
		SubProblem problem = new SubProblem(new Double[] {-200d, -50d, -80d, -500d});
		Double[][] resultRestrictions = problem.getRestrictions();
		assertEquals(new Double[0][0], resultRestrictions);
	}
	
	@Test
	void test_multipleRestrictions() {
		Double[] function = new Double[] {-200d, -50d, -80d, -500d};
		MasterProblem problem = new MasterProblem(function);
		Double[][] restrictions = {
				{-3d, 2d, 1d, 10d},
				{-1d, 2d, 3d, 4d},
				{4d, -3d, 2d, -1d}};
		for (Double[] ds : restrictions) {
			problem.addRestriction(ds);
		}
		
		Double[] resultFunction = problem.getFunction();
		Double[][] resultRestrictions = problem.getRestrictions();
		assertEquals(function, resultFunction);
		assertEquals(restrictions, resultRestrictions);
		System.out.println(Arrays.toString(resultFunction));
		System.out.println(Arrays.deepToString(resultRestrictions));
	}
	
	@Test
	void test_simplexArray() {
		Double[] function = new Double[] {-200d, -50d, -80d, -500d};
		MasterProblem problem = new MasterProblem(function);
		Double[][] restrictions = {
				{-3d, 2d, 1d, 10d},
				{-1d, 2d, 3d, 4d},
				{4d, -3d, 2d, -1d}};
		for (Double[] ds : restrictions) {
			problem.addRestriction(ds);
		}
		
		Double[][] simplexArray = problem.getSimplexArray();
		assertEquals(new Double[][] {
				{-3d, 2d, 1d, 10d},
				{-1d, 2d, 3d, 4d},
				{4d, -3d, 2d, -1d},
				{-200d, -50d, -80d, -500d}}, simplexArray);
		System.out.println(Arrays.deepToString(simplexArray));
	}
}