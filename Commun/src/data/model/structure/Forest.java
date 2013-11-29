package data.model.structure;

import data.model.Node;

/**
 * Represents a Forest.
 */
public class Forest extends Structure {

	/**
	 * Initializes the Forest.
	 * 
	 * @param id
	 *            Id of the Forest.
	 * @param nodes
	 *            Nodes forming the Forest.
	 */
	public Forest(long id, Node[] nodes) {
		super(id, nodes);
	}

}
