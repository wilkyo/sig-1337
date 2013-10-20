package com.google.code.sig_1337.model.xml;

import android.graphics.PointF;

/**
 * Point.
 */
public class Point extends PointF implements IPoint {

	/**
	 * Initializing constructor.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 */
	public Point(float x, float y) {
		super(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getX() {
		return x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getY() {
		return y;
	}

}
