package com.google.code.sig_1337.model.xml.structure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public enum StructureType {

	Bassin {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getColor() {
			return BASSIN;
		}

	},
	Foret {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getColor() {
			return FORET;
		}

	},
	Building {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FloatBuffer getColor() {
			return BUILDING;
		}

	};

	/**
	 * Color for bassins.
	 */
	private static final FloatBuffer BASSIN;

	/**
	 * Color for forets.
	 */
	private static final FloatBuffer FORET;

	/**
	 * Color for buildings.
	 */
	private static final FloatBuffer BUILDING;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		BASSIN = bb.asFloatBuffer();
		float r = 190f / 255f;
		float g = 208f / 255f;
		float b = 222f / 255f;
		BASSIN.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		BASSIN.position(0);
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		FORET = bb.asFloatBuffer();
		r = 205f / 255f;
		g = 213f / 255f;
		b = 132f / 255f;
		FORET.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		FORET.position(0);
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		BUILDING = bb.asFloatBuffer();
		r = 188f / 255f;
		g = 182f / 255f;
		b = 174f / 255f;
		BUILDING.put(new float[] { r, g, b, 1, r, g, b, 1, r, g, b, 1 });
		BUILDING.position(0);
	}

	/**
	 * Get the color.
	 * 
	 * @return color.
	 */
	public abstract FloatBuffer getColor();

}
