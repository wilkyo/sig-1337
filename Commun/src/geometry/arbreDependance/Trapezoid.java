package geometry.arbreDependance;

import geometry.arbreDependance.SearchGraph.NodeHolder;
import geometry.model.Point;
import geometry.model.Segment;
import data.model.structure.Structure;

public class Trapezoid {

	public Structure structure;
	public NodeHolder leaf;
	public Trapezoid leftTopNeighbor;
	public Trapezoid leftBottomNeighbor;
	public Trapezoid rightTopNeighbor;
	public Trapezoid rightBottomNeighbor;
	public Point left;
	public Point right;
	public Segment top;
	public Segment bottom;

	public Trapezoid() {
		this(null, null, null, null);
	}

	public Trapezoid(Point left, Point right, Segment top, Segment bottom) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	/**
	 * Page 138.
	 */
	public SplitTrapezoid split(Segment s) {
		SplitTrapezoid st = new SplitTrapezoid(this, s);
		// Left point and right point.
		Point p = new Point(s.debut);
		Point q = new Point(s.fin);
		if (q.x < p.x) {
			Point tmp = q;
			q = p;
			p = tmp;
		}
		// C/D.
		st.c = new Trapezoid(p, q, top, s);
		st.d = new Trapezoid(p, q, s, bottom);
		// A.
		if (!p.equals(left)) {
			// A.
			st.a = new Trapezoid(left, p, top, bottom);
			st.a.leftTopNeighbor = leftTopNeighbor;
			st.a.leftBottomNeighbor = leftBottomNeighbor;
			st.a.rightTopNeighbor = st.c;
			st.a.rightBottomNeighbor = st.d;
			if (leftTopNeighbor != null) {
				leftTopNeighbor.rightTopNeighbor = st.a;
				leftTopNeighbor.rightBottomNeighbor = st.a;
			}
			if (leftBottomNeighbor != null) {
				leftBottomNeighbor.rightTopNeighbor = st.a;
				leftBottomNeighbor.rightBottomNeighbor = st.a;
			}
			// C.
			st.c.leftTopNeighbor = st.a;
			st.c.leftBottomNeighbor = st.a;
			// D.
			st.d.leftTopNeighbor = st.a;
			st.d.leftBottomNeighbor = st.a;
		} else {
			// C.
			st.c.leftTopNeighbor = leftTopNeighbor;
			st.c.leftBottomNeighbor = leftTopNeighbor;
			// D.
			st.d.leftTopNeighbor = leftBottomNeighbor;
			st.d.leftBottomNeighbor = leftBottomNeighbor;
			if (leftTopNeighbor != null) {
				leftTopNeighbor.rightTopNeighbor = st.c;
				leftTopNeighbor.rightBottomNeighbor = st.c;
			}
			if (leftBottomNeighbor != null) {
				leftBottomNeighbor.rightTopNeighbor = st.d;
				leftBottomNeighbor.rightBottomNeighbor = st.d;
			}
		}
		// B.
		if (!q.equals(right)) {
			// B.
			st.b = new Trapezoid(q, right, top, bottom);
			st.b.leftTopNeighbor = st.c;
			st.b.leftBottomNeighbor = st.d;
			st.b.rightTopNeighbor = rightTopNeighbor;
			st.b.rightBottomNeighbor = rightBottomNeighbor;
			if (rightTopNeighbor != null) {
				rightTopNeighbor.leftTopNeighbor = st.b;
				rightTopNeighbor.leftBottomNeighbor = st.b;
			}
			if (rightBottomNeighbor != null) {
				rightBottomNeighbor.leftTopNeighbor = st.b;
				rightBottomNeighbor.leftBottomNeighbor = st.b;
			}
			// C.
			st.c.rightTopNeighbor = st.b;
			st.c.rightBottomNeighbor = st.b;
			// D.
			st.d.rightTopNeighbor = st.b;
			st.d.rightBottomNeighbor = st.b;
		} else {
			// C.
			st.c.rightTopNeighbor = rightTopNeighbor;
			st.c.rightBottomNeighbor = rightTopNeighbor;
			// D.
			st.d.rightTopNeighbor = rightBottomNeighbor;
			st.d.rightBottomNeighbor = rightBottomNeighbor;
			if (rightTopNeighbor != null) {
				rightTopNeighbor.leftTopNeighbor = st.c;
				rightTopNeighbor.leftBottomNeighbor = st.c;
			}
			if (rightBottomNeighbor != null) {
				rightBottomNeighbor.leftTopNeighbor = st.d;
				rightBottomNeighbor.leftBottomNeighbor = st.d;
			}
		}
		return st;
	}

	@Override
	public String toString() {
		return "Trapezoid [left=" + left + ", right=" + right + ", top=" + top
				+ ", bottom=" + bottom + "]";
	}

}
