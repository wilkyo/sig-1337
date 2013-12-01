package data.model.structure;

import data.model.Node;

/**
 * Represents a Building.
 */
public class Building extends Structure {

	/**
	 * Initializes the Building.
	 * 
	 * @param id
	 *            Id of the Building.
	 * @param name
	 *            Name of the Building.
	 * @param nodes
	 *            Nodes forming the Building.
	 */
	public Building(long id, String name, Node[] nodes, String geom) {
		super(id, name, nodes, geom);
	}
}
