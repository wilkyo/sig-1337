package com.google.code.sig_1337.model.xml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Enum for triangles types.
 */
public enum TrianglesType {

	/**
	 * Filled.
	 */
	Filled("") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getFill() {
			return FILLED_FILL;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getStroke() {
			return FILLED_STROKE;
		}

	},

	/**
	 * Hole.
	 */
	Hole("trou") {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getFill() {
			return HOLE_FILL;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getStroke() {
			return HOLE_STROKE;
		}

	};

	/**
	 * Fill color for filled triangles.
	 */
	private static final FloatBuffer FILLED_FILL;

	/**
	 * Stroke color for filled triangles.
	 */
	private static final FloatBuffer FILLED_STROKE;

	/**
	 * Fill color for holes.
	 */
	private static final FloatBuffer HOLE_FILL;

	/**
	 * Stroke color for holes.
	 */
	private static final FloatBuffer HOLE_STROKE;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		FILLED_FILL = bb.asFloatBuffer();
		float r = 188f / 255f;
		float g = 182f / 255f;
		float b = 174f / 255f;
		FILLED_FILL.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		FILLED_FILL.position(0);
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		FILLED_STROKE = bb.asFloatBuffer();
		r = 188f / 255f;
		g = 182f / 255f;
		b = 174f / 255f;
		FILLED_STROKE.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		FILLED_STROKE.position(0);
		HOLE_FILL = Sig1337.BACKGROUND_COLOR;
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		HOLE_STROKE = bb.asFloatBuffer();
		r = 188f / 255f;
		g = 182f / 255f;
		b = 174f / 255f;
		HOLE_STROKE.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		HOLE_STROKE.position(0);
	}

	/**
	 * Parse the given string to the corresponding triangles type.
	 * 
	 * @param s
	 *            the string to parse.
	 * @return the corresponding triangles type.
	 */
	public static TrianglesType parse(String s) {
		for (TrianglesType t : values()) {
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
	private TrianglesType(String name) {
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

}
