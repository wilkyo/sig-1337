package data.model.structure;

import data.model.Node;

/**
 * Class for the structures made of Nodes.<br />
 * Basically, they are polygons.
 */
public abstract class Structure {

	public static final String BUILDING = "building";
	public static final String FOREST = "forest";
	public static final String BASIN = "basin";

	/**
	 * Structure's id.
	 */
	private long id;
	/**
	 * Name of the Structure.
	 */
	private String name;
	/**
	 * Nodes forming the polygon.
	 */
	private Node[] nodes;

	private String geom;

	/**
	 * Initializes the Structure.
	 * 
	 * @param id
	 *            Id of the Structure.
	 * @param nodes
	 *            Nodes forming the Structure.
	 */
	public Structure(long id, String name, Node[] nodes, String geom) {
		this.id = id;
		this.name = name;
		this.nodes = nodes;
		this.geom = geom;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the nodes
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}
}
