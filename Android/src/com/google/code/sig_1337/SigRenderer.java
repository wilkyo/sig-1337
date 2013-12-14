package com.google.code.sig_1337;

import java.nio.FloatBuffer;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.Sig1337Base;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.IRoutesMap;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.structure.IStructure;
import com.google.code.sig_1337.model.xml.structure.IStructures;

/**
 * Render for {@code Sig1337}.
 */
public class SigRenderer implements GLSurfaceView.Renderer {

	/**
	 * User scale factor.
	 */
	private float userScale = 1;

	/**
	 * User horizontal translation.
	 */
	private float userDX = 0;

	/**
	 * User vertical translation.
	 */
	private float userDY = 0;

	/**
	 * SIG.
	 */
	private ISig1337 sig;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	private float ratio;

	private int width;

	private int height;

	/**
	 * Initializing constructor.
	 * 
	 * @param context
	 *            context.
	 * @param GPS
	 *            listener.
	 */
	public SigRenderer(Context context, MyLocationListener locationListener) {
		super();
		this.locationListener = locationListener;
	}

	/**
	 * Set the SIG to render.
	 * 
	 * @param sig
	 *            new value.
	 */
	public synchronized void setSig(ISig1337 sig) {
		this.sig = sig;
	}

	/**
	 * Get the center x-coordinate.<br/>
	 * <br/>
	 * This is the GPS longitude minus the minimal map longitude.
	 * 
	 * @return the center x-coordinate.
	 */
	public float getCenterX() {
		return (float) (locationListener.getLongitude() - sig.getBounds()
				.getMinLon());
	}

	/**
	 * Get the center y-coordinate.<br/>
	 * <br/>
	 * This is the GPS latitude minus the minimal map latitude.
	 * 
	 * @return the center y-coordinate.
	 */
	public float getCenterY() {
		return (float) (locationListener.getLatitude() - sig.getBounds()
				.getMinLat());
	}

	/**
	 * Get the translated x-coordinate.<br/>
	 * <br/>
	 * This is the center x-coordinate minus the user horizontal translation.
	 * 
	 * @return the translated x-coordinate.
	 */
	public float getTranslatedX() {
		return getCenterX() - userDX;
	}

	/**
	 * Get the translated y-coordinate.<br/>
	 * <br/>
	 * This is the center y-coordinate minus the user vertical translation.
	 * 
	 * @return the translated y-coordinate.
	 */
	public float getTranslatedY() {
		return getCenterY() + userDY;
	}

	/**
	 * Get the translated x-coordinate.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @return the translated x-coordinate.
	 */
	public float getTranslatedX(double x) {
		return (float) (x - sig.getBounds().getMinLon()) - userDX;
	}

	/**
	 * Get the translated y-coordinate.
	 * 
	 * @param y
	 *            y-coordinate.
	 * @return the translated y-coordinate.
	 */
	public float getTranslatedY(double y) {
		return (float) (y - sig.getBounds().getMinLat()) + userDY;
	}

	/**
	 * Get the scale factor.
	 * 
	 * @return the scale factor.
	 */
	public float getScale() {
		return 200 * userScale;
	}

	/**
	 * Get the scale factor.
	 * 
	 * @return the scale factor.
	 */
	public float getScaleX() {
		return getScale();
	}

	/**
	 * Get the scale factor.
	 * 
	 * @return the scale factor.
	 */
	public float getScaleY() {
		return getScale();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-ratio, ratio, -1, 1, 0, 1);
		gl.glTranslatef(0, 0, -0.00001f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glPushMatrix();
		gl.glScalef(getScaleX(), getScaleY(), 1);
		gl.glTranslatef(-getTranslatedX(), -getTranslatedY(), 0);
		drawGraphics(gl);
		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	/**
	 * Draw the graphics.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawGraphics(GL10 gl) {
		drawRoutes(gl);
		drawStructures(gl, sig.getGraphics().getBuildings());
		drawStructures(gl, sig.getGraphics().getForets());
		drawStructures(gl, sig.getGraphics().getBassins());
	}

	/**
	 * Draw the structures.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawStructures(GL10 gl,
			IStructures<? extends IStructure> structures) {
		if (structures.isLoaded()) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			// Fill.
			FloatBuffer color = structures.getType().getFill();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
					structures.getFilledVertexBuffer());
			gl.glColor4f(color.get(0), color.get(1), color.get(2), color.get(3));
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0,
					structures.getFilledIndexCount());
			// Hole.
			color = structures.getType().getHole();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
					structures.getHoleVertexBuffer());
			gl.glColor4f(color.get(0), color.get(1), color.get(2), color.get(3));
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0,
					structures.getHoleIndexCount());
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}
	}

	/**
	 * Draw the routes.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawRoutes(GL10 gl) {
		IRoutesMap routes = sig.getGraphics().getRoutes();
		if (routes.isLoaded()) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			// For each type of route.
			for (Entry<RouteType, IRoutes> e : routes) {
				// Draw all the routes of this type.
				RouteType t = e.getKey();
				IRoutes r = e.getValue();
				// Fill.
				FloatBuffer color = t.getFill();
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, r.getFillVertexBuffer());
				gl.glColor4f(color.get(0), color.get(1), color.get(2),
						color.get(3));
				gl.glDrawElements(GL10.GL_TRIANGLES, r.getIndexCount(),
						GL10.GL_UNSIGNED_SHORT, r.getIndexBuffer());
				// Stroke.
				color = t.getStroke();
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
						r.getStrokeVertexBuffer());
				gl.glColor4f(color.get(0), color.get(1), color.get(2),
						color.get(3));
				gl.glDrawElements(GL10.GL_TRIANGLES, r.getIndexCount(),
						GL10.GL_UNSIGNED_SHORT, r.getIndexBuffer());
			}
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height);
		ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-ratio, ratio, -1, 1, 0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(Sig1337Base.BACKGROUND_RED,
				Sig1337Base.BACKGROUND_GREEN, Sig1337Base.BACKGROUND_BLUE, 1);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	/**
	 * Called when a drag event occurred.
	 * 
	 * @param dX
	 *            Horizontal dragging.
	 * @param dY
	 *            Vertical dragging.
	 */
	public void onDrag(float dX, float dY) {
		userDX += dX / 100000f;
		userDY += dY / 100000f;
	}

	/**
	 * Called when a tap event occurred.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 */
	public void onTap(float x, float y) {
		Point2D p = screenToMap(x, y);
		Logger.getLogger("pouet").info(p.x + " " + p.y);
		String name = sig.getStructureName(p.x, p.y);
		Logger.getLogger("pouet").info("Name: " + name);
	}

	/**
	 * Called when a scale event occurred.
	 * 
	 * @param scale
	 *            scale factor.
	 */
	public void onScale(float scale) {
		userScale *= scale;
	}

	/**
	 * Convert the screen coordinates to map coordinates.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @return new coordinates.
	 */
	public Point2D screenToMap(double x, double y) {
		Logger.getLogger("pouet").info("Screen: " + x + " " + y);
		// Screen to projection.
		x = ((x - width / 2f) / (width / 2f)) * ratio;
		y = ((y - height / 2f) / (height / 2f)) * 1;
		// Remove scale factor and center.
		Logger.getLogger("pouet").info(
				"Center: " + getTranslatedX() + " " + getTranslatedY());
		x /= getScaleX();
		y /= getScaleY();
		// User translation.
		x -= userDX;
		y += userDY;
		Logger.getLogger("pouet").info("User: " + x + " " + y);
		x = ((x + userDX) + ((getTranslatedX() + userDX) - locationListener
				.getLongitude())) * -1;
		y = ((y - userDY) + ((getTranslatedY() - userDY) - locationListener
				.getLatitude())) * -1;
		Logger.getLogger("pouet").info("Absolute: " + x + " " + y);
		return new Point2D(x, y);
	}

	/**
	 * Convert the map coordinates to screen coordinates.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @return new coordinates.
	 */
	public Point2D mapToScreen(double x, double y) {
		// Absolute to relative.
		x -= sig.getBounds().getMinLon();
		y -= sig.getBounds().getMinLat();
		// User translation.
		x -= userDX;
		y += userDY;
		// Distance from center.
		x -= getTranslatedX();
		y -= getTranslatedY();
		return null;
	}

	private static class Point2D {

		private double x;
		private double y;

		public Point2D(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

}