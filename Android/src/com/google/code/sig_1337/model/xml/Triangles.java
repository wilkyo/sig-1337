package com.google.code.sig_1337.model.xml;

import java.util.ArrayList;

/**
 * Triangles.
 */
public class Triangles extends ArrayList<ITriangle> implements ITriangles {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Type.
	 */
	private TrianglesType type;

	/**
	 * Initializing constructor.
	 * 
	 * @param type
	 *            type.
	 */
	public Triangles(TrianglesType type) {
		super();
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrianglesType getType() {
		return type;
	}

}
