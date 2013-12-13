package com.google.code.sig_1337.model.xml.route;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.google.code.sig_1337.model.xml.IPoint;

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
	 * Angle.
	 */
	private final float angle;

	/**
	 * Length.
	 */
	private final float length;

	/**
	 * Vertex buffer.
	 */
	private final FloatBuffer fillVertexBuffer;

	/**
	 * Vertex buffer.
	 */
	private final FloatBuffer strokeVertexBuffer;

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
		float dX = (float) (to.getRelativeLongitude() - from
				.getRelativeLongitude());
		float dY = (float) (to.getRelativeLatitude() - from
				.getRelativeLatitude());
		angle = (float) ((Math.atan2(dY, dX) * 180f) / Math.PI);
		length = (float) Math.sqrt(dX * dX + dY * dY);
		// Fill.
		float y = type.getFillSize() / 2f;
		ByteBuffer bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		fillVertexBuffer = bb.asFloatBuffer();
		fillVertexBuffer.put(new float[] { //
				0, y, 0, // From.
						length, y, 0, // To.
						0, -y, 0, // From.
						length, -y, 0 // To.
				});
		fillVertexBuffer.position(0);
		// Stroke.
		y = type.getStrokeSize() / 2f;
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		strokeVertexBuffer = bb.asFloatBuffer();
		strokeVertexBuffer.put(new float[] { //
				0, y, 0, // From.
						length, y, 0, // To.
						0, -y, 0, // From.
						length, -y, 0 // To.
				});
		strokeVertexBuffer.position(0);
		// Create the index buffer.
		bb = ByteBuffer.allocateDirect(12);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(new short[] { 0, 1, 2, 1, 2, 3 });
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
	public float getAngle() {
		return angle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getFillVertexBuffer() {
		return fillVertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getStrokeVertexBuffer() {
		return strokeVertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ShortBuffer getIndexBuffer() {
		return indexBuffer;
	}

}
