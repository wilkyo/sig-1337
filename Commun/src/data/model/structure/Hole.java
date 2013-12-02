package data.model.structure;

import data.model.Node;

/**
 * Represents the Holes of a Building.
 */
public class Hole extends Structure {

	/**
	 * Id of the structure the Hole is part of.
	 */
	private long idStructure;

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
	public Hole(long id, long idStructure, Node[] nodes) {
		super(id, null, nodes, null);
		this.idStructure = idStructure;
	}

	/**
	 * @return the idBuilding
	 */
	public long getIdStructure() {
		return idStructure;
	}

}
