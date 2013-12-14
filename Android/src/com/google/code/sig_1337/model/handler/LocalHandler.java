package com.google.code.sig_1337.model.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.code.sig_1337.model.ILocalSig1337;
import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.IVertex;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.Tree;
import com.google.code.sig_1337.model.xml.Tree.INode;
import com.google.code.sig_1337.model.xml.Tree.Leaf;
import com.google.code.sig_1337.model.xml.Tree.XNode;
import com.google.code.sig_1337.model.xml.Tree.YNode;
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
	 * Name for the {@code xnode} tag.
	 */
	private static final String XNODE = "xnode";

	/**
	 * Name for the {@code ynode} tag.
	 */
	private static final String YNODE = "ynode";

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
	private void readTree(XmlPullParser parser, Tree tree)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, TREE);
		tree.root = readNode(parser);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, TREE);
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
	private INode readNode(XmlPullParser parser) throws XmlPullParserException,
			IOException, InterruptedException {
		checkInterrupted();
		if (parser.nextTag() != XmlPullParser.START_TAG) {
			throw new IOException();
		} else {
			String name = parser.getName();
			if (name.equals(XNODE)) {
				return readXNode(parser);
			} else if (name.equals(YNODE)) {
				return readYNode(parser);
			} else if (name.equals(LEAF)) {
				return readLeaf(parser);
			} else {
				throw new IOException();
			}
		}
	}

	private INode readXNode(XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		float x = Float.parseFloat(parser.getAttributeValue(null, X));
		float y = Float.parseFloat(parser.getAttributeValue(null, Y));
		INode left = readNode(parser);
		INode right = readNode(parser);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, XNODE);
		return new XNode(x, y, left, right);
	}

	private INode readYNode(XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		float x1 = Float.parseFloat(parser.getAttributeValue(null, X1));
		float y1 = Float.parseFloat(parser.getAttributeValue(null, Y1));
		float x2 = Float.parseFloat(parser.getAttributeValue(null, X2));
		float y2 = Float.parseFloat(parser.getAttributeValue(null, Y2));
		INode top = readNode(parser);
		INode bottom = readNode(parser);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, YNODE);
		return new YNode(x1, y1, x2, y2, top, bottom);
	}

	private INode readLeaf(XmlPullParser parser) throws XmlPullParserException,
			IOException, InterruptedException {
		String s = parser.getAttributeValue(null, ID);
		int id = s.equals("") ? -1 : Integer.parseInt(s);
		String name = parser.getAttributeValue(null, NAME);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, LEAF);
		return new Leaf(id, name);
	}

}