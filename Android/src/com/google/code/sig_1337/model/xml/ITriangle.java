package com.google.code.sig_1337.model.xml;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Interface for triangles.
 */
public interface ITriangle {

	/**
	 * Get the first point.
	 * 
	 * @return the first point.
	 */
	public IPoint getP1();

	/**
	 * Get the second point.
	 * 
	 * @return the second point.
	 */
	public IPoint getP2();

	/**
	 * Get the third point.
	 * 
	 * @return the third point.
	 */
	public IPoint getP3();

	/**
	 * Fill the given buffers.
	 * 
	 * @param vertexBuffer
	 *            vertex buffer.
	 */
	public void fill(FloatBuffer vertexBuffer);

}
