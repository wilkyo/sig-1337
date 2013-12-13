package com.google.code.sig_1337.model.xml.structure;

import java.nio.FloatBuffer;

public interface IStructures<T extends IStructure> extends Iterable<T> {

	/**
	 * Add the given structure.
	 * 
	 * @param structure
	 *            structure to add.
	 */
	public void add(T structure);

	/**
	 * Clear the list.
	 */
	public void clear();

	/**
	 * Indicate if the list is empty.
	 * 
	 * @return if the list is empty.
	 */
	public boolean isEmpty();

	/**
	 * Called when all the structures have been loaded.
	 */
	public void done();

	/**
	 * Indicates if the structures have been loaded.
	 * 
	 * @return if the structures have been loaded.
	 */
	public boolean isLoaded();

	/**
	 * Get the vertex buffer.
	 * 
	 * @return the vertex buffer.
	 */
	public FloatBuffer getVertexBuffer();

	/**
	 * Get the color buffer.
	 * 
	 * @return the color buffer.
	 */
	public FloatBuffer getColorBuffer();

	/**
	 * Get the number of index in the buffer.
	 * 
	 * @return the number of index in the buffer.
	 */
	public int getIndexCount();

}
