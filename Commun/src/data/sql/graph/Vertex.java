package data.sql.graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.model.Node;

public class Vertex {

	public Node node;
	public Neighbors neighbors;

	public Vertex(Node node) {
		this.node = node;
		this.neighbors = new Neighbors();
	}

	public Vertex(JSONObject obj) {
		JSONObject vertex = (JSONObject) obj.get("vertex");
		this.node = new Node((Long) vertex.get("id"),
				(Double) vertex.get("longitude"),
				(Double) vertex.get("latitude"));
		this.neighbors = new Neighbors();
		for (Object n : (JSONArray) obj.get("neighbors")) {
			vertex = (JSONObject) n;
			this.neighbors.add(new Node((Long) vertex.get("id"),
					(Double) vertex.get("longitude"), (Double) vertex
							.get("latitude")));
		}
	}

	public void addVoisin(Node node) {
		neighbors.add(node);
	}

	@Override
	public String toString() {
		return node.toString() + "~" + neighbors.toString();
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject res = new JSONObject();
		JSONObject vertex = new JSONObject();
		vertex.put("id", node.getId());
		vertex.put("latitude", node.getLatitude());
		vertex.put("longitude", node.getLongitude());
		res.put("vertex", vertex);
		res.put("neighbors", neighbors.toJSON());
		return res;
	}
}
