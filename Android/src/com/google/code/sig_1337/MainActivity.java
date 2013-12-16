package com.google.code.sig_1337;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.google.code.sig_1337.remote.RemoteActivity;

public class MainActivity extends Activity {

	/**
	 * View.
	 */
	private SigView view;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	private static final String SERVER_IP = "95.157.243.69";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((EditText) findViewById(R.id.ipAddress)).setText(SERVER_IP);
	}

	public void onClickLocal(View v) {
		startActivity(new Intent(this, LocalActivity.class));
	}

	public void onClickRemote(View v) {
		Intent i = new Intent(this, RemoteActivity.class);
		i.putExtra("serverIP", ((EditText) findViewById(R.id.ipAddress))
				.getText().toString());
		startActivity(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
