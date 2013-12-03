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

	public List<Trapezoid> getTrapezoids() {
		return trapezoids;
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
	public Split2Trapezoid split2(Trapezoid[] l, Segment s) {
		// Remove initial trapezoids.
		for (int i = 0; i < l.length; ++i) {
			trapezoids.remove(l[i]);
		}
		Trapezoid left = l[0];
		Trapezoid right = l[l.length - 1];
		Split2Trapezoid st = new Split2Trapezoid(l, s);
		// Left point and right point.
		Point p = new Point(s.debut);
		Point q = new Point(s.fin);
		if (q.x < p.x) {
			Point tmp = q;
			q = p;
			p = tmp;
		}
		// Create left and right trapezoids.
		if (!p.equals(left.left)) {
			st.left = new Trapezoid(left.left, p, left.top, left.bottom);
			st.left.leftTopNeighbor = left.leftTopNeighbor;
			st.left.leftBottomNeighbor = left.leftBottomNeighbor;
			trapezoids.add(st.left);
		}
		if (!p.equals(right.right)) {
			st.right = new Trapezoid(q, right.right, right.top, right.bottom);
			st.right.rightTopNeighbor = right.rightTopNeighbor;
			st.right.rightBottomNeighbor = right.rightBottomNeighbor;
			trapezoids.add(st.right);
		}
		// Create bottom trapezoids.
		st.bottom = new Trapezoid[l.length];
		Point p2 = p;
		for (int i = 0, j = 0; i < l.length; ++i) {
			Trapezoid t = l[i];
			if (i == l.length - 1) {
				// Last trapezoid.
				Trapezoid t2 = new Trapezoid(p2, q, s, t.bottom);
				for (; j <= i; ++j) {
					st.bottom[j] = t2;
				}
				trapezoids.add(t2);
			} else if (!s.auDessus(t.right)) {
				// If the right point is below the segment.
				Trapezoid t2 = new Trapezoid(p2, t.right, s, t.bottom);
				// If it isn't the first trapezoid.
				for (; j <= i; ++j) {
					st.bottom[j] = t2;
				}
				trapezoids.add(t2);
				p2 = t.right;
			}
		}
		// Link bottom trapezoids.
		Trapezoid item0 = null;
		for (int i = 0; i < st.bottom.length; ++i) {
			Trapezoid item = st.bottom[i];
			if (i == 0) {
				// First trapezoid.
				if (st.left != null) {
					st.left.rightBottomNeighbor = item;
					item.leftBottomNeighbor = st.left;
					item.leftTopNeighbor = st.left;
				} else {
					item.leftBottomNeighbor = null;
					item.leftTopNeighbor = null;
				}
			} else if (i == st.bottom.length - 1) {
				// Last trapezoid.
				if (st.right != null) {
					st.right.leftBottomNeighbor = item;
					item.rightBottomNeighbor = st.right;
					item.rightTopNeighbor = st.right;
				} else {
					item.rightBottomNeighbor = null;
					item.rightTopNeighbor = null;
				}
			} else if (item != item0) {
				// Middle.
				item0.rightTopNeighbor = item;
				if (item0.rightBottomNeighbor == item0.rightTopNeighbor) {
					item0.rightBottomNeighbor = item;
				}
				item.leftBottomNeighbor = item0;
				item.leftTopNeighbor = item0;
			}
			item0 = item;
		}
		// Create top trapezoids.
		st.top = new Trapezoid[l.length];
		p2 = p;
		for (int i = 0, j = 0; i < l.length; ++i) {
			Trapezoid t = l[i];
			if (i == l.length - 1) {
				// Last trapezoid.
				Trapezoid t2 = new Trapezoid(p2, q, t.top, s);
				for (; j <= i; ++j) {
					st.top[j] = t2;
				}
				trapezoids.add(t2);
			} else if (s.auDessus(t.right)) {
				// If the right point is above the segment.
				Trapezoid t2 = new Trapezoid(p2, t.right, t.top, s);
				// If it isn't the first trapezoid.
				for (; j <= i; ++j) {
					st.top[j] = t2;
				}
				trapezoids.add(t2);
				p2 = t.right;
			}
		}
		// Link top trapezoids.
		item0 = null;
		for (int i = 0; i < st.top.length; ++i) {
			Trapezoid item = st.top[i];
			if (i == 0) {
				// First trapezoid.
				if (st.left != null) {
					st.left.rightTopNeighbor = item;
					item.leftBottomNeighbor = st.left;
					item.leftTopNeighbor = st.left;
				} else {
					item.leftBottomNeighbor = null;
					item.leftTopNeighbor = null;
				}
			} else if (i == st.top.length - 1) {
				// Last trapezoid.
				if (st.right != null) {
					st.right.leftTopNeighbor = item;
					item.rightBottomNeighbor = st.right;
					item.rightTopNeighbor = st.right;
				} else {
					item.rightBottomNeighbor = null;
					item.rightTopNeighbor = null;
				}
			} else if (item != item0) {
				// Middle.
				if (item0.rightTopNeighbor == item0.rightBottomNeighbor) {
					item0.rightTopNeighbor = item;
				}
				item0.rightBottomNeighbor = item;
				item.leftBottomNeighbor = item0;
				item.leftTopNeighbor = item0;
			}
			item0 = item;
		}
		return st;
	}
}
