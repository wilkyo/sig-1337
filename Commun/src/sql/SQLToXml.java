package sql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import modele.Building;
import modele.Hole;
import modele.Node;
import modele.Road;

import org.xml.sax.SAXException;

import base.Point;
import base.Polyedre;

import com.google.code.sig_1337.model.xml.RouteType;

public class SQLToXml {

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
						result.getInt(SQLHelper.CUSTOM_TABLE_NODES_ID),
						result.getInt(SQLHelper.CUSTOM_TABLE_NODES_LAT),
						result.getInt(SQLHelper.CUSTOM_TABLE_NODES_LON));
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
						result.getInt(SQLHelper.CUSTOM_TABLE_ROADS_TYPE));
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
					+ SQLHelper.CUSTOM_TABLE_BUILDINGS);
			while (result.next()) {
				Building tmp = new Building(
						result.getInt(SQLHelper.CUSTOM_TABLE_BUILDINGS_ID),
						result.getString(SQLHelper.CUSTOM_TABLE_BUILDINGS_NAME),
						SQLHelper.getArray(
								result.getArray(SQLHelper.CUSTOM_TABLE_BUILDINGS_NODES),
								nodes));
				// Holes will be set in the getAllHoles method
				buildings.put(tmp.getId(), tmp);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildings;
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
	private static String polygonToXML(Polyedre polygon) {
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
	 * Generates the XML for the Android application.
	 * 
	 * @param args
	 * 
	 * @param buildings
	 * @param roads
	 * @param nodes
	 */
	private static void generateXML(String filename, List<Road> roads,
			Map<Long, Building> buildings) {
		StringBuffer buff = new StringBuffer();
		buff.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<sig_1337>\n\t" + getBounds(filename) + "\n\t<graphics>\n");

		/* Bâtiments */
		System.out.println("Generating Buildings...");
		buff.append("\t\t<batiments>\n");
		for (Building b : buildings.values()) {
			buff.append("\t\t\t<batiment nom=\"" + b.getName() + "\">\n");
			List<Polyedre> triangles = Node.toPolygon(b.getNodes())
					.toTriangles();
			buff.append("\t\t\t\t<triangles>\n");
			for (Polyedre t : triangles) {
				buff.append(polygonToXML(t));
			}
			buff.append("\t\t\t\t</triangles>\n");
			for (Hole h : b.getHoles()) {
				List<Polyedre> trianglesTrou = Node.toPolygon(h.getNodes())
						.toTriangles();
				buff.append("\t\t\t\t<triangles type=\"trou\">\n");
				for (Polyedre t : trianglesTrou) {
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

		buff.append("\t</graphics>\n" + "</sig_1337>");
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
	public static void main(String[] args) {
		if (args.length == 0)
			args = new String[] { "files/Universite.osm" };
		Connection connection;

		try {
			/*
			 * Load the JDBC driver and establish a connection.
			 */
			Class.forName(SQLHelper.SQL_DRIVER);
			connection = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);

			Map<Long, Node> nodes = getAllNodes(connection);
			List<Road> roads = getAllRoads(connection, nodes);
			Map<Long, Building> buildings = getAllBuildings(connection, nodes);
			getAllHoles(connection, nodes, buildings);

			System.out.println(nodes.size() + " nodes.");
			System.out.println(roads.size() + " roads.");
			System.out.println(buildings.size() + " buildings.");

			connection.close();

			generateXML(args[0], roads, buildings);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
