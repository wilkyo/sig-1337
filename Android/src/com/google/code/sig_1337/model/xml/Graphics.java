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
	public Graphics() {
		super();
		this.routes = new Routes();
		this.buildings = new Structures<IBuilding>();
		this.forets = new Structures<IForet>();
		this.bassins = new Structures<IBassin>();
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
