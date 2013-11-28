package data.model;

import base.Point;
import base.Polygone;

public class Node {

	private long id;
	private double latitude;
	private double longitude;

	public Node(long id, double latitude, double longitude) {
		setId(id);
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Point toPoint() {
		return new Point( getLongitude(), getLatitude());
	}

	public static Point[] toPointsArray(Node[] nodes) {
		Point[] points = new Point[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			points[i] = nodes[i].toPoint();
		}
		return points;
	}

	public static Polygone toPolygon(Node[] nodes) {
		return new Polygone(Node.toPointsArray(nodes));
	}

	@Override
	public String toString() {
		return "Node [" + id + "]:(" + latitude + ", " + longitude + ")";
	}
}
