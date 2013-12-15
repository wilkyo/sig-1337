package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.JsonReader;
import android.util.Log;
import android.util.Xml;

import com.google.code.sig_1337.model.handler.RemoteHandler;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.remote.AsyncTaskGetLocation;

/**
 * Remote sig.
 */
public class RemoteSig1337 extends Sig1337Base implements IRemoteSig1337 {

	private String serverIP;

	public RemoteSig1337(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(InputStream is) throws XmlPullParserException,
			IOException, InterruptedException {
		clear();
		try {
			XmlPullParser p = Xml.newPullParser();
			p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			p.setInput(is, null);
			new RemoteHandler<IRemoteSig1337>().handle(this, p);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @throws  
	 */
	@Override
	public long getStructureId(double x, double y) {
		Log.v("pouet", "getStructureId");
		long res = -1;
		try {
			AsyncTaskGetLocation task = new AsyncTaskGetLocation(serverIP, y, x);
			task.execute();
			String json = task.get(3, TimeUnit.SECONDS);
			if (json != null && json.length() > 0) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
				res = (Long) jsonObject.get("id");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		Log.v("pouet", "res = " + res);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStructureName(double x, double y) {
		Log.v("pouet", "getStructureName");
		String res = null;
		try {
			AsyncTaskGetLocation task = new AsyncTaskGetLocation(serverIP, y, x);
			task.execute();
			String json = task.get(3, TimeUnit.SECONDS);
			if (json != null && json.length() > 0) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
				res = (String) jsonObject.get("name");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		Log.v("pouet", "res = " + res);
		if (res != null && res.equals(""))
			res = null;
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IItineraire getItineraire(IBuilding start, IBuilding end) {
		// TODO Auto-generated method stub
		return null;
	}

}
