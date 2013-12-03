package com.authorwjf.http_get;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.authorwjf.http_get.LocalService.LocalBinder;

public class MainAvecService extends Activity {

	private String serverIP = "192.168.1.4";
	LocalService mService;
	boolean mBound = false;

	private static final String TAG = "Main";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (!mBound) {
			Log.d(TAG, "bind");
			Intent intent = new Intent(this, LocalService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		if (mBound) {
			Log.d(TAG, "unbind");
			unbindService(mConnection);
			mBound = false;
		}
		super.onDestroy();
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			Log.d(TAG, "onServiceConnected");
			mService.getMap(serverIP);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d(TAG, "onServiceDisconnected");
			mBound = false;
		}
	};
}