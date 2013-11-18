package com.google.code.sig_1337.model.xml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Triangle.
 */
public class Triangle implements ITriangle {

	/**
	 * Coordinates per vertex.
	 */
	public static final int COORDS_PER_VERTEX = 3;

	/**
	 * Number of vertex.
	 */
	public static final int VERTEX_COUNT = 3;

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
	 * Vertex buffer.
	 */
	private final FloatBuffer vertexBuffer;

	/**
	 * Index buffer.
	 */
	private final ShortBuffer indexBuffer;

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
		// Create the vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(36);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(new float[] { //
						(float) p1.getRelativeLongitude(),
						(float) p1.getRelativeLatitude(),
						0, // P1.
						(float) p2.getRelativeLongitude(),
						(float) p2.getRelativeLatitude(),
						0, // P2.
						(float) p3.getRelativeLongitude(),
						(float) p3.getRelativeLatitude(), 0 // P3
				});
		vertexBuffer.position(0);
		// Create the index buffer.
		bb = ByteBuffer.allocateDirect(6);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(new short[] { 0, 1, 2 });
		indexBuffer.position(0);
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
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShortBuffer getIndexBuffer() {
		return indexBuffer;
	}

}
