package geometry.arbreDependance;

import geometry.model.Point;
import geometry.model.Segment;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class TrapezoidalMap {

	private Trapezoid root;
	private List<Trapezoid> trapezoids;

	public TrapezoidalMap(Rectangle2D.Double bounds) {
		root = new Trapezoid(null, null, null, null);
		trapezoids = new ArrayList<Trapezoid>();
		trapezoids.add(root);
	}

	public Trapezoid getRoot() {
		return root;
	}

	/**
	 * Page 138.
	 */
	public SplitTrapezoid split(Trapezoid t, Segment s) {
		// Remove the trapezoid from the list.
		trapezoids.remove(t);
		// Split it.
		SplitTrapezoid l = t.split(s);
		// Add the new trapezoids.
		if (l.a != null)
			trapezoids.add(l.a);
		if (l.b != null)
			trapezoids.add(l.b);
		trapezoids.add(l.c);
		trapezoids.add(l.d);
		return l;
	}

	/**
	 * Page 139.
	 */
	public void split2(Trapezoid[] l, Segment s) {
		// Left point and right point.
		Point p = new Point(s.debut);
		Point q = new Point(s.fin);
		if (q.x < p.x) {
			Point tmp = q;
			q = p;
			p = tmp;
		}
		// Split first trapezoid.
		Trapezoid t = l[0];
		Trapezoid[] left = null;
		if (t.left.equals(p)) {
			// Point already present.
			left = new Trapezoid[2];
			left[0] = new Trapezoid(t.left, l[1].right, t.top, t.bottom);
		} else {
			// Point not present.
			left = new Trapezoid[1];
			left[0] = new Trapezoid(t.left, p, t.top, t.bottom);
		}
		// Split last trapezoid.
		Trapezoid r = l[l.length - 1];
		Trapezoid[] right = null;
		if (r.right.equals(q)) {
			// Point already present.
		} else {
			// Point not present.
			right = new Trapezoid[1];
			right[0] = new Trapezoid(q, r.right, r.top, r.bottom);
		}
	}
}
