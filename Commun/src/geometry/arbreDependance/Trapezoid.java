package geometry.arbreDependance;

import geometry.arbreDependance.SearchGraph.NodeHolder;
import geometry.model.OrderedSegment;
import geometry.model.Point;
import data.model.structure.Structure;

public class Trapezoid {

	public Structure structure;
	public boolean colored = false;
	public NodeHolder leaf;
	public Trapezoid leftTopNeighbor;
	public Trapezoid leftBottomNeighbor;
	public Trapezoid rightTopNeighbor;
	public Trapezoid rightBottomNeighbor;
	public Point left;
	public Point right;
	public OrderedSegment top;
	public OrderedSegment bottom;

	public Trapezoid() {
		this(null, null, null, null);
	}

	public Trapezoid(Point left, Point right, OrderedSegment top,
			OrderedSegment bottom) {
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
	public SplitTrapezoid split(OrderedSegment s) {
		SplitTrapezoid st = new SplitTrapezoid(this, s);
		// Left point and right point.
		Point p = s.debut;
		Point q = s.fin;
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
			setRightNeighbor(leftTopNeighbor, this, st.a);
			setRightNeighbor(leftBottomNeighbor, this, st.a);
			// C.
			st.c.leftTopNeighbor = st.a;
			st.c.leftBottomNeighbor = st.a;
			// D.
			st.d.leftTopNeighbor = st.a;
			st.d.leftBottomNeighbor = st.a;
		} else {
			// P equals the left point, A doesn't exist.
			if (top != null && p.equals(top.debut)) {
				// There is a top segment and P equals its left point.
				// C has no left neighbors.
				// P...Top
				// x---------x
				// .\...C
				// ..x
				if (bottom != null && p.equals(bottom.debut)) {
					// There is a bottom segment and P equals its left point.
					// D has no left neighbors.
					// ....x
					// .../...C
					// P x----------x
					// ...\...D
					// ....x
				} else {
					// D has left neighbors.
					// ....x
					// .../...C
					// P x----------x
					// .......D
					st.d.leftTopNeighbor = leftTopNeighbor;
					st.d.leftBottomNeighbor = leftTopNeighbor;
					setRightNeighbor(leftTopNeighbor, this, st.d);
					setRightNeighbor(leftBottomNeighbor, this, st.d);
				}
			} else if (bottom != null && p.equals(bottom.debut)) {
				// There is a bottom segment and P equals its left point.
				// D has no left neighbors.
				// .......C
				// P x----------x
				// ...\...D
				// ....x
				st.c.leftTopNeighbor = leftTopNeighbor;
				st.c.leftBottomNeighbor = leftTopNeighbor;
				setRightNeighbor(leftTopNeighbor, this, st.c);
				setRightNeighbor(leftBottomNeighbor, this, st.c);
			} else {
				// P is not the left point of the top or bottom segments,
				// but is the right point of the segment at the left.
				// ..........C
				// x----x----------x
				// ....P.....D
				st.c.leftTopNeighbor = leftTopNeighbor;
				st.c.leftBottomNeighbor = leftTopNeighbor;
				setRightNeighbor(leftTopNeighbor, this, st.c);
				st.d.leftTopNeighbor = leftBottomNeighbor;
				st.d.leftBottomNeighbor = leftBottomNeighbor;
				setRightNeighbor(leftBottomNeighbor, this, st.d);
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
			setLeftNeighbor(rightTopNeighbor, this, st.b);
			setLeftNeighbor(rightBottomNeighbor, this, st.b);
			// C.
			st.c.rightTopNeighbor = st.b;
			st.c.rightBottomNeighbor = st.b;
			// D.
			st.d.rightTopNeighbor = st.b;
			st.d.rightBottomNeighbor = st.b;
		} else {
			// Q equals the right point, B doesn't exist.
			if (top != null && q.equals(top.fin)) {
				// There is a top segment and Q equals its right point.
				// C has no right neighbors.
				if (bottom != null && q.equals(bottom.fin)) {
					// There is a bottom segment and Q equals its right point.
					// D has no right neighbors.
				} else {
					// D has right neighbors.
					st.d.rightTopNeighbor = rightTopNeighbor;
					st.d.rightBottomNeighbor = rightTopNeighbor;
					setLeftNeighbor(rightTopNeighbor, this, st.d);
					setLeftNeighbor(rightBottomNeighbor, this, st.d);
				}
			} else if (bottom != null && q.equals(bottom.fin)) {
				// There is a bottom segment and Q equals its right point.
				// D has no right neighbors.
				st.c.rightTopNeighbor = rightTopNeighbor;
				st.c.rightBottomNeighbor = rightTopNeighbor;
				setLeftNeighbor(rightTopNeighbor, this, st.c);
				setLeftNeighbor(rightBottomNeighbor, this, st.c);
			} else {
				// Q is not the right point of the top or bottom segments,
				// but is the left point of the segment at the right.
				st.c.rightTopNeighbor = rightTopNeighbor;
				st.c.rightBottomNeighbor = rightTopNeighbor;
				setLeftNeighbor(rightTopNeighbor, this, st.c);
				st.d.rightTopNeighbor = rightBottomNeighbor;
				st.d.rightBottomNeighbor = rightBottomNeighbor;
				setLeftNeighbor(rightBottomNeighbor, this, st.d);
			}
		}
		return st;
	}

	public static void setRightNeighbor(Trapezoid trapezoid, Trapezoid old,
			Trapezoid neighbor) {
		if (trapezoid != null) {
			if (trapezoid.rightTopNeighbor == old)
				trapezoid.rightTopNeighbor = neighbor;
			if (trapezoid.rightBottomNeighbor == old)
				trapezoid.rightBottomNeighbor = neighbor;
		}
	}

	public static void setLeftNeighbor(Trapezoid trapezoid, Trapezoid old,
			Trapezoid neighbor) {
		if (trapezoid != null) {
			if (trapezoid.leftTopNeighbor == old)
				trapezoid.leftTopNeighbor = neighbor;
			if (trapezoid.leftBottomNeighbor == old)
				trapezoid.leftBottomNeighbor = neighbor;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(toString2());
		sb.append(" [left=");
		sb.append(left);
		sb.append(", right=");
		sb.append(right);
		sb.append(", top=");
		sb.append(top);
		sb.append(", bottom=");
		sb.append(bottom);
		sb.append(", tl=");
		sb.append(leftTopNeighbor != null ? leftTopNeighbor.toString2() : "");
		sb.append(", tb=");
		sb.append(leftBottomNeighbor != null ? leftBottomNeighbor.toString2()
				: "");
		sb.append(", lb=");
		sb.append(rightTopNeighbor != null ? rightTopNeighbor.toString2() : "");
		sb.append(", rb=");
		sb.append(rightBottomNeighbor != null ? rightBottomNeighbor.toString2()
				: "");
		return sb.toString();
	}

	public String toString2() {
		return getClass().getSimpleName() + '@'
				+ Integer.toHexString(hashCode());
	}
}
