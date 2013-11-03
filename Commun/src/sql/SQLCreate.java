package sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import modele.Road;

public class SQLCreate {

	public static Boolean createDataBase() {
		Boolean result = false;
		System.out.println("Début création base de données");
		if (dataBaseExists()) {
			// Récupération des nodes
			if (createTableNodes() && createTableBuildings()
					&& createTableRoads() && createTableHoles()) {
				result = true;
			}
		}
		System.out.println("Fin création base de données");
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
					+ SQLHelper.CUSTOM_TABLE_NODES_LON + ") ");
			mySql.append("SELECT id, lat, lon ");
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

	private static Boolean createTableBuildings() {
		Boolean result = true;
		System.out.println("Début création Buildings");
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			java.sql.Connection conn = DriverManager.getConnection(
					SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
					SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = conn.createStatement();

			// Création de la structure de la table
			StringBuilder mySql = new StringBuilder();
			mySql.append("DROP TABLE IF EXISTS "
					+ SQLHelper.CUSTOM_TABLE_BUILDINGS + ";");
			mySql.append("CREATE TABLE " + SQLHelper.CUSTOM_TABLE_BUILDINGS
					+ " (");
			mySql.append(SQLHelper.CUSTOM_TABLE_BUILDINGS_ID
					+ " bigint NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_BUILDINGS_NODES
					+ " bigint[] NOT NULL,");
			mySql.append(SQLHelper.CUSTOM_TABLE_BUILDINGS_NAME
					+ " text NOT NULL,");
			mySql.append("CONSTRAINT " + SQLHelper.CUSTOM_TABLE_BUILDINGS
					+ "_pkey PRIMARY KEY ("
					+ SQLHelper.CUSTOM_TABLE_BUILDINGS_ID + ")");
			mySql.append(")");
			mySql.append("WITH (OIDS=FALSE);");
			mySql.append("ALTER TABLE " + SQLHelper.CUSTOM_TABLE_BUILDINGS
					+ " OWNER TO postgres;");
			s.execute(mySql.toString());

			// Récupération des données
			mySql = new StringBuilder();
			mySql.append("SELECT id, nodes, ");
			// on récupère le nom du tableau "tags"
			mySql.append("(regexp_split_to_array(substring(array_to_string(tags,',','') from 'name,(.*)'), ','))[1] ");
			mySql.append("FROM " + SQLHelper.TABLE_WAYS + " ");
			mySql.append("WHERE array_to_string(tags,',','') LIKE '%building%';");
			ResultSet myResultSet = s.executeQuery(mySql.toString());
			mySql = new StringBuilder();
			while (myResultSet.next()) {
				long id = myResultSet.getLong(1);
				long[] nodes = SQLHelper.getLongArray(myResultSet.getArray(2));
				String name = myResultSet.getString(3);
				mySql.append("INSERT INTO " + SQLHelper.CUSTOM_TABLE_BUILDINGS
						+ " ");
				mySql.append("(" + SQLHelper.CUSTOM_TABLE_BUILDINGS_ID + ", "
						+ SQLHelper.CUSTOM_TABLE_BUILDINGS_NODES + ", "
						+ SQLHelper.CUSTOM_TABLE_BUILDINGS_NAME + ")");
				mySql.append(" VALUES ");
				mySql.append("("
						+ id
						+ ", '"
						+ Arrays.toString(nodes).replace("[", "{")
								.replace("]", "}")
						+ "', "
						+ (name != null ? "'" + name.replace("'", "''") + "' "
								: "''") + "); ");
			}
			if (mySql.length() > 0)
				s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création Buildings");
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
			mySql.append("SELECT id, nodes, ");
			// on récupère le nom du tableau "tags"
			mySql.append("(regexp_split_to_array(substring(array_to_string(tags,',','') from 'name,(.*)'), ','))[1], ");
			mySql.append("(regexp_split_to_array(substring(array_to_string(tags,',','') from 'highway,(.*)'), ','))[1] ");
			mySql.append("FROM " + SQLHelper.TABLE_WAYS + " ");
			mySql.append("WHERE array_to_string(tags,',','') LIKE '%highway%';");
			ResultSet myResultSet = s.executeQuery(mySql.toString());
			mySql = new StringBuilder();
			while (myResultSet.next()) {
				long id = myResultSet.getLong(1);
				long[] nodes = SQLHelper.getLongArray(myResultSet.getArray(2));
				String name = myResultSet.getString(3);
				int type = Road.UNCLASSIFIED;
				String sType = myResultSet.getString(4);
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
						+ SQLHelper.CUSTOM_TABLE_ROADS_TYPE + ")");
				mySql.append(" VALUES ");
				mySql.append("("
						+ id
						+ ", '"
						+ Arrays.toString(nodes).replace("[", "{")
								.replace("]", "}")
						+ "', "
						+ (name != null ? "'" + name.replace("'", "''") + "' "
								: "''") + ", " + type + "); ");
			}
			if (mySql.length() > 0)
				s.execute(mySql.toString());

			s.close();
			conn.close();

		} catch (Exception e) {
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
			mySql.append(SQLHelper.CUSTOM_TABLE_HOLES_ID_BUILDING
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
					+ SQLHelper.CUSTOM_TABLE_HOLES_ID_BUILDING + ") ");
			mySql.append("SELECT rel.id, ways2.nodes, builing.id ");
			mySql.append("FROM " + SQLHelper.TABLE_RELS + " rel ");
			mySql.append("INNER JOIN "
					+ SQLHelper.CUSTOM_TABLE_BUILDINGS
					+ " builing ON builing.Id = CAST(substring((regexp_split_to_array(array_to_string(rel.members,',',''),','))[3] from 2) AS bigint) ");
			mySql.append("INNER JOIN "
					+ SQLHelper.TABLE_WAYS
					+ " ways2 ON ways2.Id = CAST(substring((regexp_split_to_array(array_to_string(rel.members,',',''),','))[1] from 2) AS bigint) ");
			mySql.append("WHERE array_to_string(rel.members,',','') LIKE '%w%,inner,w%,outer%';");
			s.execute(mySql.toString());
			s.close();
			conn.close();

		} catch (Exception e) {
			result = false;
		}
		System.out.println("Fin création Holes");
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		createDataBase();
	}
}
