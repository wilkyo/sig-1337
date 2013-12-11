package com.google.code.sig_1337.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import com.google.code.sig_1337.LocalActivity;
import com.google.code.sig_1337.MockLocationListener;
import com.google.code.sig_1337.MyLocationListener;
import com.google.code.sig_1337.R;
import com.google.code.sig_1337.SigView;
import com.google.code.sig_1337.model.xml.Sig1337;

public class RemoteActivity extends Activity {

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

	// TODO
	private static final String SERVER_IP = "192.168.1.4";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote);
		// GPS.
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MockLocationListener(); // TODO
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
		final Logger l = Logger.getLogger("Pouet");
		//
		view = new SigView(this, locationListener);
		setContentView(view);
		// ((Button) findViewById(R.id.button1)).setClickable(true);
		try {
			sig = new Sig1337();
			view.setSig(sig);
			new LongRunningGetIO(this, SERVER_IP).execute();
		} catch (Exception e) {
			l.log(Level.SEVERE, "", e);
		}
	}

	public void createMap(String xml) {
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		try {
			Sig1337.parse(is, sig);
		} catch (InterruptedException e) {
			Log.wtf(LocalActivity.class.getSimpleName(), e.getMessage());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remote, menu);
		return true;
	}

}
