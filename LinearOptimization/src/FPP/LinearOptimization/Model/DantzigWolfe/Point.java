package FPP.LinearOptimization.Model.DantzigWolfe;
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
}