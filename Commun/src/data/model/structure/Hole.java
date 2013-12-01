package data.model.structure;

import data.model.Node;

/**
 * Represents the Holes of a Building.
 */
public class Hole extends Structure {

	/**
	 * Id of the Building the Hole is part of.
	 */
	private long idBuilding;

	/**
	 * Initializes the Hole.
	 * 
	 * @param id
	 *            Id of the Hole.
	 * @param idBuilding
	 *            Id of the Building of the Hole.
	 * @param nodes
	 *            Nodes forming the Hole.
	 */
	public Hole(long id, long idBuilding, Node[] nodes) {
		super(id, null, nodes, null);
		this.idBuilding = idBuilding;
	}

	/**
	 * @return the idBuilding
	 */
	public long getIdBuilding() {
		return idBuilding;
	}

}
