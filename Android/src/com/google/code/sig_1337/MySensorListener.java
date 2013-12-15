package com.google.code.sig_1337;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Sensor listener.
 */
public class MySensorListener implements SensorEventListener {

	/**
	 * Rotation.
	 */
	private float rotation;

	private float[] gravity;
	private float[] geomag;
	private float[] values = new float[3];

	/**
	 * Get the rotation.
	 * 
	 * @return the rotation.
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSensorChanged(SensorEvent evt) {
		int type = evt.sensor.getType();
		// Smoothing the sensor data a bit
		if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			geomag = evt.values;
		} else if (type == Sensor.TYPE_ACCELEROMETER) {
			gravity = evt.values;
		}

		if ((type == Sensor.TYPE_MAGNETIC_FIELD)
				|| (type == Sensor.TYPE_ACCELEROMETER)) {
			if (gravity != null && geomag != null) {
				float r[] = new float[9];
				if (SensorManager.getRotationMatrix(r, null, gravity, geomag)) {
				/*	SensorManager
							.remapCoordinateSystem(r, SensorManager.AXIS_Y,
									SensorManager.AXIS_MINUS_X, r);*/
					SensorManager.getOrientation(r, values);
					rotation = values[0];
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

}
