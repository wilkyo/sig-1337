package geometry.arbreDependance;

import geometry.model.Point;
import geometry.model.Segment;

import java.awt.geom.Rectangle2D;

public class SearchGraph {

	private TrapezoidalMap map;
	private Node root;

	public SearchGraph(Rectangle2D.Double bounds) {
		map = new TrapezoidalMap(bounds);
		root = new NodeHolder(new Leaf(map.getRoot()));
	}

	public TrapezoidalMap getMap() {
		return map;
	}

	public Trapezoid locate(Point point) {
		return root.locate(point);
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

	}

	public static class NodeHolder extends Node {

		private Node inner;

		public NodeHolder(Node inner) {
			super(null, null);
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
			Node leafC = new Leaf(split.c);
			Node leafD = new Leaf(split.d);
			Node root = new YNode(new NodeHolder(leafC), new NodeHolder(leafD),
					split.c.bottom);
			// B.
			if (split.b != null) {
				Node leafB = new Leaf(split.b);
				root = new XNode(new NodeHolder(root), new NodeHolder(leafB),
						split.b.left);
			}
			// A.
			if (split.a != null) {
				Node leafA = new Leaf(split.a);
				root = new XNode(new NodeHolder(leafA), new NodeHolder(root),
						split.a.right);
			}
			// Replace the leaf with the new node.
			parent.inner = root;
		}
	}

}
