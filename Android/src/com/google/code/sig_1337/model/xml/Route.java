package com.google.code.sig_1337.model.xml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Route implements IRoute {

	/**
	 * Its type.
	 */
	private final RouteType type;

	/**
	 * Starting point.
	 */
	private final IPoint from;

	/**
	 * End point.
	 */
	private final IPoint to;

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
	 * @param type
	 *            its type.
	 * @param from
	 *            starting point.
	 * @param to
	 *            end point.
	 */
	public Route(RouteType type, IPoint from, IPoint to) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
		// Create the vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(24);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(new float[] { //
				(float) from.getRelativeLongitude(),
						(float) from.getRelativeLatitude(),
						0, // From.
						(float) to.getRelativeLongitude(),
						(float) to.getRelativeLatitude(), 0 // To.
				});
		vertexBuffer.position(0);
		// Create the index buffer.
		bb = ByteBuffer.allocateDirect(4);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(new short[] { 0, 1 });
		indexBuffer.position(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RouteType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getFrom() {
		return from;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getTo() {
		return to;
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
