package data.model.structure;

import data.model.Node;

/**
 * Represents a Basin.
 */
public class Basin extends Structure {

	/**
	 * Initializes the Basin.
	 * 
	 * @param id
	 *            Id of the Basin.
	 * @param nodes
	 *            Nodes forming the Basin.
	 */
	public Basin(long id, Node[] nodes) {
		super(id, nodes);
	}

}
