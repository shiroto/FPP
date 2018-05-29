package FPP.LinearOptimization.Model.dantzig_wolfe;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class IntersectionsTest {

	@Test
	void testGetIntersections_A() {
		System.out.println("testGetIntersections_A");
		
		Line x = new Line(0, 1, 0);
		Line y = new Line(1, 0, 0);
		
		Line l1 = new Line(1, 2, 200);
		Line l2 = new Line(1, 0, 50);

		ArrayList<Line> linesA = new ArrayList<>();
		linesA.add(l2);
		linesA.add(l1);
		Intersections a = new Intersections(linesA);
		ArrayList<Point> pointsTest = new ArrayList<>();
		pointsTest.add(x.findIntersection(y));
		pointsTest.add(x.findIntersection(l2));
		pointsTest.add(l2.findIntersection(l1));
		pointsTest.add(y.findIntersection(l1));
		ArrayList<Point> pointsA = a.getIntersections();
		
		assertEquals(0,  pointsA.get(0).getX(), 0.0001);
	}

	@Test
	void testGetIntersections_B() {
		System.out.println("testGetIntersections_B");
		
		Line x = new Line(0, 1, 0);
		Line y = new Line(1, 0, 0);

		Line l3 = new Line(1, 0, 80);
		Line l4 = new Line(3, 10, 500);
		
		ArrayList<Line> linesB = new ArrayList<>();
		linesB.add(l3);
		linesB.add(l4);
		Intersections b = new Intersections(linesB);
		ArrayList<Point> pointsTest = new ArrayList<>();
		pointsTest.add(x.findIntersection(y));
		pointsTest.add(x.findIntersection(l3));
		pointsTest.add(l4.findIntersection(l3));
		pointsTest.add(y.findIntersection(l4));
		ArrayList<Point> pointsB = b.getIntersections();
		
		assertEquals(0,  pointsB.get(0).getX(), 0.0001);
	}

}
