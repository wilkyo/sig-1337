package data.model.structure;

import java.util.ArrayList;
import java.util.List;

import data.model.Node;

/**
 * Represents a Building.
 */
public class Building extends Structure {

	/**
	 * Name of the Building.
	 */
	private String name;
	/**
	 * Holes of the Building.
	 */
	private List<Hole> holes;

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
	public Building(long id, String name, Node[] nodes) {
		super(id, nodes);
		this.name = name;
		holes = new ArrayList<Hole>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
}
