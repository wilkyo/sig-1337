package com.google.code.sig_1337;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import com.google.code.sig_1337.itineraire.Itineraire;
import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.LocalSig1337;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.Voisins;
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
}
