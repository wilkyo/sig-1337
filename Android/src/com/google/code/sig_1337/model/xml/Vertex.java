package com.google.code.sig_1337.model.xml;

import java.util.List;

public class Vertex implements IVertex {
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
	 * the neighborhood
	 */
	private List<IPoint> list;

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
	 * @param list
	 * 			  neighborhood
	 */
	public Vertex(double longitude, double latitude, double relativeLongitude,
			double relativeLatitude, List<IPoint> list) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.relativeLongitude = relativeLongitude;
		this.relativeLatitude = relativeLatitude;
		this.list = list;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<IPoint> getPoint() {
		return list;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(o.getClass() == Point.class)
			return new Point(longitude, latitude, relativeLongitude, relativeLatitude).equals(o);
		if(o.getClass() != Vertex.class)
			return false;
		Vertex ver = (Vertex)o;
		return new Point(longitude, latitude, relativeLongitude, relativeLatitude).equals(new Point(ver.longitude, ver.latitude, ver.relativeLongitude, ver.relativeLatitude));
	}
}
