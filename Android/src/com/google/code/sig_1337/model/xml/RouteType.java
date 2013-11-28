package com.google.code.sig_1337.model.xml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Enums for route types.
 */
public enum RouteType {

	/**
	 * Path.
	 */
	Path("chemin") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getFill() {
			return PATH_FILL;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getStroke() {
			return PATH_STROKE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getFillSize() {
			return 0.00004f;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getStrokeSize() {
			return 0.00006f;
		}

	},
	/**
	 * Route.
	 */
	Route("route") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getFill() {
			return ROUTE_FILL;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getStroke() {
			return ROUTE_STROKE;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getFillSize() {
			return 0.00006f;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getStrokeSize() {
			return 0.00008f;
		}

	};

	/**
	 * Fill color for paths.
	 */
	private static final FloatBuffer PATH_FILL;

	/**
	 * Stroke color for paths.
	 */
	private static final FloatBuffer PATH_STROKE;

	/**
	 * Fill color for routes.
	 */
	private static final FloatBuffer ROUTE_FILL;

	/**
	 * Stroke color for routes.
	 */
	private static final FloatBuffer ROUTE_STROKE;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(64);
		bb.order(ByteOrder.nativeOrder());
		PATH_FILL = bb.asFloatBuffer();
		float r = 253f / 255f;
		float g = 250f / 255f;
		float b = 240f / 255f;
		PATH_FILL.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1, r, g,
				b, 1 });
		PATH_FILL.position(0);
		bb = ByteBuffer.allocateDirect(64);
		bb.order(ByteOrder.nativeOrder());
		PATH_STROKE = bb.asFloatBuffer();
		r = 188f / 255f;
		g = 182f / 255f;
		b = 174f / 255f;
		PATH_STROKE.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1, r, g,
				b, 1 });
		PATH_STROKE.position(0);
		bb = ByteBuffer.allocateDirect(64);
		bb.order(ByteOrder.nativeOrder());
		ROUTE_FILL = bb.asFloatBuffer();
		r = 253f / 255f;
		g = 212f / 255f;
		b = 117f / 255f;
		ROUTE_FILL.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1, r, g,
				b, 1, });
		ROUTE_FILL.position(0);
		bb = ByteBuffer.allocateDirect(64);
		bb.order(ByteOrder.nativeOrder());
		ROUTE_STROKE = bb.asFloatBuffer();
		r = 143f / 255f;
		g = 125f / 255f;
		b = 105f / 255f;
		ROUTE_STROKE.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1, r,
				g, b, 1 });
		ROUTE_STROKE.position(0);
	}

	/**
	 * Parse the given string to the corresponding route type.
	 * 
	 * @param s
	 *            the string to parse.
	 * @return the corresponding route type.
	 */
	public static RouteType parse(String s) {
		for (RouteType t : values()) {
			if (s.equals(t.name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Name of this type.
	 */
	private final String name;

	/**
	 * Initializing constructor.
	 * 
	 * @param name
	 *            name of this type.
	 */
	private RouteType(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this type.
	 * 
	 * @return the name of this type.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the fill color.
	 * 
	 * @return fill color.
	 */
	public abstract FloatBuffer getFill();

	/**
	 * Get the stroke color.
	 * 
	 * @return stroke color.
	 */
	public abstract FloatBuffer getStroke();

	/**
	 * Get the width.
	 * 
	 * @return width.
	 */
	public abstract float getFillSize();

	/**
	 * Get the width.
	 * 
	 * @return width.
	 */
	public abstract float getStrokeSize();

}
