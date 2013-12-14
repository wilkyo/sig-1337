package com.google.code.sig_1337.model.xml;

import java.util.concurrent.CopyOnWriteArrayList;

public class Voisins extends CopyOnWriteArrayList<IPoint> implements IVoisins {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		String res = "";
		for (IPoint point : this) {
			res += "x=" + point.getLongitude() + " y=" + point.getLatitude() + "\n";
		}
		return res;
	}
}
