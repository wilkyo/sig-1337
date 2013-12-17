package com.google.code.sig_1337;

import java.util.logging.Logger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Sensor listener.
 */
public class MySensorListener implements SensorEventListener {

	/**
	 * Rotation.
	 */
	private float rotation;

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
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent evt) {
		// Smoothing the sensor data a bit
		if (evt.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			rotation = evt.values[0];
			Logger.getLogger("pouet").info(rotation + " " );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

}
