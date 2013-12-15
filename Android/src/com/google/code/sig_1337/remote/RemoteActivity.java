package com.google.code.sig_1337.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.util.Log;

import com.google.code.sig_1337.ActivityBase;
import com.google.code.sig_1337.LocalActivity;
import com.google.code.sig_1337.model.ISig1337;
import com.google.code.sig_1337.model.RemoteSig1337;

public class RemoteActivity extends ActivityBase {

	/**
	 * Sig.
	 */
	private static ISig1337 sig;

	private String serverIP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.serverIP = this.getIntent().getStringExtra("serverIP");
		Log.v("pouet", serverIP);
		super.onCreate(savedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISig1337 getSig1337() {
		if (sig == null) {
			sig = new RemoteSig1337(serverIP);
		}
		return sig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSig1337() {
		new AsyncTaskGetMap(this, serverIP).execute();
	}

	public void createMap(String xml) {
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		try {
			sig.load(is);
		} catch (InterruptedException e) {
			Log.wtf(LocalActivity.class.getSimpleName(), e.getMessage());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
