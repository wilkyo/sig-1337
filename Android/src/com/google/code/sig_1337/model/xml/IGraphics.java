package com.google.code.sig_1337.model.xml;

/**
 * Interface for a list of elements to display.
 */
public interface IGraphics {

	/**
	 * Get the buildings.
	 * 
	 * @return the buildings.
	 */
	public IStructures<IBuilding> getBuildings();

	/**
	 * Get the forests.
	 * 
	 * @return the forests.
	 */
	public IStructures<IForet> getForets();

	/**
	 * Get the bassins.
	 * 
	 * @return the bassins.
	 */
	public IStructures<IBassin> getBassins();

	/**
	 * Get the routes.
	 * 
	 * @return the routes.
	 */
	public IRoutes getRoutes();

}
