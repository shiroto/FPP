package FPP.LinearOptimization.Model.DantzigWolfe;
import java.util.ArrayList;

public class Intersections {
	ArrayList<Line> lines = new ArrayList<>();
	ArrayList<Point> points = new ArrayList<>();

	Line x = new Line(0, 1, 0);
	Line y = new Line(1, 0, 0);

	public Intersections(ArrayList<Line> lines) {
		this.lines.add(x);
		this.lines.addAll(lines);
		this.lines.add(y);
	}

	public ArrayList<Point> getIntersections() {
		points.add(x.findIntersection(y));

		int currentLine = 1;
		int nextLine = 1;

		// First iteration with (0, 0)
		for (int i = currentLine; i < lines.size(); i++) {
			if (distance(x.findIntersection(y), x.findIntersection(lines.get(i))) < distance(x.findIntersection(y),
					x.findIntersection(lines.get(i)))) {
				nextLine = i;
			}
		}
		points.add(x.findIntersection(lines.get(nextLine)));

		for (int j = 0; j < lines.size() - 3; j++) {
			currentLine = nextLine;
			for (int i = currentLine + 1; i < lines.size() - 1; i++) {
				if (distance(lines.get(currentLine).findIntersection(lines.get(i)),
						lines.get(currentLine).findIntersection(lines.get(i + 1))) < distance(
								lines.get(currentLine).findIntersection(lines.get(i)),
								lines.get(currentLine).findIntersection(lines.get(i + 1)))) {
					nextLine = i;
				}
			}
			points.add(lines.get(currentLine + 1).findIntersection(lines.get(nextLine)));
		}

		// Intersection with y-Axes
		points.add(lines.get(nextLine + 1).findIntersection(y));

		return points;
	}

	private double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow((p2.y - p1.y), 2) + Math.pow((p2.x - p1.x), 2));
	}
}
