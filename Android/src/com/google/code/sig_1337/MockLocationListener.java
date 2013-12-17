package com.google.code.sig_1337;

public class MockLocationListener extends MyLocationListener {

	public double getLatitude() {
		if (super.getLatitude() == 0.0)
			return 47.843548d;
		else
			return super.getLatitude();
	}

	public double getLongitude() {
		if (super.getLongitude() == 0.0)
			return 1.934516d;
		else
			return super.getLongitude();
	}

}
