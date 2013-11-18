package triangulation;

import base.Point;
import base.Polyedre;

public class Main {

	public static void main(String[] args) {
		Point[] pts = {new Point(0, 3),new Point(2, 2), new Point(4, 3), new Point(2, 0)};
		Polyedre p = new Polyedre(pts);
		Triangulation.partitionning_polygon(p);
	}

}