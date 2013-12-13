package com.google.code.sig_1337.model.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraphics;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.ITriangle;
import com.google.code.sig_1337.model.xml.ITriangles;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.Triangle;
import com.google.code.sig_1337.model.xml.Triangles;
import com.google.code.sig_1337.model.xml.TrianglesType;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.Route;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.structure.Bassin;
import com.google.code.sig_1337.model.xml.structure.Building;
import com.google.code.sig_1337.model.xml.structure.Foret;
import com.google.code.sig_1337.model.xml.structure.IBassin;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IForet;
import com.google.code.sig_1337.model.xml.structure.IStructure;
import com.google.code.sig_1337.model.xml.structure.IStructures;

/**
 * Handler for the {@code XmlPullParser}.<br/>
 * <br/>
 * 
 * Handles only the graphics.
 */
public class RemoteHandler<U extends ISig1337> implements IHandler<U> {

	/**
	 * Name for the {@code root} tag.
	 */
	protected static final String SIG1337 = "sig_1337";

	/**
	 * Name for the {@code bounds} tag.
	 */
	protected static final String BOUNDS = "bounds";

	/**
	 * Name for the {@code minlat} tag.
	 */
	protected static final String MIN_LAT = "minlat";

	/**
	 * Name for the {@code minlon} tag.
	 */
	protected static final String MIN_LON = "minlon";

	/**
	 * Name for the {@code maxlat} tag.
	 */
	protected static final String MAX_LAT = "maxlat";

	/**
	 * Name for the {@code maxlon} tag.
	 */
	protected static final String MAX_LON = "maxlon";

	/**
	 * Name for the {@code graphics} tag.
	 */
	protected static final String GRAPHICS = "graphics";

	/**
	 * Name for the {@code bassins} tag.
	 */
	protected static final String BASSINS = "bassins";

	/**
	 * Name for the {@code bassin} tag.
	 */
	protected static final String BASSIN = "bassin";

	/**
	 * Name for the {@code forets} tag.
	 */
	protected static final String FORETS = "forets";

	/**
	 * Name for the {@code foret} tag.
	 */
	protected static final String FORET = "foret";

	/**
	 * Name for the {@code buildings} tag.
	 */
	protected static final String BUILDINGS = "batiments";

	/**
	 * Name for the {@code building} tag.
	 */
	protected static final String BUILDING = "batiment";

	/**
	 * Name for the {@code triangles} tag.
	 */
	protected static final String TRIANGLES = "triangles";

	/**
	 * Name for the {@code triangle} tag.
	 */
	protected static final String TRIANGLE = "triangle";

	/**
	 * Name for the {@code name} attribute.
	 */
	protected static final String NAME = "nom";

	/**
	 * Name for the {@code routes} tag.
	 */
	protected static final String ROUTES = "routes";

	/**
	 * Name for the {@code route} tag.
	 */
	protected static final String ROUTE = "route";

	/**
	 * Name for the {@code type} attribute.
	 */
	protected static final String TYPE = "type";

	/**
	 * Name for the {@code point} tag.
	 */
	protected static final String POINT = "point";

	/**
	 * Name for the {@code x} attribute.
	 */
	protected static final String X = "x";

	/**
	 * Name for the {@code y} attribute.
	 */
	protected static final String Y = "y";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(U sig, XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, SIG1337);
		read(sig, parser);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, SIG1337);
	}

	/**
	 * Handle given parser.
	 * 
	 * @param parser
	 *            parser to handle.
	 * @return the result.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void read(U sig, XmlPullParser parser)
			throws XmlPullParserException, IOException, InterruptedException {
		readBounds(parser, sig.getBounds());
		readGraphics(parser, sig.getGraphics(), sig.getBounds());
	}

	/**
	 * Parse the bounds.
	 * 
	 * @param parser
	 *            parser.
	 * @return parsed bounds.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void readBounds(XmlPullParser parser, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, BOUNDS);
		double minLat = Double.parseDouble(parser.getAttributeValue(null,
				MIN_LAT));
		double minLon = Double.parseDouble(parser.getAttributeValue(null,
				MIN_LON));
		double maxLat = Double.parseDouble(parser.getAttributeValue(null,
				MAX_LAT));
		double maxLon = Double.parseDouble(parser.getAttributeValue(null,
				MAX_LON));
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, BOUNDS);
		bounds.setMinLon(minLon);
		bounds.setMinLat(minLat);
		bounds.setMaxLon(maxLon);
		bounds.setMaxLat(maxLat);
	}

	protected static interface Initializer<T> {
		public T initialize(String name);
	}

	/**
	 * Parse the graphics.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed graphics.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void readGraphics(XmlPullParser parser, IGraphics graphics,
			IBounds bounds) throws XmlPullParserException, IOException,
			InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, GRAPHICS);
		// Bassins.
		readStructures(parser, BASSINS, BASSIN, graphics.getBassins(), bounds,
				new Initializer<IBassin>() {
					public IBassin initialize(String name) {
						return new Bassin(name);
					}
				});
		// Forets.
		readStructures(parser, FORETS, FORET, graphics.getForets(), bounds,
				new Initializer<IForet>() {
					public IForet initialize(String name) {
						return new Foret(name);
					}
				});
		// Buildings.
		readStructures(parser, BUILDINGS, BUILDING, graphics.getBuildings(),
				bounds, new Initializer<IBuilding>() {
					public IBuilding initialize(String name) {
						return new Building(name, new ArrayList<IPoint>());
					}
				});
		readRoutes(parser, graphics.getRoutes(), bounds);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, GRAPHICS);
	}

	/**
	 * Parse a list of buildings.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed buildings.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected <T extends IStructure> void readStructures(XmlPullParser parser,
			String tag, String tag2, IStructures<T> structures, IBounds bounds,
			Initializer<T> init) throws XmlPullParserException, IOException,
			InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, tag);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			readStructure(parser, tag2, structures, bounds, init);
		}
		parser.require(XmlPullParser.END_TAG, null, tag);
	}

	/**
	 * Parse a building.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed building.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected <T extends IStructure> void readStructure(XmlPullParser parser,
			String tag, IStructures<T> structures, IBounds bounds,
			Initializer<T> init) throws XmlPullParserException, IOException,
			InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, tag);
		String name = parser.getAttributeValue(null, NAME);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			T t = init.initialize(name);
			structures.add(t);
			readTriangles(parser, t.getTriangles(), bounds);
		}
		parser.require(XmlPullParser.END_TAG, null, tag);
	}

	/**
	 * Parse a list of triangles.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed triangles.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void readTriangles(XmlPullParser parser,
			List<ITriangles> triangles, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, TRIANGLES);
		TrianglesType type = null;
		String s = parser.getAttributeValue(null, TYPE);
		if (s != null) {
			type = TrianglesType.parse(s);
		} else {
			type = TrianglesType.Filled;
		}
		ITriangles t = new Triangles(type);
		triangles.add(t);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			t.add(readTriangle(parser, bounds));
		}
		parser.require(XmlPullParser.END_TAG, null, TRIANGLES);
	}

	/**
	 * Parse a triangle.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed triangle.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected ITriangle readTriangle(XmlPullParser parser, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, TRIANGLE);
		parser.nextTag();
		IPoint p1 = readPoint(parser, bounds);
		parser.nextTag();
		IPoint p2 = readPoint(parser, bounds);
		parser.nextTag();
		IPoint p3 = readPoint(parser, bounds);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, TRIANGLE);
		return new Triangle(p1, p2, p3);
	}

	/**
	 * Parse a list of routes.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed routes.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void readRoutes(XmlPullParser parser, IRoutes routes,
			IBounds bounds) throws XmlPullParserException, IOException,
			InterruptedException {
		checkInterrupted();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, null, ROUTES);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			readRoute(parser, routes, bounds);
		}
		parser.require(XmlPullParser.END_TAG, null, ROUTES);
	}

	/**
	 * Parse a route.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed route.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected void readRoute(XmlPullParser parser, IRoutes routes,
			IBounds bounds) throws XmlPullParserException, IOException,
			InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, ROUTE);
		RouteType type = RouteType.parse(parser.getAttributeValue(null, TYPE));
		parser.nextTag();
		IPoint from = readPoint(parser, bounds);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			checkInterrupted();
			IPoint to = readPoint(parser, bounds);
			routes.add(new Route(type, from, to));
			from = to;
		}
		parser.require(XmlPullParser.END_TAG, null, ROUTE);
	}

	/**
	 * Parse a point.
	 * 
	 * @param parser
	 *            parser.
	 * @param bounds
	 *            map bounds.
	 * @return parsed point.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 * @throws InterruptedException
	 */
	protected IPoint readPoint(XmlPullParser parser, IBounds bounds)
			throws XmlPullParserException, IOException, InterruptedException {
		checkInterrupted();
		parser.require(XmlPullParser.START_TAG, null, POINT);
		float x = Float.parseFloat(parser.getAttributeValue(null, X));
		float y = Float.parseFloat(parser.getAttributeValue(null, Y));
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, null, POINT);
		return new Point(x, y, x - bounds.getMinLon(), y - bounds.getMinLat());
	}

	protected void checkInterrupted() throws InterruptedException {
		// Check if the thread is interrupted.
		if (Thread.interrupted()) {
			Thread.currentThread().interrupt();
			throw new InterruptedException();
		}
	}

}