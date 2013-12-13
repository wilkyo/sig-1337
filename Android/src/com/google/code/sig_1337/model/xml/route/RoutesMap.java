package com.google.code.sig_1337.model.xml.route;

import java.util.HashMap;
import java.util.Iterator;

public class RoutesMap extends HashMap<RouteType, IRoutes> implements
		IRoutesMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * If the routes have been loaded.
	 */
	private boolean loaded;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoutes get(RouteType type) {
		IRoutes r = super.get(type);
		if (r == null) {
			r = new Routes();
			put(type, r);
		}
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		for (IRoutes r : values()) {
			r.clear();
		}
		super.clear();
		loaded = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		for (IRoutes r : values()) {
			r.done();
		}
		loaded = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry<RouteType, IRoutes>> iterator() {
		return entrySet().iterator();
	}

}
