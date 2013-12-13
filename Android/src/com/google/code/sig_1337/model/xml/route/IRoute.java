package com.google.code.sig_1337.model.xml.route;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.google.code.sig_1337.model.xml.IPoint;

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
	 * Get the angle between this route and the origin.
	 * 
	 * @return the angle.
	 */
	public float getAngle();

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

}
