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
						+ "\":\""
						+ result.getString(SQLHelper.CUSTOM_TABLE_STRUCTURES_GEOM)
						+ "\"}";
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

	public static String getDirection(double srcLatitude, double srcLongitude,
			double destLatitude, double destLongitude) {
		// TODO Auto-generated method stub
		return "{}";
	}
}
