package com.google.code.sig_1337.model.xml;

import java.util.List;

/**
 * Interface for Sommet.
 *
 */
public interface IVertex {
	/**
	 * Get the longitude.
	 * 
	 * @return the longitude.
	 */
	public double getLongitude();

	/**
	 * Get the latitude.
	 * 
	 * @return the latitude.
	 */
	public double getLatitude();

	/**
	 * Get the longitude, relative to the map bounds.
	 * 
	 * @return the longitude.
	 */
	public double getRelativeLongitude();

	/**
	 * Get the latitude, relative to the map bounds.
	 * 
	 * @return the latitude.
	 */
	public double getRelativeLatitude();
	
	/**
	 * Get the neighborhood
	 * 
	 * @return the neighborhood
	 */
	public List<IPoint> getPoint();
}
