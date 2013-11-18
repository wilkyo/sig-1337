package com.google.code.sig_1337.model.xml;

import java.util.List;

/**
 * Interface for buildings.
 */
public interface IBuilding {

	/**
	 * Get its name.
	 * 
	 * @return its name.
	 */
	public String getName();

	/**
	 * Get the triangles.
	 * 
	 * @return the triangles.
	 */
	public List<ITriangles> getTriangles();

}
