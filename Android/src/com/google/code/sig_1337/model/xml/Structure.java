package com.google.code.sig_1337.model.xml;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Structure implements IStructure {

	/**
	 * Its name.
	 */
	protected final String name;
	/**
	 * Triangles.
	 */
	protected final List<ITriangles> triangles;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            its name.
	 * @param triangles
	 *            triangles.
	 */
	public Structure(String name) {
		super();
		this.name = name;
		this.triangles = new CopyOnWriteArrayList<ITriangles>();
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