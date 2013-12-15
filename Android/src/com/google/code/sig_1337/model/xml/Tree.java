package com.google.code.sig_1337.model.xml;

public class Tree {

	/**
	 * Root node.
	 */
	public INode root;

	/**
	 * Initializing constructor.
	 * 
	 * @param root
	 *            root node.
	 */
	public Tree(INode root) {
		super();
		this.root = root;
	}

	/**
	 * Locate the leaf node at the given point.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @return corresponding leaf node.
	 */
	public ILeaf locate(double x, double y) {
		if (root == null) {
			return Leaf.NULL;
		} else {
			return root.locate(x, y);
		}
	}

	/**
	 * Interface for nodes.
	 */
	public static interface INode {

		/**
		 * Locate the leaf node at the given point.
		 * 
		 * @param x
		 *            x-coordinate.
		 * @param y
		 *            y-coordinate.
		 * @return corresponding leaf node.
		 */
		public ILeaf locate(double x, double y);

	}

	/**
	 * Interface for leafs.
	 */
	public static interface ILeaf extends INode {

		/**
		 * Get the id of the structure at the given coordinates.
		 * 
		 * @param x
		 *            x-coordinate.
		 * @param y
		 *            y-coordinate.
		 * @return corresponding id.
		 */
		public long getId();

		/**
		 * Get the name of the structure at the given coordinates.
		 * 
		 * @param x
		 *            x-coordinate.
		 * @param y
		 *            y-coordinate.
		 * @return corresponding name.
		 */
		public String getName();

	}

	public static class Leaf implements ILeaf {

		/**
		 * Null leaf.
		 */
		public static final Leaf NULL = new Leaf(-1, "");

		/**
		 * Structure id.
		 */
		private final int id;

		/**
		 * Structure name.
		 */
		private final String name;

		/**
		 * Initializing constructor.
		 * 
		 * @param id
		 *            structure id.
		 * @param name
		 *            structure name.
		 */
		public Leaf(int id, String name) {
			super();
			this.id = id;
			if (name == null || name.equals("")) {
				this.name = null;
			} else {
				this.name = name;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ILeaf locate(double x, double y) {
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getId() {
			return id;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return name;
		}

	}

	public static class XNode implements INode {

		/**
		 * Point.
		 */
		private final double x;

		/**
		 * Point.
		 */
		private final double y;

		/**
		 * Left child.
		 */
		private final INode left;

		/**
		 * Right child.
		 */
		private final INode right;

		/**
		 * Initializing constructor.
		 * 
		 * @param x
		 *            point.
		 * @param y
		 *            point.
		 * @param left
		 *            left child.
		 * @param right
		 *            right child.
		 */
		public XNode(double x, double y, INode left, INode right) {
			super();
			this.x = x;
			this.y = y;
			this.left = left;
			this.right = right;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ILeaf locate(double x, double y) {
			switch (Point.compare(this.x, this.y, x, y)) {
			case 0:
			case 1:
				return right.locate(x, y);
			default:
				return left.locate(x, y);
			}
		}

	}

	public static class YNode implements INode {

		/**
		 * First point.
		 */
		private final double x1;

		/**
		 * First point.
		 */
		private final double y1;

		/**
		 * Second point.
		 */
		private final double x2;

		/**
		 * Second point.
		 */
		private final double y2;

		/**
		 * Top child.
		 */
		private final INode top;

		/**
		 * Bottom child.
		 */
		private final INode bottom;

		/**
		 * Initializing constructor.
		 * 
		 * @param x1
		 *            first point.
		 * @param y1
		 *            first point.
		 * @param x2
		 *            second point.
		 * @param y2
		 *            second point.
		 * @param top
		 *            top child.
		 * @param bottom
		 *            bottom child.
		 */
		public YNode(double x1, double y1, double x2, double y2, INode top,
				INode bottom) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.top = top;
			this.bottom = bottom;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ILeaf locate(double x, double y) {
			double det = (x2 - x1) * (y - y1) - (y2 - y1) * (x - x1);
			if (det < 0) {
				return top.locate(x, y);
			} else {
				return bottom.locate(x, y);
			}
		}
	}

}
