package com.google.code.sig_1337.model.xml.route;

import com.google.code.sig_1337.model.xml.IPoint;

/**
 * Interface for routes.
 */
public interface IRoute {

	/**
	 * Get its type.
	 * 
	 * @return its type.
	 */
	public RouteType getType();

	/**
	 * Get the points.
	 * 
	 * @return the points.
	 */
	public IPoint[] getPoints();

}
