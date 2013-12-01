package com.google.code.sig_1337.model.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * XML file.
 */
public class Sig1337 implements ISig1337 {

	/**
	 * Background color red component.
	 */
	public static final float BACKGROUND_RED = 248f / 255f;

	/**
	 * Background color green component.
	 */
	public static final float BACKGROUND_GREEN = 244f / 255f;

	/**
	 * Background color blue component.
	 */
	public static final float BACKGROUND_BLUE = 232f / 255f;

	/**
	 * Background color.
	 */
	public static final FloatBuffer BACKGROUND_COLOR;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		BACKGROUND_COLOR = bb.asFloatBuffer();
		BACKGROUND_COLOR.put(new float[] { BACKGROUND_RED, BACKGROUND_GREEN,
				BACKGROUND_BLUE, 1, BACKGROUND_RED, BACKGROUND_GREEN,
				BACKGROUND_BLUE, 1, BACKGROUND_RED, BACKGROUND_GREEN,
				BACKGROUND_BLUE, 1 });
		BACKGROUND_COLOR.position(0);
	}

	/**
	 * Bounds.
	 */
	private final IBounds bounds;

	/**
	 * Graphics.
	 */
	private final IGraphics graphics;

	/**
	 * Graph.
	 */
	private final IGraph graph;

	/**
	 * Parse the given XML file.
	 * 
	 * @param in
	 *            the XML file to parse.
	 * @return the parsed XML file.
	 * @throws XmlPullParserException
	 *             error while parsing.
	 * @throws IOException
	 *             error with IO.
	 */
	public static Sig1337 parse(InputStream in) throws XmlPullParserException,
			IOException {
		XmlPullParser p = Xml.newPullParser();
		p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		p.setInput(in, null);
		return new Handler().handle(p);
	}

	/**
	 * Initializing constructor.
	 * 
	 * @param bounds
	 *            bounds.
	 * @param graphics
	 *            graphics.
	 */
	private Sig1337(IBounds bounds, IGraphics graphics, IGraph graph) {
		super();
		this.bounds = bounds;
		this.graphics = graphics;
		this.graph = graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBounds getBounds() {
		return bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGraphics getGraphics() {
		return graphics;
	}

	/**
	 * Name for the {@code root} tag.
	 */
	private static final String SIG1337 = "sig_1337";

	/**
	 * Name for the {@code bounds} tag.
	 */
	private static final String BOUNDS = "bounds";

	/**
	 * Name for the {@code minlat} tag.
	 */
	private static final String MIN_LAT = "minlat";

	/**
	 * Name for the {@code minlon} tag.
	 */
	private static final String MIN_LON = "minlon";

	/**
	 * Name for the {@code maxlat} tag.
	 */
	private static final String MAX_LAT = "maxlat";

	/**
	 * Name for the {@code maxlon} tag.
	 */
	private static final String MAX_LON = "maxlon";

	/**
	 * Name for the {@code graphics} tag.
	 */
	private static final String GRAPHICS = "graphics";

	/**
	 * Name for the {@code bassins} tag.
	 */
	private static final String BASSINS = "bassins";

	/**
	 * Name for the {@code bassin} tag.
	 */
	private static final String BASSIN = "bassin";

	/**
	 * Name for the {@code forets} tag.
	 */
	private static final String FORETS = "forets";

	/**
	 * Name for the {@code foret} tag.
	 */
	private static final String FORET = "foret";

	/**
	 * Name for the {@code buildings} tag.
	 */
	private static final String BUILDINGS = "batiments";

	/**
	 * Name for the {@code building} tag.
	 */
	private static final String BUILDING = "batiment";

	/**
	 * Name for the {@code triangles} tag.
	 */
	private static final String TRIANGLES = "triangles";

	/**
	 * Name for the {@code triangle} tag.
	 */
	private static final String TRIANGLE = "triangle";

	/**
	 * Name for the {@code name} attribute.
	 */
	private static final String NAME = "nom";

	/**
	 * Name for the {@code routes} tag.
	 */
	private static final String ROUTES = "routes";

	/**
	 * Name for the {@code route} tag.
	 */
	private static final String ROUTE = "route";

	/**
	 * Name for the {@code type} attribute.
	 */
	private static final String TYPE = "type";

	/**
	 * Name for the {@code point} tag.
	 */
	private static final String POINT = "point";

	/**
	 * Name for the {@code x} attribute.
	 */
	private static final String X = "x";

	/**
	 * Name for the {@code y} attribute.
	 */
	private static final String Y = "y";

	/**
	 * Name for the {@code graph} tag.
	 */
	private static final String GRAPH = "graph";

	/**
	 * Name for the {@code tag} tag.
	 */
	private static final String VERTEX = "vertex";

	/**
	 * Handler for the {@code XmlPullParser}.
	 */
	private static class Handler {

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
		 */
		public Sig1337 handle(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, SIG1337);
			IBounds bounds = readBounds(parser);
			IGraphics graphics = readGraphics(parser, bounds);
			IGraph graph = readGraph(parser, bounds);
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, SIG1337);
			return new Sig1337(bounds, graphics, graph);
		}

		/**
		 * Parse the graph.
		 * 
		 * @param parser
		 *            parser.
		 * @param bounds
		 *            map bounds.
		 * @return parsed graph.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IGraph readGraph(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, GRAPH);
			IGraph graph = new Graph();
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				graph.add(readVertex(parser, bounds));
			}
			return graph;
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
		 */
		private IVertex readVertex(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, VERTEX);
			double x = Double.parseDouble(parser.getAttributeValue(null, X));
			double y = Double.parseDouble(parser.getAttributeValue(null, X));
			List<IPoint> list = new ArrayList<IPoint>();
			do {
				try {
					list.add(readPoint(parser, bounds));
				} catch (XmlPullParserException e) {
					Log.d("pouet",
							"Fin des voisins du sommet (TODO : virer le catch)");
				}
			} while (!parser.getName().equals(VERTEX));
			parser.require(XmlPullParser.END_TAG, null, VERTEX);
			return new Vertex(x, y, x - bounds.getMinLon(), y
					- bounds.getMinLat(), list);
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
		 */
		private IBounds readBounds(XmlPullParser parser)
				throws XmlPullParserException, IOException {
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
			return new Bounds(minLat, minLon, maxLat, maxLon);
		}

		private static interface Initializer<T> {
			public T initialize(String name, List<ITriangles> triangles);
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
		 */
		private IGraphics readGraphics(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, GRAPHICS);
			// Bassins.
			IStructures<IBassin> bassins = readStructures(parser, BASSINS,
					BASSIN, bounds, new Initializer<IBassin>() {
						public IBassin initialize(String name,
								List<ITriangles> triangles) {
							return new Bassin(name, triangles);
						}
					});
			// Forets.
			IStructures<IForet> forets = readStructures(parser, FORETS, FORET,
					bounds, new Initializer<IForet>() {
						public IForet initialize(String name,
								List<ITriangles> triangles) {
							return new Foret(name, triangles);
						}
					});
			// Buildings.
			IStructures<IBuilding> buildings = readStructures(parser,
					BUILDINGS, BUILDING, bounds, new Initializer<IBuilding>() {
						public IBuilding initialize(String name,
								List<ITriangles> triangles) {
							return new Building(name, triangles);
						}
					});
			IRoutes routes = readRoutes(parser, bounds);
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, GRAPHICS);
			return new Graphics(routes, buildings, forets, bassins);
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
		 */
		private <T extends IStructure> IStructures<T> readStructures(
				XmlPullParser parser, String tag, String tag2, IBounds bounds,
				Initializer<T> init) throws XmlPullParserException, IOException {
			parser.nextTag();
			IStructures<T> structures = new Structures<T>();
			parser.require(XmlPullParser.START_TAG, null, tag);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				structures.add(readStructure(parser, tag2, bounds, init));
			}
			parser.require(XmlPullParser.END_TAG, null, tag);
			return structures;
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
		 */
		private <T extends IStructure> T readStructure(XmlPullParser parser,
				String tag, IBounds bounds, Initializer<T> init)
				throws XmlPullParserException, IOException {
			List<ITriangles> triangles = new ArrayList<ITriangles>();
			parser.require(XmlPullParser.START_TAG, null, tag);
			String name = parser.getAttributeValue(null, NAME);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				triangles.add(readTriangles(parser, bounds));
			}
			parser.require(XmlPullParser.END_TAG, null, tag);
			return init.initialize(name, triangles);
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
		 */
		private ITriangles readTriangles(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, TRIANGLES);
			TrianglesType type = null;
			String s = parser.getAttributeValue(null, TYPE);
			if (s != null) {
				type = TrianglesType.parse(s);
			} else {
				type = TrianglesType.Filled;
			}
			ITriangles triangles = new Triangles(type);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				triangles.add(readTriangle(parser, bounds));
			}
			parser.require(XmlPullParser.END_TAG, null, TRIANGLES);
			return triangles;
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
		 */
		private ITriangle readTriangle(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
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
		 */
		private IRoutes readRoutes(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			IRoutes routes = new Routes();
			parser.require(XmlPullParser.START_TAG, null, ROUTES);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				routes.addAll(readRoute(parser, bounds));
			}
			parser.require(XmlPullParser.END_TAG, null, ROUTES);
			return routes;
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
		 */
		private List<IRoute> readRoute(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, ROUTE);
			List<IRoute> l = new ArrayList<IRoute>();
			RouteType type = RouteType.parse(parser.getAttributeValue(null,
					TYPE));
			parser.nextTag();
			IPoint from = readPoint(parser, bounds);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				IPoint to = readPoint(parser, bounds);
				l.add(new Route(type, from, to));
				from = to;
			}
			parser.require(XmlPullParser.END_TAG, null, ROUTE);
			return l;
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
		 */
		private IPoint readPoint(XmlPullParser parser, IBounds bounds)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, POINT);
			float x = Float.parseFloat(parser.getAttributeValue(null, X));
			float y = Float.parseFloat(parser.getAttributeValue(null, Y));
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, POINT);
			return new Point(x, y, x - bounds.getMinLon(), y
					- bounds.getMinLat());
		}

	}

	/**
	 * Format a {@code Sig1337}.
	 */
	public static class Format implements ISig1337Format {

		/**
		 * Indentation symbol.
		 */
		private static final String INDENT = " ";

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(ISig1337 sig1337) {
			StringBuilder sb = new StringBuilder();
			sb.append('<');
			sb.append(SIG1337);
			sb.append(">\n");
			toString(sb, INDENT, sig1337.getBounds());
			toString(sb, INDENT, sig1337.getGraphics());
			toString(sb, INDENT, sig1337.getGraph());
			sb.append("</");
			sb.append(SIG1337);
			sb.append(">\n");
			return sb.toString();
		}

		/**
		 * Format the given graph.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param graph
		 *            graph to format.
		 */
		private void toString(StringBuilder sb, String indent, IGraph graph) {
			sb.append(indent);
			sb.append('<');
			sb.append(GRAPH);
			sb.append(">\n");
			for (IVertex vertex : graph) {
				toString(sb, indent + INDENT, vertex);
			}
			sb.append("</");
			sb.append(GRAPH);
			sb.append(">\n");

		}

		/**
		 * Format the given vertex.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param vertex
		 *            vertex to format.
		 */
		private void toString(StringBuilder sb, String indent, IVertex vertex) {
			sb.append(indent);
			sb.append("<");
			sb.append(VERTEX);
			sb.append(" x=\"");
			sb.append(vertex.getLongitude());
			sb.append("\" y=\"");
			sb.append(vertex.getLatitude());
			sb.append("\">\n");
			for (IPoint point : vertex.getPoint()) {
				toString(sb, indent + INDENT, point);
			}
			sb.append("</");
			sb.append(VERTEX);
			sb.append(">\n");
		}

		/**
		 * Format the given bounds.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param bounds
		 *            bounds to format.
		 */
		private void toString(StringBuilder sb, String indent, IBounds bounds) {
			sb.append(indent);
			sb.append('<');
			sb.append(BOUNDS);
			sb.append(' ');
			sb.append(MIN_LAT);
			sb.append("=\"");
			sb.append(bounds.getMinLat());
			sb.append("\" ");
			sb.append(MIN_LON);
			sb.append("=\"");
			sb.append(bounds.getMinLon());
			sb.append("\" ");
			sb.append(MAX_LAT);
			sb.append("=\"");
			sb.append(bounds.getMaxLat());
			sb.append("\" ");
			sb.append(MAX_LON);
			sb.append("=\"");
			sb.append(bounds.getMaxLon());
			sb.append("\"");
			sb.append(indent);
			sb.append("/>\n");
		}

		/**
		 * Format the given graphics.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param graphics
		 *            graphics to format.
		 */
		private void toString(StringBuilder sb, String indent,
				IGraphics graphics) {
			sb.append(indent);
			sb.append('<');
			sb.append(GRAPHICS);
			sb.append(">\n");
			toString(sb, indent + INDENT, graphics.getBassins());
			toString(sb, indent + INDENT, graphics.getForets());
			toString(sb, indent + INDENT, graphics.getBuildings());
			toString(sb, indent + INDENT, graphics.getRoutes());
			sb.append(indent);
			sb.append("</");
			sb.append(GRAPHICS);
			sb.append(">\n");
		}

		/**
		 * Format the given buildings.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param buildings
		 *            buildings to format.
		 */
		private void toString(StringBuilder sb, String indent,
				IStructures<? extends IStructure> structures) {
			sb.append(indent);
			sb.append('<');
			sb.append(BUILDINGS);
			sb.append(">\n");
			for (IStructure s : structures) {
				toString(sb, indent + INDENT, s);
			}
			sb.append(indent);
			sb.append("</");
			sb.append(BUILDINGS);
			sb.append(">\n");
		}

		/**
		 * Format the given building.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param building
		 *            building to format.
		 */
		private void toString(StringBuilder sb, String indent,
				IStructure structure) {
			sb.append(indent);
			sb.append('<');
			sb.append(BUILDING);
			sb.append(" name=\"");
			sb.append(structure.getName());
			sb.append("\">\n");
			for (ITriangles triangles : structure.getTriangles()) {
				toString(sb, indent + INDENT, triangles);
			}
			sb.append(indent);
			sb.append("</");
			sb.append(BUILDING);
			sb.append(">\n");
		}

		/**
		 * Format the given triangles.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param triangles
		 *            triangles to format.
		 */
		private void toString(StringBuilder sb, String indent,
				ITriangles triangles) {
			sb.append(indent);
			sb.append('<');
			sb.append(TRIANGLES);
			if (triangles.getType() != null) {
				sb.append(" type=\"");
				sb.append(triangles.getType().getName());
				sb.append('\"');
			}
			sb.append(">\n");
			for (ITriangle t : triangles) {
				toString(sb, indent + INDENT, t);
			}
			sb.append(indent);
			sb.append("</");
			sb.append(TRIANGLES);
			sb.append(">\n");
		}

		/**
		 * Format the given triangle.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param triangle
		 *            triangle to format.
		 */
		private void toString(StringBuilder sb, String indent,
				ITriangle triangle) {
			sb.append(indent);
			sb.append('<');
			sb.append(TRIANGLE);
			sb.append(">\n");
			toString(sb, indent + INDENT, triangle.getP1());
			toString(sb, indent + INDENT, triangle.getP2());
			toString(sb, indent + INDENT, triangle.getP3());
			sb.append(indent);
			sb.append("</");
			sb.append(TRIANGLE);
			sb.append(">\n");
		}

		/**
		 * Format the given routes.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param routes
		 *            routes to format.
		 */
		private void toString(StringBuilder sb, String indent, IRoutes routes) {
			sb.append(indent);
			sb.append('<');
			sb.append(ROUTES);
			sb.append(">\n");
			for (IRoute r : routes) {
				toString(sb, indent + INDENT, r);
			}
			sb.append(indent);
			sb.append("</");
			sb.append(ROUTES);
			sb.append(">\n");
		}

		/**
		 * Format the given route.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param route
		 *            route to format.
		 */
		private void toString(StringBuilder sb, String indent, IRoute route) {
			sb.append(indent);
			sb.append('<');
			sb.append(ROUTE);
			sb.append(" type=\"");
			sb.append(route.getType().getName());
			sb.append("\">\n");
			toString(sb, indent + INDENT, route.getFrom());
			toString(sb, indent + INDENT, route.getTo());
			sb.append(indent);
			sb.append("</");
			sb.append(ROUTE);
			sb.append(">\n");
		}

		/**
		 * Format the given point.
		 * 
		 * @param sb
		 *            string builder.
		 * @param indent
		 *            indentation.
		 * @param point
		 *            point to format.
		 */
		private void toString(StringBuilder sb, String indent, IPoint point) {
			sb.append(indent);
			sb.append("<point x=\"");
			sb.append(point.getLongitude());
			sb.append("\" y=\"");
			sb.append(point.getLatitude());
			sb.append("\"/>\n");
		}
	}

	@Override
	public IGraph getGraph() {
		return graph;
	}

}
