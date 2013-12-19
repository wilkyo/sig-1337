package com.google.code.sig_1337.model.xml;

import java.util.concurrent.CopyOnWriteArrayList;

import com.google.code.sig_1337.model.graph.Node;

public class Voisins extends CopyOnWriteArrayList<Node> implements IVoisins {

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
