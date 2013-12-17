package com.google.code.sig_1337;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.code.sig_1337.ItineraireActivity.ItineraireItem;
import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.route.IRoute;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IStructure;
import com.google.code.sig_1337.model.xml.structure.IStructures;

public abstract class ActivityBase extends Activity implements
		SigRendererListener {

	/**
	 * Choice for the starting location.
	 */
	private static final String MA_POSITION = "Ma position";

	/**
	 * View.
	 */
	private SigView view;

	/**
	 * Location manager.
	 */
	private LocationManager locationManager;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	/**
	 * Sensor manager.
	 */
	private SensorManager sensorManager;

	/**
	 * Sensor listener.
	 */
	private MySensorListener sensorListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// GPS.
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MockLocationListener(); // TODO
		// Sensor.
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorListener = new MySensorListener();
		//
		final Logger l = Logger.getLogger("Pouet");
		//
		view = new SigView(this, locationListener, sensorListener);
		setContentView(view);
		try {
			// Create and get the sig.
			ISig1337 sig = getSig1337();
			// Set the sig in the view.
			view.getRenderer().setSig(sig);
			// Load the sig.
			loadSig1337();
		} catch (Exception e) {
			l.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_base, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		view.onPause();
		// Detach the listeners.
		view.getRenderer().remove(this);
		locationManager.removeUpdates(locationListener);
		sensorManager.unregisterListener(sensorListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
		view.onResume();
		// Attach the listeners.
		view.getRenderer().add(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		sensorManager.registerListener(sensorListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * Get the sig.
	 * 
	 * @return the sig.
	 */
	protected abstract ISig1337 getSig1337();

	/**
	 * Load the sig.
	 */
	protected abstract void loadSig1337();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean action = false;
		if (item.getItemId() == R.id.itineraire) {
			ItineraireItem[] liste = {};
			ArrayList<ItineraireItem> l = new ArrayList<ItineraireItem>();
			for (IBuilding building : getSig1337().getGraphics().getBuildings()) {
				String name = building.getName();
				if (name != null && !name.equals("") && !l.contains(name)
						&& building.getVoisins().size() != 0)
					l.add(new ItineraireItem(building.getId(), building
							.getName()));
			}
			Intent i = new Intent(this, ItineraireActivity.class);
			Collections.sort(l);
			l.add(0, new ItineraireItem(-1, MA_POSITION));
			liste = l.toArray(liste);
			i.putExtra("Nombat", liste);
			startActivityForResult(i, 1);
		}
		return action;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data.getAction().equals("Routage")) {
			if (requestCode == resultCode) {
				ItineraireItem bdepart = data.getParcelableExtra("Source");
				ItineraireItem barrive = data.getParcelableExtra("Target");
				IStructures<IBuilding> buildings = getSig1337().getGraphics()
						.getBuildings();
				IBuilding depart = null;
				if (MA_POSITION.equals(bdepart)) {
					// Get the id of the structure at our location.
					long id = getSig1337().getStructureId(
							locationListener.getLongitude(),
							locationListener.getLatitude());
					// Get the corresponding building.
					depart = buildings.get(id);
				} else {
					depart = buildings.get(bdepart.id);
				}
				IBuilding arrive = null;
				if (MA_POSITION.equals(barrive)) {
					long id = getSig1337().getStructureId(
							locationListener.getLongitude(),
							locationListener.getLatitude());
					arrive = buildings.get(id);
				} else {
					arrive = buildings.get(barrive.id);
				}
				if ((depart != null && depart.getVoisins().size() != 0)
						&& (arrive != null && arrive.getVoisins().size() != 0)) {
					IRoutes iti = getSig1337().getItineraire(depart, arrive);
					view.getRenderer().onItineraire(iti);
					if (iti != null) {
						// Debug.
						String s = "";
						for (IRoute route : iti) {
							for (IPoint iPoint : route.getPoints()) {
								s += iPoint.toString() + " ";
							}
						}
						Log.d("pouet", s);
					} else {
						Log.d("pouet", "No path.");
						Toast.makeText(this, R.string.no_path,
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Log.d("pouet", "Do nothing.");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStructureSelected(IStructure structure) {
		if (structure != null) {
			String name = structure.getName();
			if (name != null && !name.equals("")) {
				Toast.makeText(this, structure.getName(), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

}
