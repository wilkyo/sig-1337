package data.model.structure;

import data.model.Node;

/**
 * Class for the structures made of Nodes.<br />
 * Basically, they are polygons.
 */
public abstract class Structure {

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

	/**
	 * Initializes the Structure.
	 * 
	 * @param id
	 *            Id of the Structure.
	 * @param nodes
	 *            Nodes forming the Structure.
	 */
	public Structure(long id, String name, Node[] nodes) {
		this.id = id;
		this.name = name;
		this.nodes = nodes;
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
}
