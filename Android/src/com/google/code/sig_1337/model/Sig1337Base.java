package com.google.code.sig_1337.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.google.code.sig_1337.model.xml.Bounds;
import com.google.code.sig_1337.model.xml.Graphics;
import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraphics;

/**
 * Base for the sig.
 */
public abstract class Sig1337Base implements ISig1337 {

	/**
	 * Background color red component.
	 */
	public static final float BACKGROUND_RED = 248f / 255f;

	/**
	 * Background color green component.
	 */
	public static final float BACKGROUND_GREEN = 244f / 255f;

	/**
	 * Background color blue component.
	 */
	public static final float BACKGROUND_BLUE = 232f / 255f;

	/**
	 * Background color.
	 */
	public static final FloatBuffer BACKGROUND_COLOR;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(12);
		bb.order(ByteOrder.nativeOrder());
		BACKGROUND_COLOR = bb.asFloatBuffer();
		BACKGROUND_COLOR.put(new float[] { BACKGROUND_RED, BACKGROUND_GREEN,
				BACKGROUND_BLUE });
		BACKGROUND_COLOR.position(0);
	}

	/**
	 * Bounds.
	 */
	protected final Bounds bounds;

	/**
	 * Graphics.
	 */
	protected final IGraphics graphics;

	/**
	 * Default constructor.
	 */
	public Sig1337Base() {
		super();
		bounds = new Bounds(0, 0, 0, 0);
		graphics = new Graphics();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		bounds.setMinLat(0);
		bounds.setMinLon(0);
		bounds.setMaxLat(0);
		bounds.setMaxLon(0);
		graphics.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBounds getBounds() {
		return bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGraphics getGraphics() {
		return graphics;
	}

}
