package data.sql.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import data.model.Node;
import data.model.Road;
import data.model.structure.Building;

public class DijkstraGraph extends HashMap<Node, DijkstraVertex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DijkstraGraph(List<Road> roads) {
		super();

		for (Road r : roads) {
			for (int i = 0; i < r.getNodes().length; i++) {
				Node n = r.getNodes()[i];
				DijkstraVertex neighbor = get(n);
				if (neighbor == null) {
					neighbor = new DijkstraVertex(n);
					put(n, neighbor);
				}
				if (i > 0) {
					neighbor.addVoisin(r.getNodes()[i - 1]);
				}
				if (i < r.getNodes().length - 1) {
					neighbor.addVoisin(r.getNodes()[i + 1]);
				}
			}
		}
	}

	public DijkstraGraph(String json) {
		super();
		try {
			JSONObject jsonObject = (JSONObject) JSONValue
					.parseWithException(json);
			JSONArray vertices = (JSONArray) jsonObject.get("graph");
			for (Object o : vertices) {
				DijkstraVertex vertex = new DijkstraVertex(new Vertex(
						(JSONObject) o));
				put(vertex.node, vertex);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private boolean estUnVoisin(Node n, Building b) {
		for (Node v : b.getNeighbors())
			if (n.equals(v))
				return true;
		return false;
	}

	public List<Node> dijkstra(Building src, Building dest) {
		PriorityQueue<DijkstraVertex> aVisiter = new PriorityQueue<DijkstraVertex>();

		for (Node n : src.getNeighbors()) {
			DijkstraVertex source = (DijkstraVertex) get(n);
			aVisiter.add(source);
			source.distance = 0;
		}

		DijkstraVertex actuel = aVisiter.poll();
		int cpt = 0;
		while (!estUnVoisin(actuel.node, dest) && aVisiter.size() > 0) {
			cpt++;
			actuel.visited = true;
			for (Node n : actuel.neighbors) {
				int dist = actuel.distance + 1;
				DijkstraVertex tmp = (DijkstraVertex) get(n);
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
		for (Node n : dest.getNeighbors()) {
			actuel = (DijkstraVertex) get(n);
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

	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONObject res = new JSONObject();
		JSONArray resArray = new JSONArray();
		for (Vertex u : values()) {
			resArray.add(u.toJSON());
		}
		res.put("graph", resArray);
		return res.toJSONString();
	}
}
