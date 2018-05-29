package FPP.LinearOptimization.Model.dantzig_wolfe;
public class Point {
	double x, y;

	Point(double x, double y) {
		this.x = x + 0;
		this.y = y + 0;
	}

	@Override
	public String toString() {
		return String.format("{%f, %f}", x, y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}