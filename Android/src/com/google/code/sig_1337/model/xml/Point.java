package com.google.code.sig_1337.model.xml;

/**
 * Point.
 */
public class Point implements IPoint {

	/**
	 * Compare the two given points.
	 * 
	 * @param x1
	 *            first point.
	 * @param y1
	 *            first point.
	 * @param x2
	 *            second point.
	 * @param y2
	 *            second point.
	 * @return 1, 0, -1 for the position of (x2, y2) relative to (x1, y1).
	 */
	public static int compare(double x1, double y1, double x2, double y2) {
		if (x1 < x2) {
			return 1;
		} else if (x1 > x2) {
			return -1;
		} else if (y1 < y2) {
			return 1;
		} else if (y1 > y2) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Longitude.
	 */
	private double longitude;

	/**
	 * Latitude.
	 */
	private double latitude;

	/**
	 * Relative longitude.
	 */
	private double relativeLongitude;

	/**
	 * Relative latitude.
	 */
	private double relativeLatitude;

	/**
	 * Initializing constructor.
	 * 
	 * @param longitude
	 *            longitude.
	 * @param latitude
	 *            latitude.
	 * @param relativeLongitude
	 *            relative longitude.
	 * @param relativeLatitude
	 *            relative latitude.
	 */
	public Point(double longitude, double latitude, double relativeLongitude,
			double relativeLatitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.relativeLongitude = relativeLongitude;
		this.relativeLatitude = relativeLatitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLongitude() {
		return longitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLatitude() {
		return latitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRelativeLongitude() {
		return relativeLongitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRelativeLatitude() {
		return relativeLatitude;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		Point p = (Point) o;
		return close(p.getLatitude(), latitude)
				&& close(p.getLongitude(), longitude);
	}

	private final static double EPSILON = 0.000001;
	
	private boolean close(double  x, double y) {
		return (x - y < EPSILON) && (y - x < EPSILON);
	}
	
	@Override
	public String toString() {
		String s = "";
		s += "longitude:" + longitude + " latitude:" + latitude;
		return s;
	}
}
