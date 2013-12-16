package com.google.code.sig_1337.model.xml.structure;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.code.sig_1337.model.xml.ITriangles;
import com.google.code.sig_1337.model.xml.IVoisins;
import com.google.code.sig_1337.model.xml.Voisins;

public abstract class Structure implements IStructure {

	/**
	 * Its name.
	 */
	private final String name;

	/**
	 * Triangles.
	 */
	private final List<ITriangles> triangles;

	private final long id;

	protected final IVoisins voisins;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            its name.
	 * @param triangles
	 *            triangles.
	 */
	public Structure(String name, long id) {
		super();
		this.name = name;
		this.triangles = new CopyOnWriteArrayList<ITriangles>();
		this.voisins = new Voisins();
		this.id = id;
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

	@Override
	public long getId() {
		return id;
	}
}