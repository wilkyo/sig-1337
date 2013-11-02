package modele;

public class Hole {

	private int id;
	private Node[] nodes;
	private int idBuilding;

	public Hole(int id, int idBuilding, Node[] nodes) {
		setId(id);
		setIdBuilding(idBuilding);
		setNodes(nodes);
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

	public int getIdBuilding() {
		return idBuilding;
	}

	public void setIdBuilding(int idBuilding) {
		this.idBuilding = idBuilding;
	}
}
