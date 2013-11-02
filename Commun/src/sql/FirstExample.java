package sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.sql.Array;

public class FirstExample {

	public static void main(String[] args) {

		java.sql.Connection conn;

		try {
			/*
			 * Load the JDBC driver and establish a connection.
			 */
			Class.forName(SQLHelper.SQL_DRIVER);
			conn = DriverManager
					.getConnection(SQLHelper.SERVER_URL + SQLHelper.DB_NAME,
							SQLHelper.USERNAME, SQLHelper.PASSWORD);
			/*
			 * Add the geometry types to the connection. Note that you must cast
			 * the connection to the pgsql-specific connection implementation
			 * before calling the addDataType() method.
			 */
			// ((org.postgresql.Connection) conn).addDataType("geometry",
			// "org.postgis.PGgeometry");
			// ((org.postgresql.Connection) conn).addDataType("box3d",
			// "org.postgis.PGbox3d");
			/*
			 * Create a statement and execute a select query.
			 */
			Statement s = conn.createStatement();
			// ResultSet r = s
			// .executeQuery("select ST_AsText(geom) as geom,id from geomtable");
			ResultSet res = s.executeQuery("SELECT * FROM "
					+ SQLHelper.TABLE_WAYS);
			int cpt = 0;
			while (res.next()) {
				cpt++;
				/*
				 * Retrieve the geometry as an object then cast it to the
				 * geometry type. Print things out.
				 */
				// PGgeometry geom = (PGgeometry) r.getObject(1);
				int id = res.getInt(1);
				java.sql.Array array = res.getArray(2);
				int[] nodes = SQLHelper.getArray(array);

				// int lat = r.getInt(2);
				// int lon = r.getInt(3);
				Array tags = (Array) res.getObject(3);
				// String[] data =
				if (array != null) {
					System.out.println("Row " + id + ":"
							+ Arrays.toString(nodes) + ", " + ": " + tags);
					for (Object i : nodes) {
						System.out.print(i + ":");
					}
					System.out.println();
				}
				// for(int i = 0 ; i < data.length ; i++) {
				// System.out.print(data[i] + " ");
				// }
				// System.out.println(geom.toString());
			}
			System.out.println(cpt + " lines");
			s.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}