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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainSansService extends Activity {

	private String serverIP = "192.168.43.104";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_sans_service);
		((EditText) findViewById(R.id.ipAddress)).setText(serverIP);
		new LongRunningGetIO().execute();
	}

	public void getMap(View v) {
//		((Button) findViewById(R.id.button1)).setClickable(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_sans, menu);
		return true;
	}

	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

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
			serverIP = ((EditText) findViewById(R.id.ipAddress)).getText()
					.toString();
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
				System.out.println("Length: " + response.length());
				((EditText) findViewById(R.id.my_edit)).setText(response
						.length() >= 200 ? response.substring(0, 200)
						: response);
			}
			((Button) findViewById(R.id.button1)).setClickable(true);
		}
	}

}
