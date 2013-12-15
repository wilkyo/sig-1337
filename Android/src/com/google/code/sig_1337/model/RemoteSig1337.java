package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
	 */
	@Override
	public int getStructureId(double x, double y) {
		int res = -1;
		try {
			String json = new AsyncTaskGetLocation(serverIP, x, y).get();
			if (json != null && json.length() > 0) {
				String[] jsonElements = json.split("\",\"");
				if (jsonElements != null && jsonElements.length > 0) {
					for (String jsonElement : jsonElements) {
						jsonElement = jsonElement.replace("\"", "");
						String[] jsonValues = jsonElement.split(":");
						if (jsonValues != null && jsonValues.length == 2 && jsonValues[0].equals("id")) {
							res = Integer.parseInt(jsonValues[1]);
							break;
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStructureName(double x, double y) {
		String res = "";
		try {
			String json = new AsyncTaskGetLocation(serverIP, x, y).get();
			if (json != null && json.length() > 0) {
				String[] jsonElements = json.split("\",\"");
				if (jsonElements != null && jsonElements.length > 0) {
					for (String jsonElement : jsonElements) {
						jsonElement = jsonElement.replace("\"", "");
						String[] jsonValues = jsonElement.split(":");
						if (jsonValues != null && jsonValues.length == 2 && jsonValues[0].equals("name")) {
							res = jsonValues[1];
							break;
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
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
