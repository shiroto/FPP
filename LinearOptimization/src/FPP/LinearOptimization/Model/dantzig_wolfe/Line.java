package FPP.LinearOptimization.Model.dantzig_wolfe;
public class Line {

	// double a, b, c;
	Point e, s;

	Line(double a, double b, double c) {
		// this.a = a;
		// this.b = b;
		// this.c = c;

		if (b != 0) {
			this.e = new Point(0, c / b);
		} else {
			this.e = new Point(c / a, 10);
		}

		if (a != 0) {
			this.s = new Point(c / a, 0);
		} else {
			this.s = new Point(10, c / b);
		}
	}

	public Point findIntersection(Line l2) {
		Line l1 = this;
		double a1 = l1.e.y - l1.s.y;
		double b1 = l1.s.x - l1.e.x;
		double c1 = a1 * l1.s.x + b1 * l1.s.y;

		double a2 = l2.e.y - l2.s.y;
		double b2 = l2.s.x - l2.e.x;
		double c2 = a2 * l2.s.x + b2 * l2.s.y;

		double delta = a1 * b2 - a2 * b1;
		return new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
	}
}
