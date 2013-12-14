package com.google.code.sig_1337.model.xml.route;

import com.google.code.sig_1337.model.xml.IPoint;

public class Route implements IRoute {

	/**
	 * Its type.
	 */
	private final RouteType type;

	/**
	 * Point.
	 */
	private final IPoint[] points;

	/**
	 * Initializing constructor.
	 * 
	 * @param type
	 *            its type.
	 * @param points
	 *            the points.
	 */
	public Route(RouteType type, IPoint[] points) {
		super();
		this.type = type;
		this.points = points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RouteType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint[] getPoints() {
		return points;
	}

}
