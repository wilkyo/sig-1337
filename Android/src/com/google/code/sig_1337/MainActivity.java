package com.google.code.sig_1337;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;

import com.google.code.sig_1337.model.xml.Sig1337;
import com.google.code.sig_1337.model.xml.Sig1337.Format;

public class MainActivity extends Activity {

	/**
	 * View.
	 */
	private GLSurfaceView view;

	/**
	 * Renderer.
	 */
	private SigRenderer renderer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		renderer = new SigRenderer(this);
		view = new GLSurfaceView(this);
		view.setRenderer(renderer);
		setContentView(view);
		Logger l = Logger.getLogger("Pouet");
		try {
			Resources r = getResources();
			Sig1337 s = Sig1337.parse(r.openRawResource(R.raw.exemple));
			l.info(new Format().toString(s));
			renderer.setSig(s);
		} catch (Exception e) {
			l.log(Level.SEVERE, "", e);
		}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		view.onPause();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		view.onResume();
	}

}
