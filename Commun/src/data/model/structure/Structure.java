package data.model.structure;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Holes of the Structure.
	 */
	private List<Hole> holes;

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
		if (name == null)
			this.name = "";
		this.nodes = nodes;
		this.holes = new ArrayList<Hole>();
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

	/**
	 * @return the holes
	 */
	public List<Hole> getHoles() {
		return holes;
	}

	/**
	 * Adds a Hole to the Building.
	 * 
	 * @param hole
	 *            Hole to add.
	 */
	public void addHole(Hole hole) {
		this.holes.add(hole);
	}

	/**
	 * @return the geom
	 */
	public String getGeom() {
		return geom;
	}
}
