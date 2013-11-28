package data.model;

public class Road {

	// Way To be used for minor roads in the public road network which are not
	// residential and of a lower classification than tertiary.
	public static final int UNCLASSIFIED = 0;
	// Administrative classification in the UK, generally linking larger towns.
	public static final int PRIMARY = 1;
	// The link roads (sliproads/ramps) leading to/from a primary road from/to a
	// primary road or lower class highway.
	public static final int PRIMARY_LINK = 2;
	// Way Administrative classification in the UK, generally linking smaller
	// towns and villages
	public static final int SECONDARY = 3;
	// The link roads (sliproads/ramps) leading to/from a secondary road from/to
	// a secondary road or lower class highway.
	public static final int SECONDARY_LINK = 4;
	// A "C" road in the UK. Generally for use on roads wider than 4 metres
	// (13') in width, and for faster/wider minor roads that aren't A or B
	// roads.
	public static final int TERTIARY = 5;
	// Way The link roads (sliproads/ramps) leading to/from a tertiary road
	// from/to a tertiary road or lower class highway.
	public static final int TERTIARY_LINK = 6;
	// For roads used mainly/exclusively for pedestrians in shopping and some
	// residential areas which may allow access by motorised vehicles only for
	// very limited periods of the day
	public static final int PEDESTRIAN = 7;
	// Way Roads which are primarily lined with housing, but which are of a
	// lowest classification than tertiary and which are not living streets.
	public static final int RESIDENTIAL = 8;
	// For access roads to, or within an industrial estate, camp site, business
	// park, car park etc.
	public static final int SERVICE = 9;
	// Roads for agricultural or forestry uses etc
	public static final int TRACK = 10;
	// A non-specific or shared-use path
	public static final int PATH = 11;
	// For designated footpaths; i.e., mainly/exclusively for pedestrians.
	public static final int FOOTWAY = 12;
	// For flights of steps (stairs) on footways.
	public static final int STEPS = 13;
	// For designated cycleways; i.e., mainly/exclusively for bicycles.
	public static final int CYCLEWAY = 14;

	private int id;
	private Node[] nodes;
	private String name;
	private int type;
	private String geom;

	public Road(int id, String name, Node[] nodes, int type, String geom) {
		this.id = id;
		this.name = name;
		this.nodes = nodes;
		this.type = type;
		this.geom = geom;
	}

	public int getId() {
		return id;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

}
