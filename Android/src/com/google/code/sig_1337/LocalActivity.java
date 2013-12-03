package com.google.code.sig_1337;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.code.sig_1337.model.xml.IBuilding;
import com.google.code.sig_1337.model.xml.Sig1337;

public class LocalActivity extends Activity {

	/**
	 * Sig.
	 */
	private static Sig1337 sig;

	/**
	 * View.
	 */
	private SigView view;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	/**
	 * {@inheritDoc}
	 */
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
			sig = new Sig1337();
			view.setSig(sig);
			Intent i = new Intent(this, Sig1337Service.class);
			startService(i);
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
		getMenuInflater().inflate(R.menu.local, menu);
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

	public static class Sig1337Service extends IntentService {

		public Sig1337Service() {
			super("Sig1337");
		}

		protected void onHandleIntent(Intent workIntent) {
			try {
				Resources r = getResources();
				Sig1337.parse(r.openRawResource(R.raw.map), sig);
			} catch (InterruptedException e) {
				Logger l = Logger
						.getLogger(LocalActivity.class.getSimpleName());
				l.log(Level.SEVERE, e.getMessage(), e);
			} catch (Exception e) {
				Logger l = Logger
						.getLogger(LocalActivity.class.getSimpleName());
				l.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean action = false;
		if(item.getItemId() == R.id.itineraire) {
			String [] liste = {};
			ArrayList<String> l = new ArrayList<String>();
			for (IBuilding building : sig.getGraphics().getBuildings()) {
				String name = building.getName();
				if(name != null && !name.equals("") && !l.contains(name))
					 l.add(building.getName());
			}			
			Intent i = new Intent(this,ItineraireActivity.class);
			liste = l.toArray(liste);
			Arrays.sort(liste);
			i.putExtra("Nombat", liste);
			startActivityForResult(i, 1);
		}
		return action;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data.getAction().equals("Routage")) {
			if(requestCode == resultCode) {
				Log.d("pouet", "Do the routage from " + data.getStringExtra("Source") + " to " + data.getStringExtra("Target"));
			}
			else {
				Log.d("pouet", "Do nothing.");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
