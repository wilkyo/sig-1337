package modele;

import base.Point;
import base.Polyedre;

public class Node {

	private long id;
	private float latitude;
	private float longitude;

	public Node(long id, float latitude, float longitude) {
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

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
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
