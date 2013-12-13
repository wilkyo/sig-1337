package com.google.code.sig_1337.model.xml;

import java.nio.FloatBuffer;

/**
 * Triangle.
 */
public class Triangle implements ITriangle {

	/**
	 * First point.
	 */
	private final IPoint p1;

	/**
	 * Second point.
	 */
	private final IPoint p2;

	/**
	 * Third point.
	 */
	private final IPoint p3;

	/**
	 * Initializing constructor.
	 * 
	 * @param p1
	 *            first point.
	 * @param p2
	 *            second point.
	 * @param p3
	 *            third point.
	 */
	public Triangle(IPoint p1, IPoint p2, IPoint p3) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getP1() {
		return p1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getP2() {
		return p2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getP3() {
		return p3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fill(FloatBuffer vertexBuffer) {
		vertexBuffer.put((float) p1.getRelativeLongitude());
		vertexBuffer.put((float) p1.getRelativeLatitude());
		vertexBuffer.put(0);
		vertexBuffer.put((float) p2.getRelativeLongitude());
		vertexBuffer.put((float) p2.getRelativeLatitude());
		vertexBuffer.put(0);
		vertexBuffer.put((float) p3.getRelativeLongitude());
		vertexBuffer.put((float) p3.getRelativeLatitude());
		vertexBuffer.put(0);
	}

}
