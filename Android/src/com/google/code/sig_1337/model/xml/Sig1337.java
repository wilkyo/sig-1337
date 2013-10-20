package com.google.code.sig_1337.model.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * XML file.
 */
public class Sig1337 implements ISig1337 {

	/**
	 * Graphics.
	 */
	private final IGraphics graphics;

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
	 * @param graphics
	 *            graphics.
	 */
	private Sig1337(IGraphics graphics) {
		super();
		this.graphics = graphics;
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
	 * Name for the {@code graphics} tag.
	 */
	private static final String GRAPHICS = "graphics";

	/**
	 * Name for the {@code buildings} tag.
	 */
	private static final String BUILDINGS = "batiments";

	/**
	 * Name for the {@code building} tag.
	 */
	private static final String BUILDING = "batiment";

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
			IGraphics graphics = readGraphics(parser);
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, SIG1337);
			return new Sig1337(graphics);
		}

		/**
		 * Parse the graphics.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed graphics.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IGraphics readGraphics(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, GRAPHICS);
			IBuildings buildings = readBuildings(parser);
			IRoutes routes = readRoutes(parser);
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, GRAPHICS);
			return new Graphics(routes, buildings);
		}

		/**
		 * Parse a list of buildings.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed buildings.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IBuildings readBuildings(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			IBuildings buildings = new Buildings();
			parser.require(XmlPullParser.START_TAG, null, BUILDINGS);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				buildings.add(readBuilding(parser));
			}
			parser.require(XmlPullParser.END_TAG, null, BUILDINGS);
			return buildings;
		}

		/**
		 * Parse a building.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed building.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IBuilding readBuilding(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, BUILDING);
			String name = parser.getAttributeValue(null, NAME);
			// TODO points.
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, BUILDING);
			return new Building(name);
		}

		/**
		 * Parse a list of routes.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed routes.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IRoutes readRoutes(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			IRoutes routes = new Routes();
			parser.require(XmlPullParser.START_TAG, null, ROUTES);
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				routes.add(readRoute(parser));
			}
			parser.require(XmlPullParser.END_TAG, null, ROUTES);
			return routes;
		}

		/**
		 * Parse a route.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed route.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IRoute readRoute(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, ROUTE);
			RouteType type = RouteType.parse(parser.getAttributeValue(null,
					TYPE));
			IPoint from = readPoint(parser);
			IPoint to = readPoint(parser);
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, ROUTE);
			return new Route(type, from, to);
		}

		/**
		 * Parse a point.
		 * 
		 * @param parser
		 *            parser.
		 * @return parsed point.
		 * @throws XmlPullParserException
		 *             error while parsing.
		 * @throws IOException
		 *             error with IO.
		 */
		private IPoint readPoint(XmlPullParser parser)
				throws XmlPullParserException, IOException {
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, POINT);
			float x = Float.parseFloat(parser.getAttributeValue(null, X));
			float y = Float.parseFloat(parser.getAttributeValue(null, Y));
			parser.nextTag();
			parser.require(XmlPullParser.END_TAG, null, POINT);
			return new Point(x, y);
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
			toString(sb, INDENT, sig1337.getGraphics());
			sb.append("</");
			sb.append(SIG1337);
			sb.append(">\n");
			return sb.toString();
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
				IBuildings buildings) {
			sb.append(indent);
			sb.append('<');
			sb.append(BUILDINGS);
			sb.append(">\n");
			for (IBuilding b : buildings) {
				toString(sb, indent + INDENT, b);
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
				IBuilding building) {
			sb.append(indent);
			sb.append('<');
			sb.append(BUILDING);
			sb.append(" name=\"");
			sb.append(building.getName());
			sb.append("\">\n");
			// TODO content.
			sb.append(indent);
			sb.append("</");
			sb.append(BUILDING);
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
			sb.append(point.getX());
			sb.append("\" y=\"");
			sb.append(point.getY());
			sb.append("\"/>\n");
		}
	}

}
