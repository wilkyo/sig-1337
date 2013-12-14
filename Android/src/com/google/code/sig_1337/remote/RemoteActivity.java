package com.google.code.sig_1337.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

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

	// TODO
	private static final String SERVER_IP = "192.168.1.4";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ISig1337 getSig1337() {
		if (sig == null) {
			sig = new RemoteSig1337(SERVER_IP);
		}
		return sig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSig1337() {
		new AsyncTaskGetMap(this, SERVER_IP).execute();
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
