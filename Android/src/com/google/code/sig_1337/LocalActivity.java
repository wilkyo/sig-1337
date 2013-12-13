package com.google.code.sig_1337;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.LocalSig1337;
import com.google.code.sig_1337.model.xml.structure.IBuilding;

public class LocalActivity extends ActivityBase {

	/**
	 * Map.
	 */
	private static final int MAP = R.raw.map;

	/**
	 * Sig.
	 */
	private static ISig1337 sig;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISig1337 getSig1337() {
		if (sig == null) {
			sig = new LocalSig1337();
		}
		return sig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSig1337() {
		Intent i = new Intent(this, Sig1337Service.class);
		startService(i);
	}

	public static class Sig1337Service extends IntentService {

		public Sig1337Service() {
			super("Sig1337");
		}

		protected void onHandleIntent(Intent workIntent) {
			try {
				sig.load(getResources().openRawResource(MAP));
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
		if (item.getItemId() == R.id.itineraire) {
			String[] liste = {};
			ArrayList<String> l = new ArrayList<String>();
			for (IBuilding building : sig.getGraphics().getBuildings()) {
				String name = building.getName();
				if (name != null && !name.equals("") && !l.contains(name))
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
				Log.d("pouet",
						"Do the routage from " + data.getStringExtra("Source")
								+ " to " + data.getStringExtra("Target"));
			} else {
				Log.d("pouet", "Do nothing.");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
