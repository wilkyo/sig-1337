package com.google.code.sig_1337.model.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.code.sig_1337.model.ILocalSig1337;
import com.google.code.sig_1337.model.xml.ArbreDecision;
import com.google.code.sig_1337.model.xml.ArbreDecision.BoundingBox;
import com.google.code.sig_1337.model.xml.ArbreDecision.INode;
import com.google.code.sig_1337.model.xml.ArbreDecision.Leaf;
import com.google.code.sig_1337.model.xml.ArbreDecision.Node;
import com.google.code.sig_1337.model.xml.ArbreDecision.NullNode;
import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.IVertex;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.Vertex;

/**
 * Handler for the {@code XmlPullParser}.<br/>
 * <br/>
 * 
 * Also handles the graph, tree...
 */
public class LocalHandler<T extends ILocalSig1337> extends RemoteHandler<T> {

	/**
	 * Name for the {@code graph} tag.
	 */
	private static final String GRAPH = "graph";

	/**
	 * Name for the {@code tag} tag.
	 */
	private static final String VERTEX = "vertex";

	/**
	 * Name for the {@code tree} tag.
	 */
	private static final String TREE = "tree";

	/**
	 * Name for the {@code boundingBoxes} tag.
	 */
	private static final String BOUNDING_BOXES = "boundingBoxes";

	/**
	 * Name for the {@code boundingBox} tag.
	 */
	private static final String BOUNDING_BOX = "boundingBox";

	/**
	 * Name for the {@code nodes} tag.
	 */
	private static final String NODES = "nodes";

	/**
	 * Name for the {@code node} tag.
	 */
	private static final String NODE = "node";

	/**
	 * Name for the {@code null} tag.
	 */
	private static final String NULL = "null";

	/**
	 * Name for the {@code leaf} tag.
	 */
	private static final String LEAF = "leaf";

	/**
	 * Name for the {@code x1} attribute.
	 */
	protected static final String X1 = "x1";

	/**
	 * Name for the {@code y1} attribute.
	 */
	protected static final String Y1 = "y1";

	/**
	 * Name for the {@code x2} attribute.
	 */
	protected static final String X2 = "x2";

	/**
	 * Name for the {@code y2} attribute.
	 */
	protected static final String Y2 = "y2";

	/**
	 * Name for the {@code id} attribute.
	 */
	protected static final String ID = "id";

	/**
	 * Name for the {@code sId} attribute.
	 */
	protected static final String SID = "sId";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void read(T sig, XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		super.read(sig, parser);
		readGraph(parser, sig.getGraph(), sig.getBounds());
		readTree(parser, sig.getTree());
	}

	/**
	 * Parse the graph.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	private void readGraph(XmlPullParser parser, IGraph graph, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, GRAPH);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			IVertex vertex = readVertex(parser, bounds);
			graph.put(
					new Point(vertex.getLongitude(), vertex.getLatitude(),
							vertex.getRelativeLongitude(), vertex
									.getRelativeLatitude()), vertex.getPoint());
		}
	}

	/**
	 * Parse the sommet.
	 * 
	 * @param parser
	 *            parser
	 * @param bounds
	 *            map bounds
	 * @return parsed sommet
	 * @throws XmlPullParserException
	 *             error while parsing
	 * @throws IOException
	 *             error with IO
	 * @throws InterruptedException
	 */
	private IVertex readVertex(XmlPullParser parser, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, VERTEX);
		double x = Double.parseDouble(parser.getAttributeValue(null, X));
		double y = Double.parseDouble(parser.getAttributeValue(null, Y));
		List<IPoint> list = new ArrayList<IPoint>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			list.add(readPoint(parser, bounds));
		}
		parser.require(XmlPullParser.END_TAG, null, VERTEX);
		return new Vertex(x, y, x - bounds.getMinLon(), y - bounds.getMinLat(),
				list);
	}

	/**
	 * Parse the tree.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	private void readTree(XmlPullParser parser, ArbreDecision tree)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, TREE);
		Map<Long, BoundingBox> map = readBoundingBoxes(parser);
		tree.root = readNodes(parser, map);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, TREE);
	}

	private Map<Long, BoundingBox> readBoundingBoxes(XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, BOUNDING_BOXES);
		Map<Long, BoundingBox> map = new HashMap<Long, BoundingBox>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			readBoundingBox(parser, map);
		}
		parser.require(XmlPullParser.END_TAG, null, BOUNDING_BOXES);
		return map;
	}

	private void readBoundingBox(XmlPullParser parser,
			Map<Long, BoundingBox> map) throws XmlPullParserException,
			IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, BOUNDING_BOX);
		long id = Long.parseLong(parser.getAttributeValue(null, ID));
		long sId = Long.parseLong(parser.getAttributeValue(null, SID));
		double x1 = Double.parseDouble(parser.getAttributeValue(null, X1));
		double y1 = Double.parseDouble(parser.getAttributeValue(null, Y1));
		double x2 = Double.parseDouble(parser.getAttributeValue(null, X2));
		double y2 = Double.parseDouble(parser.getAttributeValue(null, Y2));
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, BOUNDING_BOX);
		map.put(id, new BoundingBox(sId, x1, y1, x2, y2));
	}

	private INode readNodes(XmlPullParser parser, Map<Long, BoundingBox> map)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, NODES);
		INode node = readNode(parser, map);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, NODES);
		return node;
	}

	/**
	 * Parse the tree.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	private INode readNode(XmlPullParser parser, Map<Long, BoundingBox> map)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		if (parser.nextTag() != XmlPullParser.START_TAG) {
			throw new IOException();
		} else {
			String name = parser.getName();
			if (name.equals(NODE)) {
				return readCNode(parser, map);
			} else if (name.equals(NULL)) {
				return readNullNode(parser);
			} else if (name.equals(LEAF)) {
				return readLeaf(parser, map);
			} else {
				throw new IOException();
			}
		}
	}

	private INode readCNode(XmlPullParser parser, Map<Long, BoundingBox> map)
			throws XmlPullParserException, IOException, InterruptedException {
		double x = Double.parseDouble(parser.getAttributeValue(null, X));
		double y = Double.parseDouble(parser.getAttributeValue(null, Y));
		INode topLeft = readNode(parser, map);
		INode topRight = readNode(parser, map);
		INode bottomLeft = readNode(parser, map);
		INode bottomRight = readNode(parser, map);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, NODE);
		return new Node(topLeft, topRight, bottomLeft, bottomRight, x, y);
	}

	private INode readNullNode(XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, NULL);
		return NullNode.NULL;
	}

	private INode readLeaf(XmlPullParser parser, Map<Long, BoundingBox> map)
			throws XmlPullParserException, IOException, InterruptedException {
		List<BoundingBox> l = new ArrayList<BoundingBox>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			l.add(readLeafBoundingBox(parser, map));
		}
		parser.require(XmlPullParser.END_TAG, null, LEAF);
		return new Leaf(l);
	}

	private BoundingBox readLeafBoundingBox(XmlPullParser parser,
			Map<Long, BoundingBox> map) throws XmlPullParserException,
			IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, BOUNDING_BOX);
		long id = Long.parseLong(parser.getAttributeValue(null, ID));
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, BOUNDING_BOX);
		return map.get(id);
	}

}