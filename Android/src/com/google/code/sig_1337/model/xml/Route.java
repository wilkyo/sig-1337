package com.google.code.sig_1337.model.xml;

public class Route implements IRoute {

	/**
	 * Its type.
	 */
	private final RouteType type;

	/**
	 * Starting point.
	 */
	private final IPoint from;

	/**
	 * End point.
	 */
	private final IPoint to;

	/**
	 * Initializing constructor.
	 * 
	 * @param type
	 *            its type.
	 * @param from
	 *            starting point.
	 * @param to
	 *            end point.
	 */
	public Route(RouteType type, IPoint from, IPoint to) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
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
	public IPoint getFrom() {
		return from;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getTo() {
		return to;
	}

}
