package com.google.code.sig_1337.model.xml;

import java.util.List;

/**
 * Building.
 */
public class Building implements IBuilding {

	/**
	 * Its name.
	 */
	private final String name;

	/**
	 * Triangles.
	 */
	private final List<ITriangles> triangles;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            its name.
	 * @param triangles
	 *            triangles.
	 */
	public Building(String name, List<ITriangles> triangles) {
		super();
		this.name = name;
		this.triangles = triangles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ITriangles> getTriangles() {
		return triangles;
	}

}
