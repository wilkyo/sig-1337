package geometry.model;

public class OrderedSegment extends Segment {

	private static Point min(Point p1, Point p2) {
		return p1.x < p2.x ? p1 : p2;
	}

	private static Point max(Point p1, Point p2) {
		return p1.x > p2.x ? p1 : p2;
	}

	public OrderedSegment(Point debut, Point fin) {
		super(min(debut, fin), max(debut, fin));
	}

}
