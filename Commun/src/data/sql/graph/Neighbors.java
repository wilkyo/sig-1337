package data.sql.graph;

import java.util.Arrays;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.model.Node;

public class Neighbors extends LinkedList<Node> {

	private static final long serialVersionUID = 1L;

	public Neighbors() {
		super();
	}

	@Override
	public String toString() {
		return Arrays.toString(this.toArray(new Node[0]));
	}

	@SuppressWarnings("unchecked")
	public JSONArray toJSON() {
		JSONArray res = new JSONArray();
		for (Node n : this) {
			JSONObject vertex = new JSONObject();
			vertex.put("id", n.getId());
			vertex.put("latitude", n.getLatitude());
			vertex.put("longitude", n.getLongitude());
			res.add(vertex);
		}
		return res;
	}

}
