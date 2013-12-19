package com.google.code.sig_1337.model.graph;


public class DijkstraVertex extends Vertex implements
		Comparable<DijkstraVertex> {

	public boolean visited = false;
	public int distance = Integer.MAX_VALUE;
	public DijkstraVertex previous = null;

	public DijkstraVertex(Node node) {
		super(node);
	}

	public DijkstraVertex(Vertex v) {
		super(v.node);
		neighbors = v.neighbors;
	}

	@Override
	public int compareTo(DijkstraVertex another) {
		return distance - another.distance;
	}

}
