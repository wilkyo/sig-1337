package com.google.code.sig_1337;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;

import com.google.code.sig_1337.model.xml.IBounds;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.IRoute;
import com.google.code.sig_1337.model.xml.IRoutes;
import com.google.code.sig_1337.model.xml.ISig1337;
import com.google.code.sig_1337.model.xml.IStructure;
import com.google.code.sig_1337.model.xml.IStructures;
import com.google.code.sig_1337.model.xml.ITriangle;
import com.google.code.sig_1337.model.xml.ITriangles;
import com.google.code.sig_1337.model.xml.RouteType;
import com.google.code.sig_1337.model.xml.Sig1337;
import com.google.code.sig_1337.model.xml.StructureType;

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
		synchronized (structures) {
			if (structures != null && !structures.isEmpty()) {
				for (IStructure structure : structures) {
					drawStructure(gl, structure);
				}
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
	private void drawStructure(GL10 gl, IStructure structure) {
		StructureType type = structure.getType();
		List<ITriangles> list = structure.getTriangles();
		synchronized (list) {
			for (ITriangles triangles : list) {
				// Color depending on the type.
				FloatBuffer color = triangles.getType().getColor(type);
				// Draw the triangles.
				synchronized (triangles) {
					for (ITriangle t : triangles) {
						// Draw the triangle.
						gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
								t.getVertexBuffer());
						gl.glColorPointer(4, GL10.GL_FLOAT, 0, color);
						gl.glDrawElements(GL10.GL_TRIANGLES, 3,
								GL10.GL_UNSIGNED_SHORT, t.getIndexBuffer());
					}
				}
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
		synchronized (l) {
			if (l != null && !l.isEmpty()) {
				for (IRoute r : l) {
					drawRoute(gl, r);
				}
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
		RouteType type = route.getType();
		gl.glPushMatrix();
		IPoint from = route.getFrom();
		gl.glTranslatef((float) from.getRelativeLongitude(),
				(float) from.getRelativeLatitude(), 0);
		gl.glRotatef(route.getAngle(), 0, 0, 1);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, route.getFillVertexBuffer());
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, type.getFill());
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
				route.getIndexBuffer());
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, route.getStrokeVertexBuffer());
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, type.getStroke());
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
				route.getIndexBuffer());
		gl.glPopMatrix();
		/*
		 * gl.glVertexPointer(3, GL10.GL_FLOAT, 0, route.getVertexBuffer());
		 * gl.glColorPointer(4, GL10.GL_FLOAT, 0, type.getFill());
		 * gl.glLineWidth(type.getFillSize() * testScale);
		 * gl.glDrawElements(GL10.GL_LINES, 2, GL10.GL_UNSIGNED_SHORT,
		 * route.getIndexBuffer()); gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
		 * route.getVertexBuffer()); gl.glColorPointer(4, GL10.GL_FLOAT, 0,
		 * type.getStroke()); gl.glLineWidth(type.getStrokeSize() * testScale);
		 * gl.glDrawElements(GL10.GL_LINES, 2, GL10.GL_UNSIGNED_SHORT,
		 * route.getIndexBuffer());
		 */
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
		gl.glClearColor(Sig1337.BACKGROUND_RED, Sig1337.BACKGROUND_GREEN,
				Sig1337.BACKGROUND_BLUE, 1);
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