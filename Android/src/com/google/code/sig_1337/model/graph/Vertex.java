package com.google.code.sig_1337.model.graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.code.sig_1337.model.xml.IBounds;

public class Vertex {

	public Node node;
	public Neighbors neighbors;

	public Vertex(Node node) {
		this.node = node;
		this.neighbors = new Neighbors();
	}

	public Vertex(JSONObject obj, IBounds bounds) {
		JSONObject vertex = (JSONObject) obj.get("vertex");
		double longitude = (Double) vertex.get("longitude");
		double latitude = (Double) vertex.get("latitude");
		this.node = new Node((Long) vertex.get("id"), longitude, latitude,
				longitude - bounds.getMinLon(), latitude - bounds.getMinLat());
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
}
