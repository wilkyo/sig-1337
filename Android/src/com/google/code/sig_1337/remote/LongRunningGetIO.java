package com.google.code.sig_1337.remote;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.AsyncTask;

public class LongRunningGetIO extends AsyncTask<Void, Void, String> {

	private Context mContext;
	private String serverIP;

	public LongRunningGetIO(Context mContext, String serverIP) {
		this.mContext = mContext;
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
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return text;
	}

	protected void onPostExecute(String response) {
		if (response != null) {
			System.out.println("Length: " + response.length());
			if (response.length() >= 200)
				((RemoteActivity) mContext).createMap(response);
		}
	}
}
