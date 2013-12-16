package data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import data.model.structure.Structure;

public class LocationHelper {

	public static String getNearestBuilding(double latitude, double longitude,
			boolean nullable) {
		Connection connection;
		String res = "{}";
		StringBuilder query = new StringBuilder();
		try {
			Class.forName(SQLHelper.SQL_DRIVER);
			connection = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);
			/*
			 * SELECT name, ST_AsGeoJSON(ST_Transform(geom, 4326)) AS geom FROM
			 * sig1337_structures WHERE type = 'building' AND
			 * ST_Contains(ST_Transform(geom, 4326),
			 * ST_GeomFromText('POINT(1.92497 47.8443678)', 4326)) = 't';
			 */
			Statement s = connection.createStatement();
			query.append("SELECT ");
			// id, name
			query.append(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + ", "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME);
			// geom
			query.append(", ST_AsGeoJSON(ST_Transform("
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM);
			// distance
			query.append(", " + SQLHelper.REAL_SRID + ")) AS "
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM
					+ ", ST_Distance(ST_Transform("
					+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ", "
					+ SQLHelper.REAL_SRID + "), ST_GeomFromText('"
					+ SQLHelper.GEOMETRY_POINT + "(" + longitude + " "
					+ latitude + ")', " + SQLHelper.REAL_SRID
					+ ")) AS distance");
			// from
			query.append(" FROM " + SQLHelper.CUSTOM_TABLE_STRUCTURES);
			// where type
			query.append(" WHERE " + SQLHelper.CUSTOM_TABLE_STRUCTURES_TYPE
					+ " = '" + Structure.BUILDING + "'");
			// where not null
			if (nullable)
				query.append(" AND name IS NOT NULL");
			// where distance
			query.append(" ORDER BY distance ASC LIMIT 1;");

			ResultSet result = s.executeQuery(query.toString());
			System.out.println(query);
			if (result.next()) {
				res = "{"
						+ "\""
						+ SQLHelper.CUSTOM_TABLE_STRUCTURES_ID
						+ "\":\""
						+ result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_ID)
						+ "\",\""
						+ SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME
						+ "\":\""
						+ result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_NAME)
						+ "\",\""
						+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM
						+ "\":"
						+ result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM)
						+ "}";
			}
			s.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("Problem executing the query :\n"
					+ query.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getDirection(long idbatsrc, long idbatdest) {
		Connection connection;
		String res = "{}";
		StringBuilder query = new StringBuilder();
		try {
			long idsrc, iddest;
			Class.forName(SQLHelper.SQL_DRIVER);
			connection = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);
			Statement s = connection.createStatement();
			idsrc = getPlusProcheSommet(idbatsrc, s);
			iddest = getPlusProcheSommet(idbatdest, s);
			if (idsrc != -1 && iddest != -1) {
				query.append("SELECT ST_AsGeoJSON(geom_way) as chemin FROM " + SQLHelper.TABLE_EDGES + ", pgr_astar('SELECT CAST("
						+ SQLHelper.TABLE_EDGES
						+ "."
						+ SQLHelper.EDGES_ID
						+ " as int4), CAST("
						+ SQLHelper.TABLE_EDGES
						+ "."
						+ SQLHelper.EDGES_SOURCE
						+ " as int4), CAST("
						+ SQLHelper.TABLE_EDGES
						+ "."
						+ SQLHelper.EDGES_TARGET
						+ " as int4),CAST(ST_Length("
						+ SQLHelper.TABLE_EDGES
						+ "."
						+ SQLHelper.EDGES_GEOM
						+ ") as float8) as cost, ST_X(source.the_geom) as x1, ST_Y(source.the_geom) as y1,ST_X(target.the_geom) as x2, ST_Y(target.the_geom) as y2 FROM "
						+ SQLHelper.TABLE_EDGES + ","
						+ SQLHelper.TABLE_VERTICES + " as source,"
						+ SQLHelper.TABLE_VERTICES + " as target WHERE "
						+ SQLHelper.TABLE_EDGES + "." + SQLHelper.EDGES_SOURCE
						+ " = source.id and " + SQLHelper.TABLE_EDGES + "."
						+ SQLHelper.EDGES_TARGET + " = target.id'," + idsrc +"," + iddest + ",false,false) as chemin WHERE " + SQLHelper.TABLE_EDGES + "." + SQLHelper.EDGES_ID + " = id2");
				ResultSet result = s.executeQuery(query.toString());
				res = "{";
				while(result.next()) {
					res += result.getString("chemin");
				}
				res += "}";
				System.out.println(res);
			}
			s.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("Problem executing the query :\n"
					+ query.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

	private static long getPlusProcheSommet(long idBatiment, Statement s)
			throws SQLException {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(SQLHelper.TABLE_VERTICES + "." + SQLHelper.VERTICES_ID
				+ " as id FROM ");
		query.append(SQLHelper.CUSTOM_TABLE_STRUCTURES + ","
				+ SQLHelper.TABLE_VERTICES);
		query.append(" WHERE ");
		query.append("ST_Distance(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM
				+ "," + "ST_Transform(" + SQLHelper.VERTICES_GEOM
				+ ", ST_SRID(" + SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM
				+ "))) = (SELECT MIN(ST_Distance("
				+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + ","
				+ "ST_Transform(" + SQLHelper.VERTICES_GEOM + ", ST_SRID("
				+ SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM + "))))" + " FROM "
				+ SQLHelper.CUSTOM_TABLE_STRUCTURES + ","
				+ SQLHelper.TABLE_VERTICES + " WHERE "
				+ SQLHelper.CUSTOM_TABLE_STRUCTURES + "."
				+ SQLHelper.CUSTOM_TABLE_STRUCTURES_ID + " = '" + idBatiment
				+ "');");
		ResultSet result = s.executeQuery(query.toString());
		if (result.next()) {
			return result.getLong("id");
		}
		return -1;
	}
}
