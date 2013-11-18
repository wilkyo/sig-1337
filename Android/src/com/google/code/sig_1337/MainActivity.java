package com.google.code.sig_1337;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.google.code.sig_1337.model.xml.Sig1337;
import com.google.code.sig_1337.model.xml.Sig1337.Format;

public class MainActivity extends Activity {

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
			Resources r = getResources();
			Sig1337 s = Sig1337.parse(r.openRawResource(R.raw.map));
			l.info(new Format().toString(s));
			view.setSig(s);
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
		getMenuInflater().inflate(R.menu.main, menu);
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

}
