package com.google.code.sig_1337.model.graph;

import java.util.Arrays;
import java.util.LinkedList;

import com.google.code.sig_1337.model.xml.IPoint;

public class Neighbors extends LinkedList<Node> {

	private static final long serialVersionUID = 1L;

	public Neighbors() {
		super();
	}

	@Override
	public String toString() {
		return Arrays.toString(this.toArray(new IPoint[0]));
	}

}
