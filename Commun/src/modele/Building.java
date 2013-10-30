package modele;

public class Building {

	private int id;
	private int[] nodes;
	private String name;
	private int[] holes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getNodes() {
		return nodes;
	}

	public void setNodes(int[] nodes) {
		this.nodes = nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getHoles() {
		return holes;
	}

	public void setHoles(int[] holes) {
		this.holes = holes;
	}
}
