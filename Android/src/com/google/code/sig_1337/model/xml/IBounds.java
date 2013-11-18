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
	 * Get the minimal longitude.
	 * 
	 * @return the minimal longitude;
	 */
	public double getMinLon();

	/**
	 * Get the maximal latitude.
	 * 
	 * @return the maximal latitude;
	 */
	public double getMaxLat();

	/**
	 * Get the maximal longitude.
	 * 
	 * @return the maximal longitude;
	 */
	public double getMaxLon();

}
