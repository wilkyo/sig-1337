package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modele.Building;
import modele.Hole;
import modele.Node;
import modele.Road;

public class SQLToXml {

	/**
	 * Gets all the nodes from the table.
	 * 
	 * @param db
	 *            Connection to the SQL Database.
	 * @return The Map&lt;Integer, Node&gt; of the nodes.
	 */
	private static Map<Integer, Node> getAllNodes(Connection db) {
		Map<Integer, Node> nodes = new HashMap<Integer, Node>();
		Statement s;
		try {
			s = db.createStatement();
			ResultSet result = s.executeQuery("SELECT * FROM "
					+ SQLHelper.TABLE_NODES);
			while (result.next()) {
				Node tmp = new Node(result.getInt(1), result.getInt(2),
						result.getInt(3));
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
	private static List<Road> getAllRoads(Connection db,
			Map<Integer, Node> nodes) {
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
				roads.add(tmp.getId(), tmp);
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
	private static Map<Integer, Building> getAllBuildings(Connection db,
			Map<Integer, Node> nodes) {
		Map<Integer, Building> buildings = new HashMap<Integer, Building>();
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
	private static void getAllHoles(Connection db, Map<Integer, Node> nodes,
			Map<Integer, Building> buildings) {
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
	 * Generates the XML for the Android application.
	 * 
	 * @param buildings
	 * @param roads
	 * @param nodes
	 */
	private static void generateXML(Map<Integer, Node> nodes, List<Road> roads,
			Map<Integer, Building> buildings) {
		// TODO
		System.err.println("Not implemented yet.");
	}

	/**
	 * Gets the data from the PostGIS database and generate the xml for the
	 * Android application.
	 */
	public static void main(String[] args) {
		Connection connection;

		try {
			/*
			 * Load the JDBC driver and establish a connection.
			 */
			Class.forName(SQLHelper.SQL_DRIVER);
			connection = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);

			Map<Integer, Node> nodes = getAllNodes(connection);
			List<Road> roads = getAllRoads(connection, nodes);
			Map<Integer, Building> buildings = getAllBuildings(connection,
					nodes);
			getAllHoles(connection, nodes, buildings);

			System.out.println(nodes.size() + " nodes.");
			System.out.println(roads.size() + " roads.");
			System.out.println(buildings.size() + " buildings.");

			connection.close();

			generateXML(nodes, roads, buildings);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
