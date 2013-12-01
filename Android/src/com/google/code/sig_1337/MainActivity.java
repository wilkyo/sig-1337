package com.google.code.sig_1337;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	/**
	 * View.
	 */
	private SigView view;

	/**
	 * GPS listener.
	 */
	private MyLocationListener locationListener;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final MediaPlayer mp = MediaPlayer.create(MainActivity.this,
				R.raw.bouton);
		((Button) findViewById(R.id.local))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						mp.setVolume(1.0f, 1.0f);
						mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mp.start();
						startActivity(new Intent(MainActivity.this,
								LocalActivity.class));
					}
				});
		((Button) findViewById(R.id.remote))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								RemoteActivity.class));
					}
				});
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
