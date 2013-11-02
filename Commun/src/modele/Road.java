package modele;

public class Road {

	public static final int UNCLASSIFIED = 0;
	public static final int PATH = 1;

	private int id;
	private Node[] nodes;
	private String name;
	private int type;

	public Road(int id, String name, Node[] nodes, int type) {
		setId(id);
		setName(name);
		setNodes(nodes);
		setType(type);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
