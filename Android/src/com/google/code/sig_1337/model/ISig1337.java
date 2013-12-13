package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IGraphics;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.structure.IStructure;

/**
 * Interface for the sig.
 */
public interface ISig1337 {

	/**
	 * Get the bounds.
	 * 
	 * @return the bounds.
	 */
	public IBounds getBounds();

	/**
	 * Get the graphics.
	 * 
	 * @return the graphics.
	 */
	public IGraphics getGraphics();

	/**
	 * Clear the sig.
	 */
	public void clear();

	/**
	 * Load the sig.
	 * 
	 * @param is
	 *            input stream.
	 * @throws XmlPullParserException
	 *             error with the parser.
	 * @throws IOException
	 *             error while reading.
	 * @throws InterruptedException
	 *             operation interrupted.
	 */
	public void load(InputStream is) throws XmlPullParserException,
			IOException, InterruptedException;

	/**
	 * Get the structure at the given coordinates.
	 * 
	 * @param x
	 *            longitude.
	 * @param y
	 *            latitude.
	 * @return the structure.
	 */
	public IStructure getStructure(double x, double y);

	/**
	 * Get the itineraire from one point to another.
	 * 
	 * @param x1
	 *            first point x-coordinate.
	 * @param y1
	 *            first point y-coordinate.
	 * @param x2
	 *            second point x-coordinate.
	 * @param y2
	 *            second point y-coordinate.
	 * @return the itineraire.
	 */
	public IItineraire getItineraire(double x1, double y1, double x2, double y2);

}
