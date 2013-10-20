package com.google.code.sig_1337.model.xml;

/**
 * Enums for route types.
 */
public enum RouteType {

	/**
	 * Path.
	 */
	Path("chemin"),
	/**
	 * Route.
	 */
	Route("route");

	/**
	 * Parse the given string to the corresponding route type.
	 * 
	 * @param s
	 *            the string to parse.
	 * @return the corresponding route type.
	 */
	public static RouteType parse(String s) {
		for (RouteType t : values()) {
			if (s.equals(t.name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Name of this type.
	 */
	private final String name;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            name of this type.
	 */
	private RouteType(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this type.
	 * 
	 * @return the name of this type.
	 */
	public String getName() {
		return name;
	}

}
