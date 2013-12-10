package data.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sun.security.util.BigInt;
import data.model.Node;
import data.model.Road;
import data.model.structure.Building;
import data.model.structure.Structure;

public class SQLCreate {

	public static Boolean createDataBase() {
		Boolean result = false;
		System.out.println("Début création base de données");
		if (dataBaseExists()) {
			// Récupération des nodes
			if (createTableNodes() && createTableStructures()
					&& createTableRoads() && createTableHoles()
					&& createTableGraph() && addStructuresNeighbors()) {
				result = true;
			}
		}
		System.out.println("Fin création base de données");
		return result;
	}

	private static boolean createTableGraph() {
		Boolean result = true;
		System.out.println("Début création des tables du graphes");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();
			System.out.println("Lecture du fichier");

			StringBuilder sql = new StringBuilder();

			BufferedReader br = new BufferedReader(new FileReader(
					"files/osm_2po_4pgr.sql"));
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				sql.append(line);
			}
			br.close();

			s.execute(sql.toString());

			conn.close();
		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création des tables du graphes");
		return result;
	}

	private static Boolean dataBaseExists() {
		Boolean result = true;
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			conn.close();
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

	private static Boolean createTableNodes() {
		Boolean result = true;
		System.out.println("Début création Nodes");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();

			// Création de la structure de la table
			StringBuilder mySql = new StringBuilder();
			mySql.append("DROP TABLE IF EXISTS " + SQLHelper.CUSTOM_TABLE_NODES
					+ ";");
			mySql.append("CREATE TABLE " + SQLHelper.CUSTOM_TABLE_NODES + " (");
			mySql.append(SQLHelper.CUSTOM_TABLE_NODES_ID + " bigint NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_NODES_LAT
					+ " integer NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_NODES_LON
					+ " integer NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_NODES_GEOM
					+ " geometry NOT NULL,");
			mySql.append("CONSTRAINT " + SQLHelper.CUSTOM_TABLE_NODES
					+ "_pkey PRIMARY KEY (" + SQLHelper.CUSTOM_TABLE_NODES_ID
					+ ")");
			mySql.append(")");
			mySql.append("WITH (OIDS=FALSE);");
			mySql.append("ALTER TABLE " + SQLHelper.CUSTOM_TABLE_NODES
					+ " OWNER TO postgres;");
			s.execute(mySql.toString());

			// Récupération des données
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_NODES + " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_NODES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_NODES_LAT + ", "
					+ SQLHelper.CUSTOM_TABLE_NODES_LON + ", "
					+ SQLHelper.CUSTOM_TABLE_NODES_GEOM + ") ");
			mySql.append("SELECT id, lat, lon, ST_SetSRID(ST_MakePoint(lon,lat), 900913) ");
			mySql.append("FROM " + SQLHelper.TABLE_NODES + ";");
			s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création Nodes");
		return result;
	}

	private static Boolean createTableStructures() {
		Boolean result = true;
		System.out.println("Début création des structures");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();

			// Création de la structure de la table
			StringBuilder mySql = new StringBuilder();
			mySql.append("DROP TABLE IF EXISTS "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + ";");
			mySql.append("CREATE TABLE " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " (");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID
					+ " bigint NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES
					+ " bigint[] NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + " text NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM
					+ " geometry NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE
					+ " text NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_NEIGHBORS
					+ " bigint[] NULL,");
			mySql.append("CONSTRAINT " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ "_pkey PRIMARY KEY ("
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ")");
			mySql.append(")");
			mySql.append("WITH (OIDS=FALSE);");
			mySql.append("ALTER TABLE " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " OWNER TO postgres;");
			s.execute(mySql.toString());

			// Récupération des données pour les buildings
			System.out.println("Début insertion des buildings");
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + ") ");
			mySql.append("SELECT w.id, w.nodes, p.name, p.way, '"
					+ Structure.BUILDING + "' AS typeStructure ");
			mySql.append("FROM " + SQLHelper.TABLE_POLYGONS + " p ");
			mySql.append("INNER JOIN " + SQLHelper.TABLE_WAYS
					+ " w ON w.id = p.osm_id ");
			mySql.append("WHERE p.building IS NOT NULL AND p.building != '';");
			s.execute(mySql.toString());

			// On prend aussi ceux avec des trous ...
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + ") ");
			mySql.append("SELECT w.id, w.nodes, p.name, p.way, '"
					+ Structure.BUILDING + "' AS typeStructure ");
			mySql.append("FROM " + SQLHelper.TABLE_POLYGONS + " p ");
			mySql.append("INNER JOIN planet_osm_rels r ON ABS(p.osm_id) = r.id ");
			mySql.append("INNER JOIN planet_osm_ways w ON ((r.members[2] = 'outer' AND w.id = CAST(SUBSTRING(r.members[1], 2) AS BIGINT)) OR (r.members[4] = 'outer' AND w.id = CAST(SUBSTRING(r.members[3], 2) AS BIGINT))) ");
			mySql.append("WHERE p.osm_id < 0 AND p.building IS NOT NULL AND p.building != '' AND w.id NOT IN (SELECT id FROM sig1337_structures);");
			s.execute(mySql.toString());

			System.out.println("Fin insertion des buildings");

			// Récupération des données pour les forest
			System.out.println("Début insertion des forests");
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + ") ");
			mySql.append("SELECT w.id, w.nodes, p.name, p.way, '"
					+ Structure.FOREST + "' AS typeStructure ");
			mySql.append("FROM " + SQLHelper.TABLE_POLYGONS + " p ");
			mySql.append("INNER JOIN " + SQLHelper.TABLE_WAYS
					+ " w ON w.id = p.osm_id ");
			mySql.append("WHERE landuse = 'forest';");
			s.execute(mySql.toString());
			System.out.println("Fin insertion des forests");

			// Récupération des données pour les basins
			System.out.println("Début insertion des basins");
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + ") ");
			mySql.append("SELECT w.id, w.nodes, p.name, p.way, '"
					+ Structure.BASIN + "' AS typeStructure ");
			mySql.append("FROM " + SQLHelper.TABLE_POLYGONS + " p ");
			mySql.append("INNER JOIN " + SQLHelper.TABLE_WAYS
					+ " w ON w.id = p.osm_id ");
			mySql.append("WHERE landuse = 'basin';");
			s.execute(mySql.toString());

			// On prend aussi ceux avec des trous ...
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + ") ");
			mySql.append("SELECT w.id, w.nodes, p.name, p.way, '"
					+ Structure.BASIN + "' AS typeStructure ");
			mySql.append("FROM " + SQLHelper.TABLE_POLYGONS + " p ");
			mySql.append("INNER JOIN planet_osm_rels r ON ABS(p.osm_id) = r.id ");
			mySql.append("INNER JOIN planet_osm_ways w ON ((r.members[2] = 'outer' AND w.id = CAST(SUBSTRING(r.members[1], 2) AS BIGINT)) OR (r.members[4] = 'outer' AND w.id = CAST(SUBSTRING(r.members[3], 2) AS BIGINT))) ");
			mySql.append("WHERE p.osm_id < 0 AND p.landuse = 'basin';");
			s.execute(mySql.toString());

			System.out.println("Fin insertion des basins");

			mySql = new StringBuilder();
			mySql.append("DELETE FROM " + SQLHelper.TABLE_GEOMETRY_COLUMNS
					+ " WHERE f_table_name = '"
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + "'; ");
			mySql.append("INSERT INTO " + SQLHelper.TABLE_GEOMETRY_COLUMNS
					+ " VALUES ('', '" + SQLHelper.DB_SCHEMA + "', '"
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + "', '"
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + "', "
					+ SQLHelper.GEOMETRY_POLYGON_DIMENSIONS + ", "
					+ SQLHelper.OSM_SRID + ", '" + SQLHelper.GEOMETRY_POLYGON
					+ "'); ");
			s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création des structures");
		return result;
	}

	private static Boolean createTableRoads() {
		Boolean result = true;
		System.out.println("Début création Roads");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();

			// Création de la structure de la table
			StringBuilder mySql = new StringBuilder();
			mySql.append("DROP TABLE IF EXISTS " + SQLHelper.CUSTOM_TABLE_ROADS
					+ ";");
			mySql.append("CREATE TABLE " + SQLHelper.CUSTOM_TABLE_ROADS + " (");
			mySql.append(SQLHelper.CUSTOM_TABLE_ROADS_ID + " bigint NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_ROADS_NODES
					+ " bigint[] NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_ROADS_NAME + " text NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_ROADS_TYPE + " int NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_ROADS_GEOM
					+ " geometry NOT NULL,");
			mySql.append("CONSTRAINT " + SQLHelper.CUSTOM_TABLE_ROADS
					+ "_pkey PRIMARY KEY (" + SQLHelper.CUSTOM_TABLE_ROADS_ID
					+ ")");
			mySql.append(")");
			mySql.append("WITH (OIDS=FALSE);");
			mySql.append("ALTER TABLE " + SQLHelper.CUSTOM_TABLE_ROADS
					+ " OWNER TO postgres;");
			s.execute(mySql.toString());

			// Récupération des données
			mySql = new StringBuilder();
			mySql.append("SELECT w.id, w.nodes, ");
			// on récupère le nom du tableau "tags"
			mySql.append("(regexp_split_to_array(substring(array_to_string(w.tags,',','') from 'name,(.*)'), ','))[1], ");
			mySql.append("(regexp_split_to_array(substring(array_to_string(w.tags,',','') from 'highway,(.*)'), ','))[1], ");
			mySql.append("l.way ");
			mySql.append("FROM " + SQLHelper.TABLE_WAYS + " w ");
			mySql.append("INNER JOIN " + SQLHelper.TABLE_LINES
					+ " l ON l.osm_id = w.id ");
			mySql.append("WHERE array_to_string(w.tags,',','') LIKE '%highway%';");
			ResultSet myResultSet = s.executeQuery(mySql.toString());
			mySql = new StringBuilder();
			while (myResultSet.next()) {
				long id = myResultSet.getLong(1);
				long[] nodes = SQLHelper.getLongArray(myResultSet.getArray(2));
				String name = myResultSet.getString(3);
				int type = Road.UNCLASSIFIED;
				String sType = myResultSet.getString(4);
				String sWay = myResultSet.getString(5);
				if (sType != null) {
					switch (myResultSet.getString(4).toUpperCase()) {
					case "UNCLASSIFIED":
						type = Road.UNCLASSIFIED;
						break;
					case "PRIMARY":
						type = Road.PRIMARY;
						break;
					case "PRIMARY_LINK":
						type = Road.PRIMARY_LINK;
						break;
					case "SECONDARY":
						type = Road.SECONDARY;
						break;
					case "SECONDARY_LINK":
						type = Road.SECONDARY_LINK;
						break;
					case "TERTIARY":
						type = Road.TERTIARY;
						break;
					case "TERTIARY_LINK":
						type = Road.TERTIARY_LINK;
						break;
					case "PEDESTRIAN":
						type = Road.PEDESTRIAN;
						break;
					case "RESIDENTIAL":
						type = Road.RESIDENTIAL;
						break;
					case "SERVICE":
						type = Road.SERVICE;
						break;
					case "TRACK":
						type = Road.TRACK;
						break;
					case "PATH":
						type = Road.PATH;
						break;
					case "FOOTWAY":
						type = Road.FOOTWAY;
						break;
					case "STEPS":
						type = Road.STEPS;
					case "CYCLEWAY":
						type = Road.CYCLEWAY;
						break;
					}
				}
				mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_ROADS
						+ " ");
				mySql.append("(" + SQLHelper.CUSTOM_TABLE_ROADS_ID + ", "
						+ SQLHelper.CUSTOM_TABLE_ROADS_NODES + ", "
						+ SQLHelper.CUSTOM_TABLE_ROADS_NAME + ", "
						+ SQLHelper.CUSTOM_TABLE_ROADS_TYPE + ", "
						+ SQLHelper.CUSTOM_TABLE_ROADS_GEOM + ")");
				mySql.append(" VALUES ");
				mySql.append("("
						+ id
						+ ", '"
						+ Arrays.toString(nodes).replace("[", "{")
								.replace("]", "}")
						+ "', "
						+ (name != null ? "'" + name.replace("'", "''") + "' "
								: "''") + ", " + type + ", '" + sWay + "'); ");
				mySql.append("DELETE FROM " + SQLHelper.TABLE_GEOMETRY_COLUMNS
						+ " WHERE f_table_name = '"
						+ SQLHelper.CUSTOM_TABLE_ROADS + "'; ");
				mySql.append("INSERT INTO " + SQLHelper.TABLE_GEOMETRY_COLUMNS
						+ " VALUES ('', '" + SQLHelper.DB_SCHEMA + "', '"
						+ SQLHelper.CUSTOM_TABLE_ROADS + "', '"
						+ SQLHelper.CUSTOM_TABLE_ROADS_GEOM + "', "
						+ SQLHelper.GEOMETRY_LINE_DIMENSIONS + ", "
						+ SQLHelper.OSM_SRID + ", '" + SQLHelper.GEOMETRY_LINE
						+ "'); ");
			}
			if (mySql.length() > 0)
				s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
			System.err.println("Erreur lors de la création des roads.");
			result = false;
		}
		System.out.println("Fin création Roads");
		return result;
	}

	private static Boolean createTableHoles() {
		Boolean result = true;
		System.out.println("Début création Holes");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();

			// Création de la structure de la table
			StringBuilder mySql = new StringBuilder();
			mySql.append("DROP TABLE IF EXISTS " + SQLHelper.CUSTOM_TABLE_HOLES
					+ ";");
			mySql.append("CREATE TABLE " + SQLHelper.CUSTOM_TABLE_HOLES + " (");
			mySql.append(SQLHelper.CUSTOM_TABLE_HOLES_ID + " bigint NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_HOLES_NODES
					+ " bigint[] NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_HOLES_ID_STRUCTURE
					+ " bigint NOT NULL,");
			mySql.append("CONSTRAINT " + SQLHelper.CUSTOM_TABLE_HOLES
					+ "_pkey PRIMARY KEY (" + SQLHelper.CUSTOM_TABLE_HOLES_ID
					+ ")");
			mySql.append(")");
			mySql.append("WITH (OIDS=FALSE);");
			mySql.append("ALTER TABLE " + SQLHelper.CUSTOM_TABLE_HOLES
					+ " OWNER TO postgres;");
			s.execute(mySql.toString());

			// Récupération des données
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_HOLES + " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_HOLES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_HOLES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_HOLES_ID_STRUCTURE + ") ");
			mySql.append("SELECT rel.id, ways2.nodes, builing.id ");
			mySql.append("FROM " + SQLHelper.TABLE_RELS + " rel ");
			mySql.append("INNER JOIN "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES
					+ " builing ON builing.Id = CAST(substring((regexp_split_to_array(array_to_string(rel.members,',',''),','))[3] from 2) AS bigint) ");
			mySql.append("INNER JOIN "
					+ SQLHelper.TABLE_WAYS
					+ " ways2 ON ways2.Id = CAST(substring((regexp_split_to_array(array_to_string(rel.members,',',''),','))[1] from 2) AS bigint) ");
			mySql.append("WHERE array_to_string(rel.members,',','') LIKE '%w%,inner,w%,outer%';");
			s.execute(mySql.toString());

			// Récupération des trous manquant
			mySql = new StringBuilder();
			mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_HOLES + " ");
			mySql.append("(" + SQLHelper.CUSTOM_TABLE_HOLES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_HOLES_NODES + ", "
					+ SQLHelper.CUSTOM_TABLE_HOLES_ID_STRUCTURE + ") ");
			mySql.append("SELECT w.id, w.nodes, CASE WHEN r.members[2] = 'outer' THEN CAST(SUBSTRING(r.members[1], 2) AS BIGINT) ELSE CAST(SUBSTRING(r.members[3], 2) AS BIGINT) END ");
			mySql.append("FROM planet_osm_polygon p ");
			mySql.append("INNER JOIN planet_osm_rels r ON ABS(p.osm_id) = r.id ");
			mySql.append("INNER JOIN planet_osm_ways w ON ((r.members[2] = 'inner' AND w.id = CAST(SUBSTRING(r.members[1], 2) AS BIGINT)) OR (r.members[4] = 'inner' AND w.id = CAST(SUBSTRING(r.members[3], 2) AS BIGINT))) ");
			mySql.append("WHERE p.osm_id < 0 ");
			s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création Holes");
		return result;
	}

	private static Boolean addStructuresNeighbors() {
		Boolean result = true;
		System.out.println("Début ajout voisins buildings");
		try {
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s;

			// Récupération des buildings
			Map<Long, Building> buildings = new HashMap<Long, Building>();

			s = conn.createStatement();
			ResultSet res = s.executeQuery("SELECT * FROM "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES + " WHERE "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE + " = '"
					+ Structure.BUILDING + "'");
			while (res.next()) {
				Building tmp = new Building(
						res.getInt(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID),
						res.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME),
						null,
						res.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM),
						null);
				// Holes will be set in the getAllHoles method
				buildings.put(tmp.getId(), tmp);
			}

			for (Long key : buildings.keySet()) {
				Building b = buildings.get(key);
				s = conn.createStatement();
				ArrayList<Long> lstNeighbors = new ArrayList<Long>();
				res = s.executeQuery("SELECT r.nodes, (ST_DumpPoints(r.geom)).path[1], ST_DISTANCE((ST_DumpPoints(r.geom)).geom, (SELECT s.geom FROM sig1337_structures s WHERE s.id ="
						+ b.getId()
						+ ")) AS DIST FROM "
						+ SQLHelper.CUSTOM_TABLE_ROADS + " r ORDER BY DIST ");
				while (res.next()) {
					double dist = res.getDouble(3);
					if (dist < 80) {
						long[] idNodes = SQLHelper
								.getLongArray(res.getArray(1));
						int pos = res.getInt(2);
						if (idNodes != null && idNodes.length >= pos && pos > 0) {
							lstNeighbors.add(idNodes[pos - 1]);
						}
					} else {
						break;
					}
				}

				if (lstNeighbors.size() > 0) {
					s = conn.createStatement();
					String sNeighbors = "";
					for (int i = 0; i < lstNeighbors.size(); i++) {
						if (i == 0) {
							sNeighbors += lstNeighbors.get(i).toString();
						} else {
							sNeighbors += "," + lstNeighbors.get(i).toString();
						}
					}
					s.executeUpdate("UPDATE "
							+ SQLHelper.CUSTOM_TABLE_STRUCTURES + " SET "
							+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NEIGHBORS
							+ " = '{" + sNeighbors + "}' WHERE "
							+ SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + " = "
							+ b.getId());
				}
			}
		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin ajout voisins buildings");
		return result;
	}
}
