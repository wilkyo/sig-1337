package com.google.code.sig_1337;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.google.code.sig_1337.model.Color;
import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.IRoutesMap;
import com.google.code.sig_1337.model.xml.route.Route;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.route.Routes;
import com.google.code.sig_1337.model.xml.structure.IStructure;
import com.google.code.sig_1337.model.xml.structure.IStructures;

/**
 * Render for {@code Sig1337}.
 */
public class SigRenderer implements GLSurfaceView.Renderer {

	/**
	 * Scale of the user icon.
	 */
	private static final float USER_SCALE = 0.75f;

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
	 * Location listener.
	 */
	private MyLocationListener locationListener;

	/**
	 * Sensor listener.
	 */
	private MySensorListener sensorListener;

	private float ratio;

	private int width;

	private int height;

	private IRoutes itineraire;

	private User user;

	/**
	 * Initializing constructor.
	 * 
	 * @param context
	 *            context.
	 * @param locationListener
	 *            location listener.
	 * @param sensorListener
	 *            sensor listener.
	 */
	public SigRenderer(Context context, MyLocationListener locationListener,
			MySensorListener sensorListener) {
		super();
		this.locationListener = locationListener;
		this.sensorListener = sensorListener;
		user = new User();
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
	 * Called when an itineraire has been selected.
	 * 
	 * @param itineraire
	 *            selected itineraire.
	 */
	public void onItineraire(IItineraire itineraire) {
		if (itineraire == null) {
			this.itineraire = null;
		} else {
			Routes routes = new Routes();
			routes.add(new Route(RouteType.Itineraire, itineraire
					.toArray(new IPoint[itineraire.size()])));
			routes.done();
			this.itineraire = routes;
		}
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
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		{
			gl.glPushMatrix();
			gl.glScalef(getScaleX(), getScaleY(), 1);
			gl.glTranslatef(userDX, -userDY, 0);
			drawUser(gl);
			gl.glTranslatef(-getCenterX(), -getCenterY(), 0);
			drawGraphics(gl);
			gl.glPopMatrix();
		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	/**
	 * Draw the graphics.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawGraphics(GL10 gl) {
		if (itineraire != null && itineraire.isLoaded()) {
			drawRoutes(gl, RouteType.Itineraire, itineraire);
		}
		drawRoutes(gl);
		drawStructures(gl, sig.getGraphics().getBuildings());
		drawStructures(gl, sig.getGraphics().getForets());
		drawStructures(gl, sig.getGraphics().getBassins());
	}

	/**
	 * Draw the user.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawUser(GL10 gl) {
		gl.glPushMatrix();
		gl.glScalef(USER_SCALE, USER_SCALE, 1);
		gl.glRotatef((float) (Math.toDegrees(sensorListener.getRotation())), 0,
				0, -1);
		{
			// Fill.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, user.fillVertexBuffer);
			gl.glColor4f(Color.LIGHT_BLUE.red, Color.LIGHT_BLUE.green,
					Color.LIGHT_BLUE.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, user.vertexCount);
			// Stroke.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, user.strokeVertexBuffer);
			gl.glColor4f(Color.DARK_BLUE.red, Color.DARK_BLUE.green,
					Color.DARK_BLUE.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, user.vertexCount);
		}
		{
			// Fill.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, user.fillVertexBufferArrow);
			gl.glColor4f(Color.LIGHT_BLUE.red, Color.LIGHT_BLUE.green,
					Color.LIGHT_BLUE.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, user.vertexCountArrow);
			// Stroke.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
					user.strokeVertexBufferArrow);
			gl.glColor4f(Color.DARK_BLUE.red, Color.DARK_BLUE.green,
					Color.DARK_BLUE.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, user.vertexCountArrow);
		}
		gl.glPopMatrix();
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
			// Fill.
			Color color = structures.getType().getFill();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
					structures.getFilledVertexBuffer());
			gl.glColor4f(color.red, color.green, color.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0,
					structures.getFilledIndexCount());
			// Hole.
			color = structures.getType().getHole();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
					structures.getHoleVertexBuffer());
			gl.glColor4f(color.red, color.green, color.blue, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0,
					structures.getHoleIndexCount());
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
			// For each type of route.
			for (Entry<RouteType, IRoutes> e : routes) {
				drawRoutes(gl, e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * Draw the routes.
	 * 
	 * @param gl
	 *            OpenGL.
	 */
	private void drawRoutes(GL10 gl, RouteType type, IRoutes routes) {
		// Fill.
		Color color = type.getFill();
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, routes.getFillVertexBuffer());
		gl.glColor4f(color.red, color.green, color.blue, 1);
		gl.glDrawElements(GL10.GL_TRIANGLES, routes.getIndexCount(),
				GL10.GL_UNSIGNED_SHORT, routes.getIndexBuffer());
		// Stroke.
		color = type.getStroke();
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, routes.getStrokeVertexBuffer());
		gl.glColor4f(color.red, color.green, color.blue, 1);
		gl.glDrawElements(GL10.GL_TRIANGLES, routes.getIndexCount(),
				GL10.GL_UNSIGNED_SHORT, routes.getIndexBuffer());
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
		gl.glClearColor(Color.BACKGROUND.red, Color.BACKGROUND.green,
				Color.BACKGROUND.blue, 1);
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
		Logger.getLogger("pouet").info("screen " + x + " " + y);
		Point2D p = screenToMap(x, y);
		Logger.getLogger("pouet").info("map: " + p.x + " " + p.y);
		Point2D p2 = mapToScreen(p.x, p.y);
		Logger.getLogger("pouet").info("screen " + p2.x + " " + p2.y);
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
		// Scale to OpenGL.
		x = ((x - (width / 2f)) / (width / 2f)) * ratio;
		y = ((y - (height / 2f)) / (height / 2f) * 1);
		// Remove scale.
		x /= getScaleX();
		y /= getScaleY();
		// Center.
		double cX = getCenterX() - userDX;
		double cY = getCenterY() + userDY;
		// Remove distance from center.
		x = x + cX;
		y = cY - y;
		// Position.
		x = (x + sig.getBounds().getMinLon());
		y = (y + sig.getBounds().getMinLat());
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
		// Center.
		double cX = getCenterX() - userDX;
		double cY = getCenterY() + userDY;
		// Position.
		x = (x - sig.getBounds().getMinLon());
		y = (y - sig.getBounds().getMinLat());
		// Distance from center.
		x = x - cX;
		y = cY - y;
		// Scale.
		x *= getScaleX();
		y *= getScaleY();
		// Scale to screen.
		x = (x / ratio) * (width / 2f) + (width / 2f);
		y = (y / 1) * (height / 2f) + (height / 2f);
		return new Point2D(x, y);
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

	private static class User {

		/**
		 * Radius for the circle of the user icon.
		 */
		private static final float USER_RADIUS = 0.0001f;

		/**
		 * Distance between the circle and the arrow of the user icon.
		 */
		private static final float USER_DISTANCE_ARROW = USER_RADIUS * 0.875f;

		/**
		 * Size for the arrow of the user icon.
		 */
		private static final float USER_SIZE_ARROW = 0.0001f;

		/**
		 * Number of vertex for the circle of the user icon.
		 */
		private static final int USER_VERTEX = 12;

		/**
		 * Vertex buffer.
		 */
		private FloatBuffer fillVertexBuffer;

		/**
		 * Vertex buffer.
		 */
		private FloatBuffer strokeVertexBuffer;

		/**
		 * Vertex buffer.
		 */
		private FloatBuffer fillVertexBufferArrow;

		/**
		 * Vertex buffer.
		 */
		private FloatBuffer strokeVertexBufferArrow;

		/**
		 * Number of vertex.
		 */
		private int vertexCount;

		/**
		 * Number of vertex.
		 */
		private int vertexCountArrow;

		public User() {
			vertexCount = USER_VERTEX + 2;
			vertexCountArrow = 3;
			// Circle.
			ByteBuffer bb = ByteBuffer.allocateDirect(vertexCount * 3 * 4);
			bb.order(ByteOrder.nativeOrder());
			fillVertexBuffer = bb.asFloatBuffer();
			bb = ByteBuffer.allocateDirect(vertexCount * 3 * 4);
			bb.order(ByteOrder.nativeOrder());
			strokeVertexBuffer = bb.asFloatBuffer();
			fill(fillVertexBuffer, USER_VERTEX, USER_RADIUS);
			fill(strokeVertexBuffer, USER_VERTEX, USER_RADIUS * 1.25f);
			// Arrow.
			bb = ByteBuffer.allocateDirect(vertexCountArrow * 3 * 4);
			bb.order(ByteOrder.nativeOrder());
			fillVertexBufferArrow = bb.asFloatBuffer();
			bb = ByteBuffer.allocateDirect(vertexCountArrow * 3 * 4);
			bb.order(ByteOrder.nativeOrder());
			strokeVertexBufferArrow = bb.asFloatBuffer();
			fillArrow(fillVertexBufferArrow, USER_DISTANCE_ARROW,
					USER_SIZE_ARROW);
			fillArrow(strokeVertexBufferArrow, USER_DISTANCE_ARROW * 0.875f,
					USER_SIZE_ARROW * 1.5f);
		}

		private void fill(FloatBuffer vertexBuffer, int vertex, float radius) {
			double step = (Math.PI * 2) / vertex;
			double a = 0;
			vertexBuffer.put(0f);
			vertexBuffer.put(0f);
			vertexBuffer.put(0f);
			for (int i = 0; i < vertex; ++i) {
				vertexBuffer.put((float) (Math.cos(a) * radius));
				vertexBuffer.put((float) (Math.sin(a) * radius));
				vertexBuffer.put(0);
				a += step;
			}
			vertexBuffer.put((float) (Math.cos(0) * radius));
			vertexBuffer.put((float) (Math.sin(0) * radius));
			vertexBuffer.put(0);
			vertexBuffer.position(0);
		}

		private void fillArrow(FloatBuffer vertexBuffer, float distance,
				float size) {
			vertexBuffer.put((float) (distance));
			vertexBuffer.put((float) (size));
			vertexBuffer.put((float) (0));
			vertexBuffer.put((float) (distance));
			vertexBuffer.put((float) (-size));
			vertexBuffer.put((float) (0));
			vertexBuffer.put((float) (distance + size * 1.5f));
			vertexBuffer.put((float) (0));
			vertexBuffer.put((float) (0));
			vertexBuffer.position(0);
		}
	}

}