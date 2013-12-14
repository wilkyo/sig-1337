package com.google.code.sig_1337;

import java.nio.FloatBuffer;
import java.util.Map.Entry;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;

import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.Sig1337Base;
import com.google.code.sig_1337.model.xml.IBounds;
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
	 * Scale listener.
	 */
	private MyScaleListener scaleListener;

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

	private double mapX;

	private double mapY;

	private double mapWidth;

	private double mapHeight;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	private float ratio;

	private float width;

	private float height;

	private float testScale;

	private float scale;

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
		scaleListener = new MyScaleListener();
		this.locationListener = locationListener;
	}

	/**
	 * Get the scale listener.
	 * 
	 * @return the scale listener.
	 */
	public MyScaleListener getScaleListener() {
		return scaleListener;
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

		// Bounds.
		IBounds bounds = sig.getBounds();
		mapX = bounds.getMinLon();
		mapY = bounds.getMinLat();
		mapWidth = bounds.getMaxLon() - bounds.getMinLon();
		mapHeight = bounds.getMaxLat() - bounds.getMinLat();
		// Translate.
		float cX = (float) (locationListener.getLongitude() - mapX);
		float cY = (float) (locationListener.getLatitude() - mapY);
		// Scale.
		float rW = (float) ((ratio * 2) / mapWidth);
		float rH = (float) ((2 / mapHeight));
		float initialScale = (float) Math.max(rW, rH);
		scale = initialScale * userScale;
		gl.glPushMatrix();
		//
		gl.glScalef(200 * userScale, 200 * userScale, 1);
		gl.glTranslatef(-(float) (cX - userDX / 100000f),
				-(float) (cY + userDY / 100000f), 0);
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
		userDX += dX;
		userDY += dY;
	}

	/**
	 * Custom scale listener.
	 */
	private class MyScaleListener extends SimpleOnScaleGestureListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			userScale *= detector.getScaleFactor();
			return true;
		}

	}

}