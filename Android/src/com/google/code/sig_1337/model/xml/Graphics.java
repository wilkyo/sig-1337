package com.google.code.sig_1337.model.xml;

/**
 * Graphics.
 */
public class Graphics implements IGraphics {

	/**
	 * Routes.
	 */
	private IRoutes routes;

	/**
	 * Buildings.
	 */
	private IBuildings buildings;

	/**
	 * Initializing constructor.
	 * 
	 * @param routes
	 *            routes.
	 * @param buildings
	 *            buildings.
	 */
	public Graphics(IRoutes routes, IBuildings buildings) {
		super();
		this.routes = routes;
		this.buildings = buildings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoutes getRoutes() {
		return routes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBuildings getBuildings() {
		return buildings;
	}

}
