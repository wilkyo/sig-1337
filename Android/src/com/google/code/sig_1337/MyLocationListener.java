package com.google.code.sig_1337;

import java.util.logging.Logger;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * GPS listener.
 */
public class MyLocationListener implements LocationListener {

	/**
	 * Latitude in degrees.
	 */
	private double latitude;

	/**
	 * Longitude in degrees.
	 */
	private double longitude;

	/**
	 * Bearing.
	 */
	private float bearing;

	/**
	 * Get the latitude.
	 * 
	 * @return The latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get the longitude.
	 * 
	 * @return The longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Get the bearing.
	 * 
	 * @return The bearing.
	 */
	public float getBearing() {
		return bearing;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		bearing = location.getBearing();
		Logger.getLogger("pouet", bearing + "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO GPS disabled.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO GPS enabled.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
