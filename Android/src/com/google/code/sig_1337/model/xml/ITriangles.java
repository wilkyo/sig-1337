package com.google.code.sig_1337.model.xml;

import java.util.List;

/**
 * Interface for a list of triangles.
 */
public interface ITriangles extends List<ITriangle> {

	/**
	 * Get the type.
	 * 
	 * @return The type.
	 */
	public TrianglesType getType();

}
