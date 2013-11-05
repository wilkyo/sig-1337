package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class Polyedre {
	public Point[] points;

	public Polyedre(Point[] points) {
		super();
		this.points = points;
	}

	public boolean contains(Point p) {
		return true;
	}

	public ArrayList<Segment> envellopeConvexN3() {
		ArrayList<Segment> res = new ArrayList<Segment>();

		for (Point p1 : points) {
			for (Point p2 : points) {
				Segment s = new Segment(p1, p2);
				boolean ok = true;
				for (Point p3 : points) {
					if (s.produitVectoriel(new Segment(p1, p3)) < 0) {
						ok = false;
						break;
					}
				}
				if (ok)
					res.add(s);
			}
		}
		return res;
	}

	/**
	 * Implementation de l'envellope convexe en O(n log(n))
	 * 
	 * @TODO: Finir l'implementation
	 * @return
	 */
	public ArrayList<Segment> envellopeConvex() {
		ArrayList<Segment> res = new ArrayList<Segment>();
		ArrayList<Point> pointtrie = new ArrayList<Point>();
		Point P = points[0];
		int index = 0;
		int i = 0;
		for (Point p : points) {
			if (P.y > p.y) {
				P = p;
				index = i;
			} else if (P.y == p.y && P.x > P.x) {
				P = p;
				index = i;
			}
			i++;
		}
		points[index] = points[0];
		points[0] = P;
		final Point p1 = P;
		Point[] tabs = points.clone();
		Point x = new Point(0, P.y);
		final Segment xp = new Segment(P, x);
		Arrays.sort(tabs, 1, tabs.length, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				Segment Po1 = new Segment(p1, o1);
				Segment Po2 = new Segment(p1, o2);
				double p1 = Math.acos(xp.produitScalaire(Po1)
						/ (xp.longueur() * Po1.longueur()));
				double p2 = Math.acos(xp.produitScalaire(Po2)
						/ (xp.longueur() * Po2.longueur()));
				return (int) (p1 - p2);
			}
		});
		Vector<Point> pile = new Vector<Point>();
		pile.add(P);
		pile.add(tabs[1]);
		return null;
	}

	private float produitvectoriel(Point p1, Point p2, Point p3) {
		return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
	}

	public boolean agauche(Segment s) {
		return false;
	}

	/**
	 * Returns the list of triangles composing the Polygon.
	 * 
	 * @return List<Polyedre> of the triangles.
	 */
	public List<Polyedre> toTriangles() {
		List<Polyedre> triangles = new ArrayList<Polyedre>();
		// TODO Bouchon
		Point[] first = new Point[] { points[0], points[1], points[2] };
		triangles.add(new Polyedre(first));
		return triangles;
	}
}
