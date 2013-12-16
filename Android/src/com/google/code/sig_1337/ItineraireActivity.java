package com.google.code.sig_1337;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
		Parcelable[] p = getIntent().getExtras().getParcelableArray("Nombat");
		ItineraireItem[] ii = new ItineraireItem[p.length];
		for (int i = 0; i < p.length; ++i) {
			ii[i] = (ItineraireItem) p[i];
		}
		ArrayAdapter<ItineraireItem> adapter = new ArrayAdapter<ItineraireItem>(
				this, android.R.layout.simple_spinner_item, ii);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_source.setAdapter(adapter);
		spinner_target.setAdapter(adapter);
		spinner_target.setSelection(1);
	}

	public void valide(View v) {
		Intent i = new Intent("Routage");
		i.putExtra("Source", (ItineraireItem) spinner_source
				.getItemAtPosition(spinner_source.getSelectedItemPosition()));
		i.putExtra("Target", (ItineraireItem) spinner_target
				.getItemAtPosition(spinner_target.getSelectedItemPosition()));
		setResult(1, i);
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent("Routage");
		setResult(RESULT_CANCELED, i);
		super.onBackPressed();
	}

	public static class ItineraireItem implements Comparable<ItineraireItem>,
			Parcelable {

		/**
		 * Creator.
		 */
		public static final Parcelable.Creator<ItineraireItem> CREATOR = new Parcelable.Creator<ItineraireItem>() {
			public ItineraireItem createFromParcel(Parcel in) {
				return new ItineraireItem(in.readLong(), in.readString());
			}

			public ItineraireItem[] newArray(int size) {
				return new ItineraireItem[size];
			}
		};

		/**
		 * Structure identifier.
		 */
		public long id;

		/**
		 * Structure name.
		 */
		public String name;

		/**
		 * Initializing constructor.
		 * 
		 * @param id
		 *            structure identifier.
		 * @param name
		 *            structure name.
		 */
		public ItineraireItem(long id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(ItineraireItem other) {
			return name.compareTo(other.name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int describeContents() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeLong(id);
			out.writeString(name);
		}

	}

}
