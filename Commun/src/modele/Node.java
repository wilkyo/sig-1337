package modele;

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

	@Override
	public String toString() {
		return "Node [" + id + "]:(" + latitude + ", " + longitude + ")";
	}
}
