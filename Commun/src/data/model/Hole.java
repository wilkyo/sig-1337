package data.model;

public class Hole {

	private long id;
	private Node[] nodes;
	private long idBuilding;

	public Hole(long id, long idBuilding, Node[] nodes) {
		setId(id);
		setIdBuilding(idBuilding);
		setNodes(nodes);
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

	public long getIdBuilding() {
		return idBuilding;
	}

	public void setIdBuilding(long idBuilding) {
		this.idBuilding = idBuilding;
	}
}
