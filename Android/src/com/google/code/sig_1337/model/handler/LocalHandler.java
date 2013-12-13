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
	 * {@inheritDoc}
	 */
	@Override
	protected void read(T sig, XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		super.read(sig, parser);
		readGraph(parser, sig.getGraph(), sig.getBounds());
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
			graph.add(readVertex(parser, bounds));
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
		double y = Double.parseDouble(parser.getAttributeValue(null, X));
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

}