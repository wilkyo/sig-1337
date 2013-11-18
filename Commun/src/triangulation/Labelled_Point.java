package triangulation;

import base.Point;

public class Labelled_Point extends Point {
	public static enum Point_Type {
		START, END, SPLIT, MERGE, REGULAR
	};

	private Point_Type type;
	
	public Labelled_Point(float x, float y, Point_Type pt) {
		super(x, y);
		type = pt;
	}
 	
	public Labelled_Point(Point p, Point_Type pt) {
		super(p);
		type = pt;
	}
	
	public Point_Type getType() {
		return type;
	}
	
	public void setType(Point_Type pt) {
		type = pt;
	}

	@Override
	public String toString() {
		return "(" + x + " " + y + ") :" + type;
	}
}
