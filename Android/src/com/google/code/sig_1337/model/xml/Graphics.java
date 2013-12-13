package com.google.code.sig_1337.model.xml;

import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.Routes;
import com.google.code.sig_1337.model.xml.structure.IBassin;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IForet;
import com.google.code.sig_1337.model.xml.structure.IStructures;
import com.google.code.sig_1337.model.xml.structure.Structures;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		routes.clear();
		buildings.clear();
		forets.clear();
		bassins.clear();
	}

}
