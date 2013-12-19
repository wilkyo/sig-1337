package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.google.code.sig_1337.model.handler.RemoteHandler;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.Route;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.route.Routes;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.remote.AsyncTaskGetItineraire;
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
				String s = (String) jsonObject.get("id");
				if (s != null && !s.equals("")) {
					res = Long.parseLong(s);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res = -1;
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
	public IRoutes getItineraire(IBuilding start, IBuilding end) {
		Log.v("pouet", "getItineraire");
		IRoutes res = null;
		try {
			AsyncTaskGetItineraire task = new AsyncTaskGetItineraire(serverIP,
					start.getId(), end.getId());
			task.execute();
			String json = task.get(3, TimeUnit.SECONDS);
			if (json != null && json.length() > 0) {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(json);
				JSONArray jsonArray = (JSONArray) obj;
				res = new Routes();
				for (Object object : jsonArray) {
					JSONObject jsonObject = (JSONObject) object;
					JSONArray coordinates = (JSONArray) jsonObject
							.get("coordinates");
					List<IPoint> points = new ArrayList<IPoint>();
					for (Object object2 : coordinates) {
						JSONArray coords = (JSONArray) object2;
						double x = (Double) coords.get(0);
						double y = (Double) coords.get(1);
						Logger.getLogger("pouet").info(x + " " + y);
						points.add(new Point(x, y, x - bounds.getMinLon(), y
								- bounds.getMinLat()));
					}
					res.add(new Route(RouteType.Itineraire, points
							.toArray(new IPoint[points.size()])));
				}
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
		return res;
	}
}
