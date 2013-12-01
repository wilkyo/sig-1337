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
	private IStructures<IBuilding> buildings;

	/**
	 * Forets.
	 */
	private IStructures<IForet> forets;

	/**
	 * Bassins.
	 */
	private IStructures<IBassin> bassins;

	/**
	 * Initializing constructor.
	 * 
	 * @param routes
	 *            routes.
	 * @param buildings
	 *            buildings.
	 * @param forets
	 *            forets.
	 * @param bassins
	 *            bassins.
	 */
	public Graphics(IRoutes routes, IStructures<IBuilding> buildings,
			IStructures<IForet> forets, IStructures<IBassin> bassins) {
		super();
		this.routes = routes;
		this.buildings = buildings;
		this.forets = forets;
		this.bassins = bassins;
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
	public IStructures<IBuilding> getBuildings() {
		return buildings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructures<IForet> getForets() {
		return forets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructures<IBassin> getBassins() {
		return bassins;
	}

}
