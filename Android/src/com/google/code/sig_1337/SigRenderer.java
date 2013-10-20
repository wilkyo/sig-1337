package com.google.code.sig_1337;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.google.code.sig_1337.model.xml.IBuilding;
import com.google.code.sig_1337.model.xml.IBuildings;
import com.google.code.sig_1337.model.xml.IRoute;
import com.google.code.sig_1337.model.xml.IRoutes;
import com.google.code.sig_1337.model.xml.ISig1337;
import com.google.code.sig_1337.model.xml.ITriangle;

/**
 * Render for {@code Sig1337}.
 */
public class SigRenderer implements GLSurfaceView.Renderer {

	/**
	 * Clear color.
	 */
	private static final float[] CLEAR_COLOR = { 0.0f, 0.0f, 0.0f, 0.0f };

	/**
	 * SIG.
	 */
	private ISig1337 sig;

	FloatBuffer colorBuffer;

	/**
	 * Initializing constructor.
	 * 
	 * @param context
	 *            context.
	 */
	public SigRenderer(Context context) {
		super();
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
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -9.0f);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		drawGraphics(gl);

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
		for (ITriangle t : building.getTriangles()) {
			// Draw the triangle.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, t.getVertexBuffer());
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT,
					t.getIndexBuffer());
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
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
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
	}

}