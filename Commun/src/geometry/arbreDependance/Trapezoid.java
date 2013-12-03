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

	public void toXML(StringBuffer buff, String indent) {
		buff.append(indent);
		buff.append("<trapezoid left=\"");
		if (left != null) {
			buff.append(left.x);
			buff.append(", ");
			buff.append(left.y);
		}
		buff.append("\" right=\"");
		if (right != null) {
			buff.append(right.x);
			buff.append(", ");
			buff.append(right.y);
		}
		buff.append("\" top=\"");
		if (top != null) {
			buff.append(top.debut.x);
			buff.append(", ");
			buff.append(top.debut.y);
			buff.append(", ");
			buff.append(top.fin.x);
			buff.append(", ");
			buff.append(top.fin.y);
		}
		buff.append("\" bottom=\"");
		if (bottom != null) {
			buff.append(bottom.debut.x);
			buff.append(", ");
			buff.append(bottom.debut.y);
			buff.append(", ");
			buff.append(bottom.fin.x);
			buff.append(", ");
			buff.append(bottom.fin.y);
		}
		buff.append("\" />\n");
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
				if (leftTopNeighbor.rightTopNeighbor == leftTopNeighbor.rightBottomNeighbor) {
					leftTopNeighbor.rightTopNeighbor = st.a;
				}
				leftTopNeighbor.rightBottomNeighbor = st.a;
			}
			if (leftBottomNeighbor != null) {
				if (leftBottomNeighbor.rightBottomNeighbor == leftBottomNeighbor.rightTopNeighbor) {
					leftBottomNeighbor.rightBottomNeighbor = st.a;
				}
				leftBottomNeighbor.rightTopNeighbor = st.a;
			}
			// C.
			st.c.leftTopNeighbor = st.a;
			st.c.leftBottomNeighbor = st.a;
			// D.
			st.d.leftTopNeighbor = st.a;
			st.d.leftBottomNeighbor = st.a;
		} else {
			// P equals the left point:
			// - P is the left point of the bottom segment => D has no left
			// neighbor.
			// - P is the left point of the top segment => C has no left
			// neighbor.
			Trapezoid t = null;
			if (bottom != null && p.equals(bottom.debut)) {
				// C: there is a bottom segment and p equals its left point.
				st.c.leftTopNeighbor = leftTopNeighbor;
				st.c.leftBottomNeighbor = leftTopNeighbor;
				t = st.c;
			} else {
				// D: there is a top segment and p equals its left point.
				st.d.leftTopNeighbor = leftBottomNeighbor;
				st.d.leftBottomNeighbor = leftBottomNeighbor;
				t = st.d;
			}
			// Modify the right neighbor of the top left neighbor.
			if (leftTopNeighbor != null) {
				if (leftTopNeighbor.rightTopNeighbor == leftTopNeighbor.rightBottomNeighbor) {
					// Top right neighbor was the same as the bottom right one.
					leftTopNeighbor.rightTopNeighbor = t;
				}
				leftTopNeighbor.rightBottomNeighbor = t;
			}
			// Modify the right neighbor of the bottom left neighbor.
			if (leftBottomNeighbor != null) {
				if (leftBottomNeighbor.rightBottomNeighbor == leftBottomNeighbor.rightTopNeighbor) {
					// Bottom right neighbor was the same as the top right one.
					leftBottomNeighbor.rightBottomNeighbor = t;
				}
				leftBottomNeighbor.rightTopNeighbor = t;
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
				if (rightTopNeighbor.leftTopNeighbor == rightTopNeighbor.leftBottomNeighbor) {
					rightTopNeighbor.leftTopNeighbor = st.b;
				}
				rightTopNeighbor.leftBottomNeighbor = st.b;
			}
			if (rightBottomNeighbor != null) {
				if (rightBottomNeighbor.leftBottomNeighbor == rightBottomNeighbor.leftTopNeighbor) {
					rightBottomNeighbor.leftBottomNeighbor = st.b;
				}
				rightBottomNeighbor.leftTopNeighbor = st.b;
			}
			// C.
			st.c.rightTopNeighbor = st.b;
			st.c.rightBottomNeighbor = st.b;
			// D.
			st.d.rightTopNeighbor = st.b;
			st.d.rightBottomNeighbor = st.b;
		} else {
			// Q equals the right point:
			// - Q is the right point of the bottom segment => D has no right
			// neighbor.
			// - Q is the right point of the top segment => C has no right
			// neighbor.
			Trapezoid t = null;
			if (bottom != null && q.equals(bottom.fin)) {
				// C: there is a bottom segment and q equals its right point.
				st.c.rightTopNeighbor = rightTopNeighbor;
				st.c.rightBottomNeighbor = rightTopNeighbor;
				t = st.c;
			} else {
				// D: there is a top segment and q equals its right point.
				st.d.rightTopNeighbor = rightBottomNeighbor;
				st.d.rightBottomNeighbor = rightBottomNeighbor;
				t = st.d;
			}
			// Modify the left neighbor of the top right neighbor.
			if (rightTopNeighbor != null) {
				if (rightTopNeighbor.leftTopNeighbor == rightTopNeighbor.leftBottomNeighbor) {
					// Top left neighbor was the same as the bottom left one.
					rightTopNeighbor.leftTopNeighbor = t;
				}
				rightTopNeighbor.leftBottomNeighbor = t;
			}
			// Modify the left neighbor of the bottom right neighbor.
			if (rightBottomNeighbor != null) {
				if (rightBottomNeighbor.leftBottomNeighbor == rightBottomNeighbor.leftTopNeighbor) {
					// Bottom left neighbor was the same as the top left one.
					rightBottomNeighbor.leftBottomNeighbor = t;
				}
				rightBottomNeighbor.leftTopNeighbor = t;
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
