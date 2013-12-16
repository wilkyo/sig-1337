package geometry.arbreDependance2;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import data.model.structure.Building;

public class ArbreDecision {

	private static final int DEPTH = 4;

	public static ArbreDecision create(Collection<Building> buildings) {
		return create(buildings, DEPTH);
	}

	public static ArbreDecision create(Collection<Building> buildings, int depth) {
		// Create the bounding box.
		List<BoundingBox> l = new ArrayList<BoundingBox>();
		long id = 1;
		double minX = 0, maxX = 0, minY = 0, maxY = 0;
		boolean first = true;
		for (Building b : buildings) {
			String name = b.getName();
			// If the building has a name.
			if (name != null && !name.isEmpty()) {
				BoundingBox bb = new BoundingBox(id, b);
				boolean firstN = true;
				for (data.model.Node n : b.getNodes()) {
					if (firstN) {
						bb.x = n.getLongitude();
						bb.y = n.getLatitude();
						bb.width = n.getLongitude();
						bb.height = n.getLatitude();
						firstN = false;
					} else {
						bb.x = Math.min(bb.x, n.getLongitude());
						bb.y = Math.min(bb.y, n.getLatitude());
						bb.width = Math.max(bb.width, n.getLongitude());
						bb.height = Math.max(bb.height, n.getLatitude());
					}
				}
				l.add(bb);
				if (first) {
					minX = bb.x;
					minY = bb.y;
					maxX = bb.width;
					maxY = bb.height;
					first = false;
				} else {
					minX = Math.min(minX, bb.x);
					minY = Math.min(minY, bb.y);
					maxX = Math.max(maxX, bb.width);
					maxY = Math.max(maxY, bb.height);
				}
				++id;
			}
		}
		// Split.
		INode node = split(minX, minY, maxX, maxY, depth, l);
		return new ArbreDecision(l, node);
	}

	private static INode split(double minX, double minY, double maxX,
			double maxY, int depth, List<BoundingBox> list) {
		List<BoundingBox> l = new ArrayList<BoundingBox>();
		for (BoundingBox bb : list) {
			// If the building is inside of the bounds.
			if (bb.x < maxX && bb.y < maxY && bb.width > minX
					&& bb.height > minY) {
				l.add(bb);
			}
		}
		if (l.isEmpty()) {
			// Empty node.
			return NullNode.NULL;
		} else if (depth == 0) {
			// Leaf.
			return new Leaf(l);
		} else {
			// Node.
			double cX = (maxX - minX) / 2 + minX;
			double cY = (maxY - minY) / 2 + minY;
			depth--;
			INode topLeft = split(minX, minY, cX, cY, depth, l);
			INode topRight = split(cX, minY, maxX, cY, depth, l);
			INode bottomLeft = split(minX, cY, cX, maxY, depth, l);
			INode bottomRight = split(cX, cY, maxX, maxY, depth, l);
			if (topLeft == NullNode.NULL && topRight == NullNode.NULL
					&& bottomLeft == NullNode.NULL
					&& bottomRight == NullNode.NULL) {
				// Empty node.
				return NullNode.NULL;
			} else {
				// Node.
				return new Node(topLeft, topRight, bottomLeft, bottomRight, cX,
						cY);
			}
		}
	}

	/**
	 * List of bounding box.
	 */
	private final List<BoundingBox> boundingBox;

	/**
	 * Root node.
	 */
	private final INode root;

	/**
	 * Initializing constructor.
	 * 
	 * @param boundingBox
	 *            list of bounding box.
	 * @param root
	 *            root node.
	 */
	public ArbreDecision(List<BoundingBox> boundingBox, INode root) {
		super();
		this.boundingBox = boundingBox;
		this.root = root;
	}

	/**
	 * Output the node to the given string buffer.
	 * 
	 * @param buff
	 *            buffer to use.
	 * @param indent
	 *            indentation.
	 */
	public void toXML(StringBuffer buff, String indent) {
		String s = indent + '\t';
		buff.append(indent);
		buff.append("<boundingBoxes>\n");
		for (BoundingBox bb : boundingBox) {
			bb.toXML(buff, s);
		}
		buff.append(indent);
		buff.append("</boundingBoxes>\n");
		buff.append(indent);
		buff.append("<nodes>\n");
		root.toXML(buff, s);
		buff.append(indent);
		buff.append("</nodes>\n");
	}

	/**
	 * Interface for nodes.
	 */
	public static interface INode {

		/**
		 * Output the node to the given string buffer.
		 * 
		 * @param buff
		 *            buffer to use.
		 * @param indent
		 *            indentation.
		 */
		public void toXML(StringBuffer buff, String indent);

	}

	/**
	 * Null node.
	 */
	private static class NullNode implements INode {

		/**
		 * Null node.
		 */
		private static INode NULL = new NullNode();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<null/>\n");
		}

	}

	/**
	 * Node.
	 */
	private static class Node implements INode {

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
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<node x=\"");
			buff.append(cX);
			buff.append("\" y=\"");
			buff.append(cY);
			buff.append("\">\n");
			String s = indent + '\t';
			topLeft.toXML(buff, s);
			topRight.toXML(buff, s);
			bottomLeft.toXML(buff, s);
			bottomRight.toXML(buff, s);
			buff.append(indent);
			buff.append("</node>\n");
		}

	}

	/**
	 * Leaf node.
	 */
	private static class Leaf implements INode {

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
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<leaf>\n");
			String s = indent + '\t';
			for (BoundingBox bb : boundingBox) {
				buff.append(s);
				buff.append("<boundingBox id=\"");
				buff.append(bb.id);
				buff.append("\"/>\n");
			}
			buff.append(indent);
			buff.append("</leaf>\n");
		}

	}

	/**
	 * Bounding box of a building.
	 */
	private static class BoundingBox extends Rectangle2D.Double {

		/**
		 * Default serial version UID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Identifier.
		 */
		private final long id;

		/**
		 * Building.
		 */
		private final Building building;

		/**
		 * Initializing constructor.
		 * 
		 * @param id
		 *            identifier.
		 * @param building
		 *            building.
		 */
		public BoundingBox(long id, Building building) {
			super();
			this.id = id;
			this.building = building;
		}

		/**
		 * Output the node to the given string buffer.
		 * 
		 * @param buff
		 *            buffer to use.
		 * @param indent
		 *            indentation.
		 */
		public void toXML(StringBuffer buff, String indent) {
			buff.append(indent);
			buff.append("<boundingBox id=\"");
			buff.append(id);
			buff.append("\" name=\"");
			buff.append(building.getName());
			buff.append("\" x1=\"");
			buff.append(x);
			buff.append("\" y1=\"");
			buff.append(y);
			buff.append("\" x2=\"");
			buff.append(width);
			buff.append("\" y2=\"");
			buff.append(height);
			buff.append("\"/>\n");
		}
	}

}
