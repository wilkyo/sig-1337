package com.google.code.sig_1337.model.xml.route;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.code.sig_1337.model.Vector;
import com.google.code.sig_1337.model.xml.IPoint;

/**
 * Routes.
 */
public class Routes implements IRoutes {

	/**
	 * Inner list.
	 */
	private final List<IRoute> inner;

	/**
	 * If the structures have been loaded.
	 */
	private boolean loaded;

	/**
	 * Total number of vertex.
	 */
	private int vertexCount;

	/**
	 * Total number of index.
	 */
	private int indexCount;

	/**
	 * Vertex buffer.
	 */
	private FloatBuffer fillVertexBuffer;

	/**
	 * Vertex buffer.
	 */
	private FloatBuffer strokeVertexBuffer;

	/**
	 * Index buffer.
	 */
	private ShortBuffer indexBuffer;

	/**
	 * Default constructor.
	 */
	public Routes() {
		super();
		inner = new ArrayList<IRoute>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IRoute route) {
		inner.add(route);
		int s = route.getPoints().length - 2;
		vertexCount += 12 + s * 12;
		indexCount += (s + 1) * 6;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		inner.clear();
		loaded = false;
		vertexCount = 0;
		indexCount = 0;
		if (fillVertexBuffer != null) {
			fillVertexBuffer.clear();
			fillVertexBuffer = null;
		}
		if (strokeVertexBuffer != null) {
			strokeVertexBuffer.clear();
			strokeVertexBuffer = null;
		}
		if (indexBuffer != null) {
			indexBuffer.clear();
			indexBuffer = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IRoute> iterator() {
		return inner.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		// Create the buffer, vertexCount * sizeof(float).
		ByteBuffer bb = ByteBuffer.allocateDirect(vertexCount * 4);
		bb.order(ByteOrder.nativeOrder());
		fillVertexBuffer = bb.asFloatBuffer();
		// Create the buffer, vertexCount * sizeof(float).
		bb = ByteBuffer.allocateDirect(vertexCount * 4);
		bb.order(ByteOrder.nativeOrder());
		strokeVertexBuffer = bb.asFloatBuffer();
		// Create the buffer, indexCount * sizeof(short).
		bb = ByteBuffer.allocateDirect(indexCount * 2);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		int index = 0;
		// For all the routes.
		for (IRoute route : inner) {
			IPoint[] points = route.getPoints();
			// For each part of the route.
			IPoint from = points[0];
			for (int i = 1; i < points.length; ++i) {
				IPoint to = points[i];
				// Fill the buffers.
				index = fill(route, from.getRelativeLongitude(),
						from.getRelativeLatitude(), to.getRelativeLongitude(),
						to.getRelativeLatitude(), index);
				from = to;
			}
		}
		// Reset the position.
		fillVertexBuffer.position(0);
		strokeVertexBuffer.position(0);
		indexBuffer.position(0);
		// Set loaded to true.
		loaded = true;
	}

	/**
	 * Fill the buffers with the two given points.
	 * 
	 * @param from
	 *            first point.
	 * @param to
	 *            second point.
	 */
	private int fill(IRoute route, double x1, double y1, double x2, double y2,
			int index) {
		RouteType type = route.getType();
		float hFillSize = type.getFillSize() / 2;
		float hStrokeSize = type.getStrokeSize() / 2;
		// Vector.
		Vector v = new Vector(x2 - x1, y2 - y1);
		v.normaliser();
		// Fill.
		v.scale(hFillSize);
		// P1.
		v.normaleGauche();
		fillVertexBuffer.put((float) (x1 + v.x));
		fillVertexBuffer.put((float) (y1 + v.y));
		fillVertexBuffer.put((float) (0));
		v.invert();
		fillVertexBuffer.put((float) (x1 + v.x));
		fillVertexBuffer.put((float) (y1 + v.y));
		fillVertexBuffer.put((float) (0));
		v.normaleGauche();
		// P2.
		v.normaleGauche();
		fillVertexBuffer.put((float) (x2 + v.x));
		fillVertexBuffer.put((float) (y2 + v.y));
		fillVertexBuffer.put((float) (0));
		v.invert();
		fillVertexBuffer.put((float) (x2 + v.x));
		fillVertexBuffer.put((float) (y2 + v.y));
		fillVertexBuffer.put((float) (0));
		v.normaleGauche();
		v.normaliser();
		// Stroke.
		v.scale(hStrokeSize);
		// P1.
		v.normaleGauche();
		strokeVertexBuffer.put((float) (x1 + v.x));
		strokeVertexBuffer.put((float) (y1 + v.y));
		strokeVertexBuffer.put((float) (0));
		v.invert();
		strokeVertexBuffer.put((float) (x1 + v.x));
		strokeVertexBuffer.put((float) (y1 + v.y));
		strokeVertexBuffer.put((float) (0));
		v.normaleGauche();
		// P2.
		v.normaleGauche();
		strokeVertexBuffer.put((float) (x2 + v.x));
		strokeVertexBuffer.put((float) (y2 + v.y));
		strokeVertexBuffer.put((float) (0));
		v.invert();
		strokeVertexBuffer.put((float) (x2 + v.x));
		strokeVertexBuffer.put((float) (y2 + v.y));
		strokeVertexBuffer.put((float) (0));
		v.normaleGauche();
		// Index.
		indexBuffer.put((short) (index));
		indexBuffer.put((short) (index + 1));
		indexBuffer.put((short) (index + 3));
		indexBuffer.put((short) (index));
		indexBuffer.put((short) (index + 2));
		indexBuffer.put((short) (index + 3));
		return index + 4;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIndexCount() {
		return indexCount;
	}

}
