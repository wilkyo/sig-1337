package com.google.code.sig_1337.model.xml;

import com.google.code.sig_1337.model.xml.route.IRoutesMap;
import com.google.code.sig_1337.model.xml.route.RoutesMap;
import com.google.code.sig_1337.model.xml.structure.IBassin;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IForet;
import com.google.code.sig_1337.model.xml.structure.IStructure;
import com.google.code.sig_1337.model.xml.structure.IStructures;
import com.google.code.sig_1337.model.xml.structure.StructureType;
import com.google.code.sig_1337.model.xml.structure.Structures;

/**
 * Graphics.
 */
public class Graphics implements IGraphics {

	/**
	 * Routes.
	 */
	private IRoutesMap routes;

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
		this.routes = new RoutesMap();
		this.buildings = new Structures<IBuilding>(StructureType.Building);
		this.forets = new Structures<IForet>(StructureType.Foret);
		this.bassins = new Structures<IBassin>(StructureType.Bassin);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoutesMap getRoutes() {
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
	public IStructure getStructure(long id) {
		if (id == -1)
			return null;
		IStructure s = buildings.get(id);
		if (s != null)
			return s;
		s = forets.get(id);
		if (s != null)
			return s;
		s = bassins.get(id);
		if (s != null)
			return s;
		return null;
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
