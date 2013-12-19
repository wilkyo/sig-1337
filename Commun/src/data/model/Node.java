package data.model;

import geometry.model.Point;
import geometry.model.Polygone;

public class Node {

	private long id;
	private double latitude;
	private double longitude;
	private String geom;

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
		return new Point(getLongitude(), getLatitude());
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
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
		return "N[" + id + "]";
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}
}
