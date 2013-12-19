package com.google.code.sig_1337.model.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.structure.IBuilding;

public class DijkstraGraph extends HashMap<Long, DijkstraVertex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DijkstraGraph(String json, IBounds bounds) {
		super();
		try {
			JSONObject jsonObject = (JSONObject) JSONValue
					.parseWithException(json);
			JSONArray vertices = (JSONArray) jsonObject.get("graph");
			for (Object o : vertices) {
				DijkstraVertex vertex = new DijkstraVertex(new Vertex(
						(JSONObject) o, bounds));
				put(vertex.node.getId(), vertex);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public List<Node> dijkstra(IBuilding src, IBuilding dest) {
		PriorityQueue<DijkstraVertex> aVisiter = new PriorityQueue<DijkstraVertex>();

		for (Node n : src.getVoisins()) {
			DijkstraVertex source = (DijkstraVertex) get(n.getId());
			aVisiter.add(source);
			source.distance = 0;
		}

		DijkstraVertex actuel = aVisiter.poll();
		int cpt = 0;
		while (aVisiter.size() > 0) {
			cpt++;
			actuel.visited = true;
			for (Node n : actuel.neighbors) {
				int dist = actuel.distance + 1;
				DijkstraVertex tmp = (DijkstraVertex) get(n.getId());
				if (dist < tmp.distance) {
					tmp.distance = dist;
					tmp.previous = actuel;
					if (!tmp.visited) {
						aVisiter.add(tmp);
					}
				}
			}
			actuel = aVisiter.poll();
		}

		System.out.println("Itérations: " + cpt);
		DijkstraVertex finalNode = null;
		for (Node n : dest.getVoisins()) {
			actuel = (DijkstraVertex) get(n.getId());
			if (finalNode == null)
				finalNode = actuel;
			if (actuel.distance < finalNode.distance)
				finalNode = actuel;
		}
		System.out.println(finalNode.distance);

		LinkedList<Node> res = new LinkedList<Node>();
		while (finalNode != null) {
			res.addFirst(finalNode.node);
			finalNode = finalNode.previous;
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		for (Vertex n : values())
			b.append(n.toString() + "\n");
		return b.toString();
	}
}
