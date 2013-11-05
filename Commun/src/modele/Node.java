package modele;

import base.Point;
import base.Polyedre;

public class Node {

	private long id;
	private int latitude;
	private int longitude;

	public Node(long id, int latitude, int longitude) {
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

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public Point toPoint() {
		return new Point(getLongitude(), getLatitude());
	}

	public static Point[] toPointsArray(Node[] nodes) {
		Point[] points = new Point[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			points[i] = nodes[i].toPoint();
		}
		return points;
	}

	public static Polyedre toPolygon(Node[] nodes) {
		return new Polyedre(Node.toPointsArray(nodes));
	}

	@Override
	public String toString() {
		return "Node [" + id + "]:(" + latitude + ", " + longitude + ")";
	}
}
