package FPP.LinearOptimization.Model.dantzig_wolfe;

import java.util.ArrayList;

public class DantzigWolfeAlgorithm {

	public static void main(String[] args) {
		Line x = new Line(0, 1, 0);
		Line y = new Line(1, 0, 0);

		Line l1 = new Line(1, 2, 200);
		Line l2 = new Line(1, 0, 50);

		ArrayList<Line> linesA = new ArrayList<>();
		linesA.add(l2);
		linesA.add(l1);
		Intersections a = new Intersections(linesA);
		ArrayList<Point> pointsA = a.getIntersections();

		Line l3 = new Line(1, 0, 80);
		Line l4 = new Line(3, 10, 500);
		
		ArrayList<Line> linesB = new ArrayList<>();
		linesB.add(l3);
		linesB.add(l4);
		Intersections b = new Intersections(linesB);
		ArrayList<Point> pointsB = b.getIntersections();

		System.out.println("Eckpunkte von M1: " + x.findIntersection(y) + ", " + x.findIntersection(l2) + ", "
				+ l2.findIntersection(l1) + ", " + y.findIntersection(l1));
		System.out.println("Eckpunkte von M1: " + pointsA.get(0) + ", " + pointsA.get(1) + ", " + pointsA.get(2) + ", "
				+ pointsA.get(3));
		System.out.println("Eckpunkte von M2: " + x.findIntersection(y) + ", " + x.findIntersection(l3) + ", "
				+ l4.findIntersection(l3) + ", " + y.findIntersection(l4));
		System.out.println("Eckpunkte von M2: " + pointsB.get(0) + ", " + pointsB.get(1) + ", " + pointsB.get(2) + ", "
				+ pointsB.get(3));
	}
	
}
