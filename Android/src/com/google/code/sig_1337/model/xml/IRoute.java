package com.google.code.sig_1337.model.xml;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Interface for routes.
 */
public interface IRoute {

	/**
	 * Get its type.
	 * 
	 * @return its type.
	 */
	public RouteType getType();

	/**
	 * Get the starting point.
	 * 
	 * @return the starting point.
	 */
	public IPoint getFrom();

	/**
	 * Get the end point.
	 * 
	 * @return the end point.
	 */
	public IPoint getTo();

	/**
	 * Get the vertex buffer.
	 * 
	 * @return the vertex buffer.
	 */
	public FloatBuffer getVertexBuffer();

	/**
	 * Get the index buffer.
	 * 
	 * @return the index buffer.
	 */
	public ShortBuffer getIndexBuffer();

}
