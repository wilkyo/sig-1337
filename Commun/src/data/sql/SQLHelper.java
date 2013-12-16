package data.sql;

import java.sql.Array;
import java.util.Map;

import data.model.Node;

public class SQLHelper {

	/**
	 * Constants for the Database Connection.
	 */
	public static final String SQL_DRIVER = "org.postgresql.Driver";
	public static final String SERVER_URL = "jdbc:postgresql://localhost:5432/";
	public static final String DB_SCHEMA = "public";
	public static final String DB_NAME = "sig_1337";
	public static final String OSM_SRID = "900913";
	public static final String REAL_SRID = "4326";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	public static final String OWNER = "postgres";

	/**
	 * Constants for the tables' name.
	 */
	public static final String TABLE_GEOMETRY_COLUMNS = "geometry_columns";
	public static final String TABLE_SPATIAL_REF_SYS = "spatial_ref_sys";
	public static final String TABLE_LINES = "planet_osm_line";
	public static final String TABLE_NODES = "planet_osm_nodes";
	public static final String TABLE_POINTS = "planet_osm_point";
	public static final String TABLE_POLYGONS = "planet_osm_polygon";
	public static final String TABLE_RELS = "planet_osm_rels";
	public static final String TABLE_ROADS = "planet_osm_roads";
	public static final String TABLE_WAYS = "planet_osm_ways";
	public static final String TABLE_VERTICES = "osm_2po_4pgr_noded_vertices_pgr";
	public static final String TABLE_EDGES = "osm_2po_4pgr_noded";
	
	/**
	 * Constants for our geometries.
	 */
	public static final String GEOMETRY_POINT = "POINT";
	public static final String GEOMETRY_POINT_DIMENSIONS = "2";
	public static final String GEOMETRY_LINE = "LINESTRING";
	public static final String GEOMETRY_LINE_DIMENSIONS = "2";
	public static final String GEOMETRY_POLYGON = "GEOMETRY";
	public static final String GEOMETRY_POLYGON_DIMENSIONS = "2";

	/**
	 * Constants for our custom nodes table.
	 */
	public static final String CUSTOM_TABLE_NODES = "sig1337_nodes";
	public static final String CUSTOM_TABLE_NODES_ID = "id";
	public static final String CUSTOM_TABLE_NODES_LAT = "lat";
	public static final String CUSTOM_TABLE_NODES_LON = "lon";
	public static final String CUSTOM_TABLE_NODES_GEOM = "geom";

	/**
	 * Constants for our custom roads table.
	 */
	public static final String CUSTOM_TABLE_ROADS = "sig1337_roads";
	public static final String CUSTOM_TABLE_ROADS_ID = "id";
	public static final String CUSTOM_TABLE_ROADS_NODES = "nodes";
	public static final String CUSTOM_TABLE_ROADS_NAME = "name";
	public static final String CUSTOM_TABLE_ROADS_TYPE = "type";
	public static final String CUSTOM_TABLE_ROADS_GEOM = "geom";

	/**
	 * Constants for our custom structures table.
	 */
	public static final String CUSTOM_TABLE_STRUCTURES = "sig1337_structures";
	public static final String CUSTOM_TABLE_STRUCTURES_ID = "id";
	public static final String CUSTOM_TABLE_STRUCTURES_NODES = "nodes";
	public static final String CUSTOM_TABLE_STRUCTURES_NAME = "name";
	public static final String CUSTOM_TABLE_STRUCTURES_GEOM = "geom";
	public static final String CUSTOM_TABLE_STRUCTURES_TYPE = "type";
	public static final String CUSTOM_TABLE_STRUCTURES_NEIGHBORS = "neighbors";

	/**
	 * Constants for our custom holes table.
	 */
	public static final String CUSTOM_TABLE_HOLES = "sig1337_holes";
	public static final String CUSTOM_TABLE_HOLES_ID = "id";
	public static final String CUSTOM_TABLE_HOLES_NODES = "nodes";
	public static final String CUSTOM_TABLE_HOLES_ID_STRUCTURE = "id_structure";

	/**
	 * Constants for our custom graph table.
	 */
	public static final String CUSTOM_GRAPH_SOURCE = "get_source";
	public static final String CUSTOM_GRAPH_TARGET = "get_target";
	public static final String CUSTOM_GRAPH_POINT_X = "point_x";
	public static final String CUSTOM_GRAPH_POINT_Y = "point_y";
	public static final String CUSTOM_GRAPH_VOISIN_X = "voisin_x";
	public static final String CUSTOM_GRAPH_VOISIN_Y = "voisin_y";

	/**
	 * Constants for our graph vertices table.
	 */
	public static final String VERTICES_GEOM = "the_geom";
	public static final String VERTICES_ID = "id";
	
	/**
	 * Constants for our graph edges table.
	 */
	public static final String EDGES_GEOM = "geom_way";
	public static final String EDGES_ID = "id";
	public static final String EDGES_SOURCE = "source";
	public static final String EDGES_TARGET = "target";
	
	/**
	 * Take an SQL Array and returns its integer Array value.
	 * 
	 * @param array
	 *            The Array.
	 * @return The Integer array.
	 */
	public static int[] getArray(Array array) {
		if (array != null) {
			String[] tmp = array.toString().replace("{", "").replace("}", "")
					.split(",");
			int[] res = new int[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				res[i] = Integer.parseInt(tmp[i]);
			}
			return res;
		} else
			return new int[0];
	}

	public static long[] getLongArray(Array array) {
		if (array != null) {
			String[] tmp = array.toString().replace("{", "").replace("}", "")
					.split(",");
			long[] res = new long[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				res[i] = Long.parseLong(tmp[i]);
			}
			return res;
		} else
			return new long[0];
	}

	/**
	 * Take an SQL Array and returns its Node Array value.
	 * 
	 * @param array
	 *            The Array.
	 * @param nodes
	 *            The Map&lt;Integer, Nodes&gt; of the nodes.
	 * @return The Node array.
	 */
	public static Node[] getArray(Array array, Map<Long, Node> nodes) {
		if (array != null) {
			String[] tmp = array.toString().replace("{", "").replace("}", "")
					.split(",");
			Node[] res = new Node[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				res[i] = nodes.get(Long.parseLong(tmp[i]));
			}
			return res;
		} else
			return new Node[0];
	}
}
