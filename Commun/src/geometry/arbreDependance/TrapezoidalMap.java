package geometry.arbreDependance;

import geometry.gui.Panneau;
import geometry.model.OrderedSegment;
import geometry.model.Point;
import geometry.model.Segment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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

	public void toXML(StringBuffer buff, String indent) {
		for (Trapezoid t : trapezoids) {
			t.toXML(buff, indent);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Trapezoid t : trapezoids) {
			sb.append(t.toString());
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Page 138.
	 */
	public SplitTrapezoid split(Trapezoid t, OrderedSegment s) {
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
	public Split2Trapezoid split2(Trapezoid[] l, OrderedSegment s) {
		// Remove initial trapezoids.
		for (int i = 0; i < l.length; ++i) {
			trapezoids.remove(l[i]);
		}
		Trapezoid left = l[0];
		Trapezoid right = l[l.length - 1];
		Split2Trapezoid st = new Split2Trapezoid(l, s);
		// Left point and right point.
		Point p = s.debut;
		Point q = s.fin;
		// Create left and right trapezoids.
		if (!p.equals(left.left)) {
			st.left = new Trapezoid(left.left, p, left.top, left.bottom);
			st.left.leftTopNeighbor = left.leftTopNeighbor;
			st.left.leftBottomNeighbor = left.leftBottomNeighbor;
			Trapezoid.setRightNeighbor(left.leftTopNeighbor, left, st.left);
			Trapezoid.setRightNeighbor(left.leftBottomNeighbor, left, st.left);
			trapezoids.add(st.left);
		}
		if (!p.equals(right.right)) {
			st.right = new Trapezoid(q, right.right, right.top, right.bottom);
			st.right.rightTopNeighbor = right.rightTopNeighbor;
			st.right.rightBottomNeighbor = right.rightBottomNeighbor;
			Trapezoid.setLeftNeighbor(left.rightTopNeighbor, right, st.right);
			Trapezoid
					.setLeftNeighbor(left.rightBottomNeighbor, right, st.right);
			trapezoids.add(st.right);
		}
		// Create bottom trapezoids.
		{
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
		}
		// Create top trapezoids.
		{
			st.top = new Trapezoid[l.length];
			Point p2 = p;
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
		}
		{
			// Link fist trapezoids.
			Trapezoid t = st.top[0];
			Trapezoid b = st.bottom[0];
			Trapezoid old = l[0];
			t.rightTopNeighbor = old.rightTopNeighbor;
			b.rightBottomNeighbor = old.rightBottomNeighbor;
			if (st.left != null) {
				st.left.rightTopNeighbor = t;
				st.left.rightBottomNeighbor = b;
				t.leftBottomNeighbor = st.left;
				t.leftTopNeighbor = st.left;
				b.leftBottomNeighbor = st.left;
				b.leftTopNeighbor = st.left;
			} else if (left.top != null && p.equals(left.top.debut)) {
				if (left.bottom != null && p.equals(left.bottom.debut)) {
					// No left neighbors.
				} else {
					// Bottom left neighbors.
					b.leftTopNeighbor = old.leftTopNeighbor;
					b.leftBottomNeighbor = old.leftBottomNeighbor;
					Trapezoid.setRightNeighbor(old.leftTopNeighbor, old, b);
					Trapezoid.setRightNeighbor(old.leftBottomNeighbor, old, b);
				}
			} else if (left.bottom != null && p.equals(left.bottom.debut)) {
				// Top left neighbors.
				t.leftTopNeighbor = old.leftTopNeighbor;
				t.leftBottomNeighbor = old.leftBottomNeighbor;
				Trapezoid.setRightNeighbor(old.leftTopNeighbor, old, t);
				Trapezoid.setRightNeighbor(old.leftBottomNeighbor, old, t);
			} else {
				// Both left neighbor.
				t.leftTopNeighbor = old.leftTopNeighbor;
				t.leftBottomNeighbor = old.leftTopNeighbor;
				b.leftTopNeighbor = old.leftBottomNeighbor;
				b.leftBottomNeighbor = old.leftBottomNeighbor;
				Trapezoid.setRightNeighbor(old.leftTopNeighbor, old, t);
				Trapezoid.setRightNeighbor(old.leftBottomNeighbor, old, b);
			}
		}
		{
			// Link last trapezoids.
			Trapezoid t = st.top[st.top.length - 1];
			Trapezoid b = st.bottom[st.bottom.length - 1];
			Trapezoid old = l[l.length - 1];
			t.leftTopNeighbor = old.leftTopNeighbor;
			b.leftBottomNeighbor = old.leftBottomNeighbor;
			if (st.right != null) {
				st.right.leftTopNeighbor = t;
				st.right.leftBottomNeighbor = b;
				t.rightBottomNeighbor = st.right;
				t.rightTopNeighbor = st.right;
				b.rightBottomNeighbor = st.right;
				b.rightTopNeighbor = st.right;
			} else if (right.top != null && q.equals(right.top.fin)) {
				if (right.bottom != null && q.equals(right.bottom.fin)) {
					// No right neighbors.
				} else {
					// Bottom right neighbors.
					b.rightTopNeighbor = old.rightTopNeighbor;
					b.rightBottomNeighbor = old.rightBottomNeighbor;
					Trapezoid.setLeftNeighbor(old.rightTopNeighbor, old, b);
					Trapezoid.setLeftNeighbor(old.rightBottomNeighbor, old, b);
				}
			} else if (right.bottom != null && q.equals(right.bottom.fin)) {
				// Top right neighbors.
				t.rightTopNeighbor = old.rightTopNeighbor;
				t.rightBottomNeighbor = old.rightBottomNeighbor;
				Trapezoid.setLeftNeighbor(old.rightTopNeighbor, old, t);
				Trapezoid.setLeftNeighbor(old.rightBottomNeighbor, old, t);
			} else {
				// Both right neighbor.
				t.rightTopNeighbor = old.rightTopNeighbor;
				t.rightBottomNeighbor = old.rightTopNeighbor;
				b.rightTopNeighbor = old.rightBottomNeighbor;
				b.rightBottomNeighbor = old.rightBottomNeighbor;
				Trapezoid.setLeftNeighbor(old.rightTopNeighbor, old, t);
				Trapezoid.setLeftNeighbor(old.rightBottomNeighbor, old, b);
			}
		}
		{
			// Link middle trapezoids.
			Trapezoid t0 = st.top[0];
			Trapezoid b0 = st.bottom[0];
			for (int i = 1; i < st.bottom.length; ++i) {
				Trapezoid t = st.top[i];
				Trapezoid b = st.bottom[i];
				// Top.
				if (t != t0) {
					t0.rightBottomNeighbor = t;
					t.leftBottomNeighbor = t0;
				}
				// Bottom.
				if (b != b0) {
					b0.rightTopNeighbor = b;
					b.leftTopNeighbor = b0;
				}
				t0 = t;
				b0 = b;
			}
		}
		return st;
	}

	public void out(Rectangle2D.Double bounds, BufferedImage img, Graphics2D g) {
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		Panneau.INDEX = 0;
		for (Trapezoid t : trapezoids) {
			out(bounds, img, g, t);
			++Panneau.INDEX;
			Panneau.INDEX %= 256;
		}
		g.setComposite(c);
		for (Trapezoid t : trapezoids) {
			if (t.bottom != null) {
				out(bounds, img, g, t.bottom);
			}
			if (t.top != null) {
				out(bounds, img, g, t.top);
			}
		}
	}

	public void out(Rectangle2D.Double bounds, BufferedImage img, Graphics2D g,
			Trapezoid trapezoid) {
		double w = img.getWidth() / bounds.getWidth();
		double h = img.getHeight() / bounds.getHeight();

		Point left = trapezoid.left;
		Point right = trapezoid.right;
		Segment top = trapezoid.top;
		Segment bottom = trapezoid.bottom;
		if(top == null || bottom == null) return;
		g.setColor(new Color((int) (Math.random() * 128 + 64), (int) (Math
				.random() * 128 + 64), (int) (Math.random() * 128 + 64)));
		int[] x = new int[4];
		int[] y = new int[4];
		// Left.
		x[0] = 0;
		if (left != null) {
			x[0] = (int) ((left.x - bounds.x) * w);
		}
		x[1] = x[0];
		y[0] = 0;
		if (top != null && left != null) {
			y[0] = (int) ((top.getY(left.x) - bounds.y) * h);
		}
		y[1] = img.getHeight();
		if (bottom != null && left != null) {
			y[1] = (int) ((bottom.getY(left.x) - bounds.y) * h);
		}
		// Right.
		x[2] = img.getWidth();
		if (right != null) {
			x[2] = (int) ((right.x - bounds.x) * w);
		}
		x[3] = x[2];
		y[2] = img.getHeight();
		if (bottom != null && right != null) {
			y[2] = (int) ((bottom.getY(right.x) - bounds.y) * h);
		}
		y[3] = 0;
		if (top != null && right != null) {
			y[3] = (int) ((top.getY(right.x) - bounds.y) * h);
		}
		g.fillPolygon(x, y, 4);
		g.setColor(Color.black);
	}

	public void out(Rectangle2D.Double bounds, BufferedImage img, Graphics2D g,
			OrderedSegment segment) {
		double w = img.getWidth() / bounds.getWidth();
		double h = img.getHeight() / bounds.getHeight();
		g.setColor(Color.black);
		g.drawLine((int) ((segment.debut.x - bounds.x) * w),
				(int) ((segment.debut.y - bounds.y) * h),
				(int) ((segment.fin.x - bounds.x) * w),
				(int) ((segment.fin.y - bounds.y) * h));
	}
}
