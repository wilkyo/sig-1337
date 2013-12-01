package data.sql;

import geometry.model.Point;
import geometry.model.Polygone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.code.sig_1337.model.xml.RouteType;

import data.model.Node;
import data.model.Road;
import data.model.structure.Basin;
import data.model.structure.Building;
import data.model.structure.Forest;
import data.model.structure.Hole;
import data.model.structure.Structure;

public class SQLToXml {

	public static final double ORIGIN_SHIFT = 200 * Math.PI * 6378137 / 2.0;

	/**
	 * Gets all the nodes from the table.
	 * 
	 * @param db
	 *            Connection to the SQL Database.
	 * @return The Map&lt;Integer, Node&gt; of the nodes.
	 */
	private static Map<Long, Node> getAllNodes(Connection db) {
		Map<Long, Node> nodes = new HashMap<Long, Node>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_NODES);
			while (result.next()) {
				Node tmp = new Node(
						result.getLong(SQLHelper.CUSTOM_TABLE_NODES_ID),
						((result.getInt(SQLHelper.CUSTOM_TABLE_NODES_LAT) / ORIGIN_SHIFT) * 180.0),
						((result.getInt(SQLHelper.CUSTOM_TABLE_NODES_LON) / ORIGIN_SHIFT) * 180.0));
				tmp.setLatitude((180 / Math.PI * (2 * Math.atan(Math.exp(tmp
						.getLatitude() * Math.PI / 180.0)) - Math.PI / 2.0)));
				nodes.put(tmp.getId(), tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nodes;
	}

	/**
	 * Gets all the roads from the table.
	 * 
	 * @param db
	 *            Connection to the SQL Database.
	 * @param nodes
	 *            The Map&lt;Integer, Node&gt; of the nodes.
	 * @return The List&lt;Road&gt; of roads.
	 */
	private static List<Road> getAllRoads(Connection db, Map<Long, Node> nodes) {
		List<Road> roads = new ArrayList<Road>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_ROADS);
			while (result.next()) {
				Road tmp = new Road(
						result.getInt(SQLHelper.CUSTOM_TABLE_ROADS_ID),
						result.getString(SQLHelper.CUSTOM_TABLE_ROADS_NAME),
						SQLHelper.getArray(result
								.getArray(SQLHelper.CUSTOM_TABLE_ROADS_NODES),
								nodes),
						result.getInt(SQLHelper.CUSTOM_TABLE_ROADS_TYPE),
						result.getString(SQLHelper.CUSTOM_TABLE_ROADS_GEOM));
				roads.add(tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roads;
	}

	/**
	 * Gets all the buildings from the table.
	 * 
	 * @param db
	 *            Connection to the SQL Database.
	 * @param nodes
	 *            The Map&lt;Integer, Node&gt; of the nodes.
	 * @return The Map&lt;Integer, Building&gt; of buildings.
	 */
	private static Map<Long, Building> getAllBuildings(Connection db,
			Map<Long, Node> nodes) {
		Map<Long, Building> buildings = new HashMap<Long, Building>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + " WHERE "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + " = '"
					+ Structure.BUILDING + "'");
			while (result.next()) {
				Building tmp = new Building(
						result.getInt(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME),
						SQLHelper.getArray(
								result.getArray(SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES),
								nodes),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM));
				// Holes will be set in the getAllHoles method
				buildings.put(tmp.getId(), tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildings;
	}

	private static Map<Long, Forest> getAllForests(Connection db,
			Map<Long, Node> nodes) {
		Map<Long, Forest> forests = new HashMap<Long, Forest>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + " WHERE "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + " = '"
					+ Structure.FOREST + "'");
			while (result.next()) {
				Forest tmp = new Forest(
						result.getInt(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME),
						SQLHelper.getArray(
								result.getArray(SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES),
								nodes),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM));
				// Holes will be set in the getAllHoles method
				forests.put(tmp.getId(), tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return forests;
	}

	private static Map<Long, Basin> getAllBasins(Connection db,
			Map<Long, Node> nodes) {
		Map<Long, Basin> basins = new HashMap<Long, Basin>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + " WHERE "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + " = '"
					+ Structure.BASIN + "'");
			while (result.next()) {
				Basin tmp = new Basin(
						result.getInt(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME),
						SQLHelper.getArray(
								result.getArray(SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES),
								nodes),
						result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM));
				// Holes will be set in the getAllHoles method
				basins.put(tmp.getId(), tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return basins;
	}

	/**
	 * Gets all the holes from the table and adds them to the corresponding
	 * buildings.
	 * 
	 * @param db
	 *            Connection to the SQL Database.
	 * @param nodes
	 *            The Map&lt;Integer, Node&gt; of the nodes.
	 * @param buildings
	 *            The Map&lt;Integer, Building&gt; of the buildings.
	 */
	private static void getAllHoles(Connection db, Map<Long, Node> nodes,
			Map<Long, Building> buildings) {
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_HOLES);
			while (result.next()) {
				Hole tmp = new Hole(
						result.getInt(SQLHelper.CUSTOM_TABLE_HOLES_ID),
						result.getInt(SQLHelper.CUSTOM_TABLE_HOLES_ID_BUILDING),
						SQLHelper.getArray(result
								.getArray(SQLHelper.CUSTOM_TABLE_HOLES_NODES),
								nodes));
				buildings.get(tmp.getIdBuilding()).addHole(tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the XML representation of the Point.
	 * 
	 * @param point
	 *            The Point.
	 * @return The XML String of the Point.
	 */
	private static String pointToXML(Point point) {
		return "<point x=\"" + point.x + "\" y=\"" + point.y + "\" />";
	}

	/**
	 * Returns the XML representation of the Polyedre composed of triangles.
	 * 
	 * @param polygon
	 *            The Polyedre.
	 * @return The XML String of the Polyedre.
	 */
	private static String polygonToXML(Polygone polygon) {
		return "\t\t\t\t\t<triangle>\n" + "\t\t\t\t\t\t"
				+ pointToXML(polygon.points[0]) + "\n" + "\t\t\t\t\t\t"
				+ pointToXML(polygon.points[1]) + "\n" + "\t\t\t\t\t\t"
				+ pointToXML(polygon.points[2]) + "\n"
				+ "\t\t\t\t\t</triangle>\n";
	}

	/**
	 * Returns the bounds of the osm file.
	 * 
	 * @param filename
	 *            The osm file.
	 * @return String of the bounds element.
	 */
	private static String getBounds(String filename) {
		try {
			SAXParserFactory fabrique = SAXParserFactory.newInstance();
			SAXParser parseur = fabrique.newSAXParser();

			File fichier = new File(filename);
			OSMHandler handle = new OSMHandler();
			parseur.parse(fichier, handle);

			return handle.getBounds();
		} catch (ParserConfigurationException e) {
			System.out.println("Erreur de configuration du parseur");
		} catch (SAXException e) {
			System.out.println("Erreur de parsing");
		} catch (IOException e) {
			System.out.println("Erreur d'entrée/sortie");
		}
		return "";
	}

	/**
	 * Return the graph for routing
	 * @param db
	 * 		Connection to the SQL Database
	 * @return
	 * 		The graph
	 */
	private static Map<Point, ArrayList<Point>> getGraph(Connection db) {
		Map<Point, ArrayList<Point>> map = new HashMap<Point, ArrayList<Point>>();
		Statement s;
		try{
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM " + SQLHelper.CUSTOM_GRAPH_SOURCE);
			while (result.next()) {
				Point ori = new Point(result.getDouble(SQLHelper.CUSTOM_GRAPH_POINT_X), result.getDouble(SQLHelper.CUSTOM_GRAPH_POINT_Y));
				Point voisin = new Point(result.getDouble(SQLHelper.CUSTOM_GRAPH_VOISIN_X), result.getDouble(SQLHelper.CUSTOM_GRAPH_VOISIN_Y));
				if(map.containsKey(ori))
					map.get(ori).add(voisin);
				else {
					ArrayList<Point> list = new ArrayList<Point>();
					list.add(voisin);
					map.put(ori, list);
				}
			}
			result = s.executeQuery("SELECT * FROM " + SQLHelper.CUSTOM_GRAPH_TARGET);
			while (result.next()) {
				Point ori = new Point(result.getDouble(SQLHelper.CUSTOM_GRAPH_POINT_X), result.getDouble(SQLHelper.CUSTOM_GRAPH_POINT_Y));
				Point voisin = new Point(result.getDouble(SQLHelper.CUSTOM_GRAPH_VOISIN_X), result.getDouble(SQLHelper.CUSTOM_GRAPH_VOISIN_Y));
				if(map.containsKey(ori))
					map.get(ori).add(voisin);
				else {
					ArrayList<Point> list = new ArrayList<Point>();
					list.add(voisin);
					map.put(ori, list);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Generates the XML for the Android application.
	 * 
	 * @param args
	 * 
	 * @param buildings
	 * @param roads
	 * @param graph 
	 * @param nodes
	 */
	private static void generateXML(String filename, List<Road> roads,
			Map<Long, Building> buildings, Map<Point, ArrayList<Point>> graph) {
		StringBuffer buff = new StringBuffer();
		buff.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sig_1337>\n\t" + getBounds(filename) + "\n\t<graphics>\n");

		/* Bâtiments */
		System.out.println("Generating Buildings...");
		buff.append("\t\t<batiments>\n");
		for (Building b : buildings.values()) {
			buff.append("\t\t\t<batiment nom=\"" + b.getName() + "\">\n");

			List<Polygone> triangles = Node.toPolygon(b.getNodes())
					.toTriangles();
			buff.append("\t\t\t\t<triangles>\n");
			for (Polygone t : triangles) {
				if (t.points.length == 3 && t.points[0] != null
						&& t.points[1] != null && t.points[2] != null)
					buff.append(polygonToXML(t));
			}
			buff.append("\t\t\t\t</triangles>\n");
			for (Hole h : b.getHoles()) {
				List<Polygone> trianglesTrou = Node.toPolygon(h.getNodes())
						.toTriangles();
				buff.append("\t\t\t\t<triangles type=\"trou\">\n");
				for (Polygone t : trianglesTrou) {
					buff.append(polygonToXML(t));
				}
				buff.append("\t\t\t\t</triangles>\n");
			}
			buff.append("\t\t\t</batiment>\n");
		}
		buff.append("\t\t</batiments>\n");
		System.out.println("Buildings generated");

		/* Routes */
		System.out.println("Generating Roads...");
		buff.append("\t\t<routes>\n");
		for (Road r : roads) {
			String type;
			switch (r.getType()) {
			case (Road.CYCLEWAY):
			case (Road.PEDESTRIAN):
			case (Road.PATH):
			case (Road.STEPS):
			case (Road.FOOTWAY):
				type = RouteType.Path.getName();
				break;
			case (Road.TRACK):
			case (Road.RESIDENTIAL):
				type = RouteType.Route.getName();
				break;
			case (Road.PRIMARY):
			case (Road.PRIMARY_LINK):
			case (Road.SECONDARY):
			case (Road.SECONDARY_LINK):
			case (Road.TERTIARY):
			case (Road.TERTIARY_LINK):
			case (Road.SERVICE):
			case (Road.UNCLASSIFIED):
			default:
				type = RouteType.Route.getName();
			}
			buff.append("\t\t\t<route"
					+ (type != null ? " type=\"" + type + "\"" : "") + ">\n");
			buff.append("\t\t\t\t" + pointToXML(r.getNodes()[0].toPoint())
					+ "\n");
			buff.append("\t\t\t\t" + pointToXML(r.getNodes()[1].toPoint())
					+ "\n");
			buff.append("\t\t\t</route>\n");
		}
		buff.append("\t\t</routes>\n");
		System.out.println("Roads generated");
		System.out.println("Generating graph ...");
		buff.append("\t</graphics>\n\t<graph>\n");
		for (Point p : graph.keySet()) {
			buff.append("\t\t<vertex x=\"" + p.x + "\" y=\"" + p.y + "\">\n");
			for (Point voisin : graph.get(p)) {
				buff.append("\t\t\t" + pointToXML(voisin) + "\n");
			}
			buff.append("\t\t</vertex>\n");
		}
		System.out.println("Graph generated");
		buff.append("\t</graph>\n</sig_1337>");
		try {
			FileWriter out = new FileWriter(new File("files/map.xml"));
			out.write(buff.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the data from the PostGIS database and generate the xml for the
	 * Android application.
	 */
	public static void process(String filename) {
		Connection connection;

		try {
			/*
			 * Load the JDBC driver and establish a connection.
			 */
			Class.forName(SQLHelper.SQL_DRIVER);
			connection = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);

			System.out.println("Preprocessing before XML...");
			Map<Long, Node> nodes = getAllNodes(connection);
			List<Road> roads = getAllRoads(connection, nodes);
			Map<Long, Building> buildings = new HashMap<Long, Building>();//getAllBuildings(connection, nodes);
			//getAllHoles(connection, nodes, buildings);

			Map<Point, ArrayList<Point>> graph = getGraph(connection);
			
			System.out.println(nodes.size() + " nodes.");
			System.out.println(roads.size() + " roads.");
			System.out.println(buildings.size() + " buildings.");

			connection.close();

			generateXML(filename, roads, buildings,graph);
			System.out.println("XML generated.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
