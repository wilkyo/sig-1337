package data.model;

import java.util.ArrayList;
import java.util.List;

public class Building {

	private long id;
	private Node[] nodes;
	private String name;
	private List<Hole> holes;

	public Building(int id, String name, Node[] nodes) {
		setId(id);
		setName(name);
		setNodes(nodes);
		holes = new ArrayList<Hole>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Hole> getHoles() {
		return holes;
	}

	public void setHoles(List<Hole> holes) {
		this.holes = holes;
	}

	public void addHole(Hole hole) {
		this.holes.add(hole);
	}
}
