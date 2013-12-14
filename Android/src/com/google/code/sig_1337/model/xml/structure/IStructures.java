package com.google.code.sig_1337.model.xml.structure;

import java.nio.FloatBuffer;

public interface IStructures<T extends IStructure> extends Iterable<T> {

	/**
	 * Get the type.
	 * 
	 * @return the type.
	 */
	public StructureType getType();

	/**
	 * Add the given structure.
	 * 
	 * @param structure
	 *            structure to add.
	 */
	public void add(T structure);

	/**
	 * Get the structure with the given name.
	 * 
	 * @param name
	 *            name of the structure.
	 * @return corresponding structure.
	 */
	public T get(String name);

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
	public FloatBuffer getFilledVertexBuffer();

	/**
	 * Get the vertex buffer.
	 * 
	 * @return the vertex buffer.
	 */
	public FloatBuffer getHoleVertexBuffer();

	/**
	 * Get the number of index in the buffer.
	 * 
	 * @return the number of index in the buffer.
	 */
	public int getFilledIndexCount();

	/**
	 * Get the number of index in the buffer.
	 * 
	 * @return the number of index in the buffer.
	 */
	public int getHoleIndexCount();

}
