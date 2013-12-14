package com.google.code.sig_1337;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.code.sig_1337.itineraire.Itineraire;
import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.LocalSig1337;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.structure.IBuilding;

public abstract class ActivityBase extends Activity {

	/**
	 * View.
	 */
	private SigView view;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// GPS.
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MockLocationListener(); // TODO
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
		final Logger l = Logger.getLogger("Pouet");
		//
		view = new SigView(this, locationListener);
		setContentView(view);
		try {
			// Create and get the sig.
			ISig1337 sig = getSig1337();
			// Set the sig in the view.
			view.setSig(sig);
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		view.onResume();
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
			String[] liste = {};
			ArrayList<String> l = new ArrayList<String>();
			for (IBuilding building : getSig1337().getGraphics().getBuildings()) {
				String name = building.getName();
				if (name != null && !name.equals("") && !l.contains(name) && building.getVoisins().size() != 0)
					l.add(building.getName());
			}
			Intent i = new Intent(this, ItineraireActivity.class);
			liste = l.toArray(liste);
			Arrays.sort(liste);
			i.putExtra("Nombat", liste);
			startActivityForResult(i, 1);
		}
		return action;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data.getAction().equals("Routage")) {
			if (requestCode == resultCode) {
				String bdepart = data.getStringExtra("Source");
				String barrive = data.getStringExtra("Target");
				IBuilding depart=null,arrive=null;
				for (IBuilding b : getSig1337().getGraphics().getBuildings()) {
					if(b.getName().equals(bdepart) && b.getVoisins().size() != 0) {
						depart = b;
						break;
					}
				}
				for (IBuilding b : getSig1337().getGraphics().getBuildings()) {
					if(b.getName().equals(barrive) && b.getVoisins().size() != 0) {
						arrive = b;
						break;
					}
				}
				if(depart != null && arrive != null) {
					IItineraire iti = getSig1337().getItineraire(depart, arrive);
					if(iti != null) {
						String s ="";
						for (IPoint iPoint : iti) {
							s+=iPoint.toString() + " ";
						}
						Log.d("pouet",s);
					} else {
						Log.d("pouet","No path.");
						Toast.makeText(this, "No path", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Log.d("pouet", "Do nothing.");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
