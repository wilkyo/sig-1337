package com.google.code.sig_1337.model.xml;

/**
 * Bounds.
 */
public class Bounds implements IBounds {

	/**
	 * Minimal latitude.
	 */
	private double minLat;

	/**
	 * Minimal longitude.
	 */
	private double minLon;

	/**
	 * Maximal latitude.
	 */
	private double maxLat;

	/**
	 * Maximal longitude.
	 */
	private double maxLon;

	/**
	 * Initializing constructor.
	 * 
	 * @param minLat
	 *            minimal latitude.
	 * @param minLon
	 *            minimal longitude.
	 * @param maxLat
	 *            maximal latitude.
	 * @param maxLon
	 *            maximal longitude.
	 */
	public Bounds(double minLat, double minLon, double maxLat, double maxLon) {
		super();
		this.minLat = minLat;
		this.minLon = minLon;
		this.maxLat = maxLat;
		this.maxLon = maxLon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMinLat() {
		return minLat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMinLon() {
		return minLon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxLat() {
		return maxLat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxLon() {
		return maxLon;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}

}
