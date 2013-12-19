package com.google.code.sig_1337.model.graph;

import android.util.Log;

import com.google.code.sig_1337.model.xml.Point;

public class Node extends Point {

	private long id;

	private Node(double longitude, double latitude, double relativeLongitude,
			double relativeLatitude) {
		super(longitude, latitude, relativeLongitude, relativeLatitude);
	}

	public Node(long id, double longitude, double latitude) {
		this(longitude, latitude, 0, 0);
		this.id = id;
	}

	public Node(long id, double longitude, double latitude,
			double relativeLongitude, double relativeLatitude) {
		this(longitude, latitude, relativeLongitude, relativeLatitude);
		this.id = id;
	}

	public long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		Log.v("hash", "node");
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "N(" + id + ")";
	}

}
