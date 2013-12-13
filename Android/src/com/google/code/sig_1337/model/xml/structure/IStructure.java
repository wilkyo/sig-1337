package com.google.code.sig_1337.model.xml.structure;

import java.util.List;

import com.google.code.sig_1337.model.xml.ITriangles;

public interface IStructure {
	/**
	 * Get its type.
	 * 
	 * @return its type.
	 */
	public StructureType getType();

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