package com.google.code.sig_1337;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.google.code.sig_1337.model.xml.Sig1337;

/**
 * OpenGL view.
 */
@SuppressLint("ViewConstructor")
public class SigView extends GLSurfaceView {

	/**
	 * Renderer.
	 */
	private SigRenderer renderer;

	/**
	 * Scale detector.
	 */
	private ScaleGestureDetector scaleDetector;

	/**
	 * Pointer id for dragging.
	 */
	private int activePointerId = -1;

	/**
	 * Last touch x-coordinate.
	 */
	private float lastTouchX;

	/**
	 * Last touch y-coordinate.
	 */
	private float lastTouchY;

	/**
	 * Current touch x-coordinate.
	 */
	private float posX;

	/**
	 * Current touch y-coordinate.
	 */
	private float posY;

	/**
	 * Initializing constructor.
	 * 
	 * @param context
	 *            the context.
	 * @param locationListener
	 *            location listener.
	 */
	public SigView(Context context, MyLocationListener locationListener) {
		super(context);
		renderer = new SigRenderer(context, locationListener);
		scaleDetector = new ScaleGestureDetector(context,
				renderer.getScaleListener());
		setRenderer(renderer);
	}

	/**
	 * Set the sig.
	 * 
	 * @param sig
	 *            new value.
	 */
	public void setSig(Sig1337 sig) {
		renderer.setSig(sig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Scale.
		scaleDetector.onTouchEvent(event);

		// Drag.
		final int action = MotionEventCompat.getActionMasked(event);
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			// Remember where we started (for dragging)
			lastTouchX = x;
			lastTouchY = y;
			// Save the ID of this pointer (for dragging)
			activePointerId = MotionEventCompat.getPointerId(event, 0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Find the index of the active pointer and fetch its position
			final int pointerIndex = MotionEventCompat.findPointerIndex(event,
					activePointerId);

			final float x = MotionEventCompat.getX(event, pointerIndex);
			final float y = MotionEventCompat.getY(event, pointerIndex);

			// Calculate the distance moved
			final float dx = x - lastTouchX;
			final float dy = y - lastTouchY;
			renderer.onDrag(dx, dy);

			posX += dx;
			posY += dy;

			// Remember this touch position for the next move event
			lastTouchX = x;
			lastTouchY = y;

			break;
		}
		case MotionEvent.ACTION_UP: {
			activePointerId = -1;
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			activePointerId = -1;
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			final int pointerId = MotionEventCompat.getPointerId(event,
					pointerIndex);
			if (pointerId == activePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				lastTouchX = MotionEventCompat.getX(event, newPointerIndex);
				lastTouchY = MotionEventCompat.getY(event, newPointerIndex);
				activePointerId = MotionEventCompat.getPointerId(event,
						newPointerIndex);
			}
			break;
		}
		}
		return true;
	}

}