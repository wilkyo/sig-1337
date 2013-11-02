package modele;

public class Node {

	private int id;
	private int latitude;
	private int longitude;

	public Node(int id, int latitude, int longitude) {
		setId(id);
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	@Override
	public String toString() {
		return "Node [" + id + "]:(" + latitude + ", " + longitude + ")";
	}
}
