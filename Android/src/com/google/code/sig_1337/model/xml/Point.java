package com.google.code.sig_1337.model.xml;

/**
 * Point.
 */
public class Point implements IPoint {

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
		if(o == null)
			return false;
		if(this == o)
			return true;
		if(this.getClass() != o.getClass())
			return false;
		Point p = (Point)o;
		return (p.getLatitude() == latitude && p.getLongitude() == longitude);
	}
}
