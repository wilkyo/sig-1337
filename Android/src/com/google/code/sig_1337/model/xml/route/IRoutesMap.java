package com.google.code.sig_1337.model.xml.route;

import java.util.Map.Entry;

public interface IRoutesMap extends Iterable<Entry<RouteType, IRoutes>> {

	/**
	 * Get the routes of the given type.
	 * 
	 * @param type
	 *            type of the routes to get.
	 * @return corresponding routes.
	 */
	public IRoutes get(RouteType type);

	/**
	 * Clear the list.
	 */
	public void clear();

	/**
	 * Called when all the routes have been loaded.
	 */
	public void done();

	/**
	 * Indicates if the routes have been loaded.
	 * 
	 * @return if the routes have been loaded.
	 */
	public boolean isLoaded();

}
