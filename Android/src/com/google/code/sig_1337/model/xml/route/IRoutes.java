package com.google.code.sig_1337.model.xml.route;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Interface for a list of routes.
 */
public interface IRoutes extends Iterable<IRoute> {

	/**
	 * Add the given route.
	 * 
	 * @param route
	 *            route to add.
	 */
	public void add(IRoute route);

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
	 * Called when all the routes have been loaded.
	 */
	public void done();

	/**
	 * Indicates if the routes have been loaded.
	 * 
	 * @return if the routes have been loaded.
	 */
	public boolean isLoaded();

	/**
	 * Get the vertex buffer.
	 * 
	 * @return the vertex buffer.
	 */
	public FloatBuffer getFillVertexBuffer();

	/**
	 * Get the vertex buffer.
	 * 
	 * @return the vertex buffer.
	 */
	public FloatBuffer getStrokeVertexBuffer();

	/**
	 * Get the index buffer.
	 * 
	 * @return the index buffer.
	 */
	public ShortBuffer getIndexBuffer();

	/**
	 * Get the number of index in the buffer.
	 * 
	 * @return the number of index in the buffer.
	 */
	public int getIndexCount();

}
