package com.google.code.sig_1337.model.xml;

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
	 * Get the starting point.
	 * 
	 * @return the starting point.
	 */
	public IPoint getFrom();

	/**
	 * Get the end point.
	 * 
	 * @return the end point.
	 */
	public IPoint getTo();

}
