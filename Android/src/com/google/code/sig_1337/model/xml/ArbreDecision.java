package com.google.code.sig_1337.model.xml;

import java.util.List;

public class ArbreDecision {

	/**
	 * Root node.
	 */
	public INode root;

	/**
	 * Initializing constructor.
	 * 
	 * @param boundingBox
	 *            list of bounding box.
	 * @param root
	 *            root node.
	 */
	public ArbreDecision(INode root) {
		super();
		this.root = root;
	}

	/**
	 * Locate the bounding box at the given point.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @return corresponding bounding box.
	 */
	public BoundingBox locate(double x, double y) {
		if (root == null) {
			return null;
		} else {
			return root.locate(x, y);
		}
	}

	/**
	 * Interface for nodes.
	 */
	public static interface INode {

		/**
		 * Locate the bounding box at the given point.
		 * 
		 * @param x
		 *            x-coordinate.
		 * @param y
		 *            y-coordinate.
		 * @return corresponding bounding box.
		 */
		public BoundingBox locate(double x, double y);

	}

	/**
	 * Null node.
	 */
	public static class NullNode implements INode {

		/**
		 * Null node.
		 */
		public static INode NULL = new NullNode();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public BoundingBox locate(double x, double y) {
			return null;
		}

	}

	/**
	 * Node.
	 */
	public static class Node implements INode {

		/**
		 * Top-left node.
		 */
		private final INode topLeft;

		/**
		 * Top-right node.
		 */
		private final INode topRight;

		/**
		 * Bottom-left node.
		 */
		private final INode bottomLeft;

		/**
		 * Bottom-right node.
		 */
		private final INode bottomRight;

		/**
		 * Center x-coordinate.
		 */
		private final double cX;

		/**
		 * Center y-coordinate.
		 */
		private final double cY;

		/**
		 * Initializing constructor.
		 * 
		 * @param topLeft
		 *            top-left node.
		 * @param topRight
		 *            top-right node.
		 * @param bottomLeft
		 *            bottom-left node.
		 * @param bottomRight
		 *            bottom-right node.
		 * @param cX
		 *            center x-coordinate.
		 * @param cY
		 *            center y-coordinate.
		 */
		public Node(INode topLeft, INode topRight, INode bottomLeft,
				INode bottomRight, double cX, double cY) {
			super();
			this.topLeft = topLeft;
			this.topRight = topRight;
			this.bottomLeft = bottomLeft;
			this.bottomRight = bottomRight;
			this.cX = cX;
			this.cY = cY;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public BoundingBox locate(double x, double y) {
			if (x < cX) {
				if (y < cY) {
					return topLeft.locate(x, y);
				} else {
					return bottomLeft.locate(x, y);
				}
			} else if (y < cY) {
				return topRight.locate(x, y);
			} else {
				return bottomRight.locate(x, y);
			}
		}

	}

	/**
	 * Leaf node.
	 */
	public static class Leaf implements INode {

		/**
		 * List of bounding box.
		 */
		private final List<BoundingBox> boundingBox;

		/**
		 * Default constructor.
		 */
		public Leaf(List<BoundingBox> boundingBox) {
			super();
			this.boundingBox = boundingBox;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public BoundingBox locate(double x, double y) {
			for (BoundingBox bb : boundingBox) {
				if (x >= bb.x1 && y >= bb.y1 && x <= bb.x2 && y <= bb.y2) {
					return bb;
				}
			}
			return null;
		}

	}

	/**
	 * Bounding box of a structure.
	 */
	public static class BoundingBox {

		/**
		 * Structure id.
		 */
		private final long id;

		/**
		 * Top-left x-coordinate.
		 */
		private final double x1;

		/**
		 * Top-left y-coordinate.
		 */
		private final double y1;

		/**
		 * Bottom-right x-coordinate.
		 */
		private final double x2;

		/**
		 * Bottom-right y-coordinate.
		 */
		private final double y2;

		/**
		 * Initializing constructor.
		 * 
		 * @param id
		 *            structure id.
		 * @param name
		 *            structure name.
		 * @param x1
		 *            top-left x-coordinate.
		 * @param y1
		 *            top-left y-coordinate.
		 * @param x2
		 *            bottom-right x-coordinate.
		 * @param y2
		 *            bottom-right y-coordinate.
		 */
		public BoundingBox(long id, double x1, double y1, double x2, double y2) {
			super();
			this.id = id;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		/**
		 * Get the structure id.
		 * 
		 * @return the structure id.
		 */
		public long getId() {
			return id;
		}

	}

}
