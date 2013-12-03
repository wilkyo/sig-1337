package geometry.arbreDependance;

import geometry.model.Point;
import geometry.model.Segment;

import java.awt.geom.Rectangle2D;

public class SearchGraph {

	private TrapezoidalMap map;
	private NodeHolder root;

	public SearchGraph(Rectangle2D.Double bounds) {
		map = new TrapezoidalMap(bounds);
		root = new NodeHolder(new Leaf(map.getRoot()));
		map.getRoot().leaf = root;
	}

	public TrapezoidalMap getMap() {
		return map;
	}

	public Trapezoid locate(Point point) {
		return root.locate(point);
	}

	public void split2(Split2Trapezoid split) {
		// Create the new leaf nodes.
		NodeHolder leafLeft = null;
		if (split.left != null) {
			leafLeft = new NodeHolder(new Leaf(split.left));
			split.left.leaf = leafLeft;
		}
		NodeHolder leafRight = null;
		if (split.right != null) {
			leafRight = new NodeHolder(new Leaf(split.right));
			split.right.leaf = leafRight;
		}
		NodeHolder[] leafTop = new NodeHolder[split.top.length];
		for (int i = 0; i < split.top.length; ++i) {
			Trapezoid t = split.top[i];
			NodeHolder n = new NodeHolder(new Leaf(t));
			t.leaf = n;
			leafTop[i] = n;
		}
		NodeHolder[] leafBottom = new NodeHolder[split.bottom.length];
		for (int i = 0; i < split.bottom.length; ++i) {
			Trapezoid t = split.bottom[i];
			NodeHolder n = new NodeHolder(new Leaf(t));
			t.leaf = n;
			leafBottom[i] = n;
		}
		// Segment.
		Point p = new Point(split.segment.debut);
		Point q = new Point(split.segment.fin);
		if (q.x < p.x) {
			Point tmp = q;
			q = p;
			p = tmp;
		}
		// Left trapezoid.
		Trapezoid t = split.initial[0];
		t.leaf.inner = new YNode(leafTop[0], leafBottom[0], split.segment);
		if (split.left != null) {
			t.leaf.inner = new XNode(leafLeft, new NodeHolder(t.leaf.inner), p);
		}
		// Right trapezoid.
		t = split.initial[split.initial.length - 1];
		t.leaf.inner = new YNode(leafTop[leafTop.length - 1],
				leafBottom[leafBottom.length - 1], split.segment);
		if (split.right != null) {
			t.leaf.inner = new XNode(new NodeHolder(t.leaf.inner), leafRight, q);
		}
		// Intermediate trapezoids.
		for (int i = 1; i < split.initial.length - 1; ++i) {
			t = split.initial[i];
			t.leaf.inner = new YNode(leafTop[i], leafBottom[i], split.segment);
		}
	}

	public void toXML(StringBuffer buff, String indent) {
		root.toXML(buff, indent);
	}

	public static abstract class Node {

		protected NodeHolder left;
		protected NodeHolder right;

		public Node(NodeHolder left, NodeHolder right) {
			super();
			this.left = left;
			this.right = right;
		}

		public Node getLeft() {
			return left;
		}

		public Node getRight() {
			return right;
		}

		public abstract Trapezoid locate(Point point);

		public void split(SplitTrapezoid split) {
		}

		protected void split(SplitTrapezoid split, NodeHolder parent) {
		}

		public abstract void toXML(StringBuffer buff, String indent);

	}

	public static class NodeHolder extends Node {

		private Node inner;

		public NodeHolder(Node inner) {
			super(null, null);
			if (inner == null)
				throw new NullPointerException();
			this.inner = inner;
		}

		public Node getLeft() {
			return inner.getLeft();
		}

		public Node getRight() {
			return inner.getRight();
		}

		public Trapezoid locate(Point point) {
			return inner.locate(point);
		}

		public void split(SplitTrapezoid split) {
			inner.split(split, this);
		}

		@Override
		public void toXML(StringBuffer buff, String indent) {
			inner.toXML(buff, indent);
		}

	}

	public static class XNode extends Node {

		private Point point;

		public XNode(NodeHolder left, NodeHolder right, Point point) {
			super(left, right);
			this.point = new Point(point);
		}

		public Point getPoint() {
			return point;
		}

		public Trapezoid locate(Point point) {
			if (point.x < this.point.x) {
				return left.locate(point);
			} else {
				return right.locate(point);
			}
		}

		@Override
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<xnode x=\"");
			buff.append(point.x);
			buff.append("\" y=\"");
			buff.append(point.y);
			buff.append("\">\n");
			left.toXML(buff, indent + '\t');
			right.toXML(buff, indent + '\t');
			buff.append(indent);
			buff.append("</xnode>\n");
		}

	}

	public static class YNode extends Node {

		private Segment segment;

		public YNode(NodeHolder left, NodeHolder right, Segment segment) {
			super(left, right);
			this.segment = segment;
		}

		public Segment getSegment() {
			return segment;
		}

		public Trapezoid locate(Point point) {
			if (segment.auDessus(point)) {
				return left.locate(point);
			} else {
				return right.locate(point);
			}
		}

		@Override
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<ynode x1=\"");
			buff.append(segment.debut.x);
			buff.append("\" y1=\"");
			buff.append(segment.debut.y);
			buff.append("\" x2=\"");
			buff.append(segment.fin.x);
			buff.append("\" y2=\"");
			buff.append(segment.fin.y);
			buff.append("\">\n");
			left.toXML(buff, indent + '\t');
			right.toXML(buff, indent + '\t');
			buff.append(indent);
			buff.append("</ynode>\n");
		}

	}

	public static class Leaf extends Node {

		private Trapezoid trapezoid;

		public Leaf(Trapezoid trapezoid) {
			super(null, null);
			this.trapezoid = trapezoid;
		}

		@Override
		public Trapezoid locate(Point point) {
			return trapezoid;
		}

		public void split(SplitTrapezoid split, NodeHolder parent) {
			// C/D.
			NodeHolder leafC = new NodeHolder(new Leaf(split.c));
			split.c.leaf = leafC;
			NodeHolder leafD = new NodeHolder(new Leaf(split.d));
			split.d.leaf = leafD;
			Node root = new YNode(leafC, leafD, split.c.bottom);
			// B.
			if (split.b != null) {
				NodeHolder leafB = new NodeHolder(new Leaf(split.b));
				split.b.leaf = leafB;
				root = new XNode(new NodeHolder(root), leafB, split.b.left);
			}
			// A.
			if (split.a != null) {
				NodeHolder leafA = new NodeHolder(new Leaf(split.a));
				split.a.leaf = leafA;
				root = new XNode(leafA, new NodeHolder(root), split.a.right);
			}
			// Replace the leaf with the new node.
			parent.inner = root;
		}

		@Override
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<leaf id=\"");
			if (trapezoid.structure != null) {
				buff.append(trapezoid.structure.getId());
			} else {
				buff.append("");
			}
			buff.append("\" name=\"");
			if (trapezoid.structure != null) {
				buff.append(trapezoid.structure.getName());
			} else {
				buff.append("");
			}
			buff.append("\"/>\n");
		}
	}

}
