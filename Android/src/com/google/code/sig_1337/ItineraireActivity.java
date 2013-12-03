package com.google.code.sig_1337;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ItineraireActivity extends Activity {
	private Spinner spinner_source;
	private Spinner spinner_target;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itineraire);
		
		spinner_source = (Spinner) findViewById(R.id.spinner_source);
		spinner_target = (Spinner) findViewById(R.id.spinner_target);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getIntent().getExtras().getStringArray("Nombat"));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner_source.setAdapter(adapter);
		spinner_target.setAdapter(adapter);
	}

	
	public void valide(View v) {
		Intent i = new Intent("Routage");
		i.putExtra("Source", (String)spinner_source.getItemAtPosition(spinner_source.getSelectedItemPosition()));
		i.putExtra("Target", (String)spinner_target.getItemAtPosition(spinner_target.getSelectedItemPosition()));
		setResult(1, i);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
}
