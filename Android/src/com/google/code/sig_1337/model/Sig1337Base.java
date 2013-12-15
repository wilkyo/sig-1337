package com.google.code.sig_1337.model;

import com.google.code.sig_1337.model.xml.Bounds;
import com.google.code.sig_1337.model.xml.Graphics;
import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraphics;
import com.google.code.sig_1337.model.xml.structure.IStructure;

/**
 * Base for the sig.
 */
public abstract class Sig1337Base implements ISig1337 {

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructure getStructure(String name) {
		return graphics.getStructure(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructure getStructure(double x, double y) {
		return getStructure(getStructureName(x, y));
	}

}
