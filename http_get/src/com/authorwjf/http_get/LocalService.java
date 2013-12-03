package com.authorwjf.http_get;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		LocalService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return LocalService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * Retrieve the map and store it into the database using the content
	 * provider.
	 * 
	 * @param serverIP
	 */
	public void getMap(String serverIP) {
		new LongRunningGetIO(serverIP).execute();
	}

	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		private String serverIP;

		public LongRunningGetIO(String serverIP) {
			this.serverIP = serverIP;
		}

		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://" + serverIP
					+ ":8080/WebService/service/map");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String response) {
			if (response != null) {
				// Adding to the database
				ContentValues mNewValues = new ContentValues();
				mNewValues.put(MySQLiteHelper.COLUMN_NAME, "Map");
				mNewValues.put(MySQLiteHelper.COLUMN_DATA, response);
				Uri resUri = getContentResolver().insert(
						MyContentProvider.CONTENT_URI, mNewValues);
				System.out.println("URI: " + resUri);
				System.out.println("Length: " + response.length());
			}
		}
	}
}