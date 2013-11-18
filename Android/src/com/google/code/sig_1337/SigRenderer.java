package com.google.code.sig_1337;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.DragEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;

import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IBuilding;
import com.google.code.sig_1337.model.xml.IBuildings;
import com.google.code.sig_1337.model.xml.IRoute;
import com.google.code.sig_1337.model.xml.IRoutes;
import com.google.code.sig_1337.model.xml.ISig1337;
import com.google.code.sig_1337.model.xml.ITriangle;
import com.google.code.sig_1337.model.xml.ITriangles;

/**
 * Render for {@code Sig1337}.
 */
public class SigRenderer implements GLSurfaceView.Renderer {

	/**
	 * Clear color.
	 */
	private static final float[] CLEAR_COLOR = { 0.0f, 0.0f, 0.0f, 0.0f };

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

	FloatBuffer colorBuffer;

	FloatBuffer colorBufferTrou;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	private float ratio;

	private float width;

	private float height;

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
		IBounds bounds = sig.getBounds();
		mapX = bounds.getMinLon();
		mapY = bounds.getMinLat();
		mapWidth = bounds.getMaxLon() - bounds.getMinLon();
		mapHeight = bounds.getMaxLat() - bounds.getMinLat();
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

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// Translate.
		float cX = (float) (locationListener.getLongitude() - mapX);
		float cY = (float) (locationListener.getLatitude() - mapY);
		// Scale.
		float rW = (float) ((ratio * 2) / mapWidth);
		float rH = (float) ((2 / mapHeight));
		float scale = (float) Math.max(rW, rH) * userScale;
		gl.glPushMatrix();
		//
		gl.glTranslatef(userDX/1000, -userDY/1000, 0);
		gl.glScalef(scale, scale, 1);
		gl.glTranslatef(-(float) cX, -(float) cY, 0);
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
		drawBuildings(gl);
		drawRoutes(gl);
	}

	/**
	 * Draw the buildings.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawBuildings(GL10 gl) {
		IBuildings l = sig.getGraphics().getBuildings();
		if (l != null && !l.isEmpty()) {
			for (IBuilding b : l) {
				drawBuilding(gl, b);
			}
		}
	}

	/**
	 * Draw the given building.
	 * 
	 * @param gl
	 *            OpenGL.
	 * @param building
	 *            building to draw.
	 */
	private void drawBuilding(GL10 gl, IBuilding building) {
		FloatBuffer color;
		for (ITriangles ts : building.getTriangles()) {
			// Color depending on the type.
			if (ts.getType() != null) {
				switch (ts.getType()) {
				case Trou:
					color = colorBufferTrou;
				default:
					color = colorBuffer;
				}
			} else {
				color = colorBuffer;
			}
			// Draw the triangles.
			for (ITriangle t : ts) {
				// Draw the triangle.
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, t.getVertexBuffer());
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, color);
				gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT,
						t.getIndexBuffer());
			}
		}
	}

	/**
	 * Draw the routes.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawRoutes(GL10 gl) {
		IRoutes l = sig.getGraphics().getRoutes();
		if (l != null && !l.isEmpty()) {
			for (IRoute r : l) {
				drawRoute(gl, r);
			}
		}
	}

	/**
	 * Draw the given route.
	 * 
	 * @param gl
	 *            OpenGL.
	 * @param route
	 *            route to draw.
	 */
	private void drawRoute(GL10 gl, IRoute route) {
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, route.getVertexBuffer());
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		switch (route.getType()) {
		case Path:
			gl.glLineWidth(2.0f);
			break;
		case Route:
			gl.glLineWidth(4.0f);
			break;
		}
		gl.glDrawElements(GL10.GL_LINES, 2, GL10.GL_UNSIGNED_SHORT,
				route.getIndexBuffer());
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
		gl.glClearColor(CLEAR_COLOR[0], CLEAR_COLOR[1], CLEAR_COLOR[2],
				CLEAR_COLOR[3]);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		ByteBuffer bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		colorBuffer = bb.asFloatBuffer();
		colorBuffer.put(new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
		colorBuffer.position(0);
		bb = ByteBuffer.allocateDirect(48);
		bb.order(ByteOrder.nativeOrder());
		colorBufferTrou = bb.asFloatBuffer();
		colorBufferTrou.put(new float[] { 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1 });
		colorBufferTrou.position(0);
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