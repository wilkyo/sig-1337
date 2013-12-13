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
	protected final String name;
	/**
	 * Triangles.
	 */
	protected final List<ITriangles> triangles;

	protected final IVoisins voisins;
	
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
		this.voisins = new Voisins();
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