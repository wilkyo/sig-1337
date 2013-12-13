package com.google.code.sig_1337.model.xml;

/**
 * Interface for bounds.
 */
public interface IBounds {

	/**
	 * Get the minimal latitude.
	 * 
	 * @return the minimal latitude;
	 */
	public double getMinLat();

	/**
	 * Set the minimal latitude.
	 * 
	 * @param minLat
	 *            new value.
	 */
	public void setMinLat(double minLat);

	/**
	 * Get the minimal longitude.
	 * 
	 * @return the minimal longitude;
	 */
	public double getMinLon();

	/**
	 * Set the minimal longitude.
	 * 
	 * @param minLon
	 *            new value.
	 */
	public void setMinLon(double minLon);

	/**
	 * Get the maximal latitude.
	 * 
	 * @return the maximal latitude;
	 */
	public double getMaxLat();

	/**
	 * Set the maximal latitude.
	 * 
	 * @param maxLat
	 *            new value.
	 */
	public void setMaxLat(double maxLat);

	/**
	 * Get the maximal longitude.
	 * 
	 * @return the maximal longitude;
	 */
	public double getMaxLon();

	/**
	 * Set the maximal longitude.
	 * 
	 * @param maxLon
	 *            new value.
	 */
	public void setMaxLon(double maxLon);

}
