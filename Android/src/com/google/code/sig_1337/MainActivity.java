package com.google.code.sig_1337;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;

import com.google.code.sig_1337.model.xml.Sig1337;
import com.google.code.sig_1337.model.xml.Sig1337.Format;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Logger l = Logger.getLogger("Pouet");
		try {
			Resources r = getResources();
			Sig1337 s = Sig1337.create(r.openRawResource(R.raw.exemple));
			l.info(new Format().toString(s));
		} catch (Exception e) {
			l.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
