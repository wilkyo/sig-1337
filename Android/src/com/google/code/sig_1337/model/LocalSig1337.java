package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.code.sig_1337.model.handler.LocalHandler;
import com.google.code.sig_1337.model.xml.Graph;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.structure.IStructure;

/**
 * Local version of the sig.<br/>
 * Loaded from map.xml.
 */
public class LocalSig1337 extends Sig1337Base implements ILocalSig1337 {

	/**
	 * Graph.
	 */
	private final IGraph graph;

	/**
	 * Default constructor.
	 */
	public LocalSig1337() {
		super();
		graph = new Graph();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGraph getGraph() {
		return graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(InputStream is) throws XmlPullParserException,
			IOException, InterruptedException {
		clear();
		try {
			XmlPullParser p = Xml.newPullParser();
			p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			p.setInput(is, null);
			new LocalHandler<ILocalSig1337>().handle(this, p);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStructure getStructure(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IItineraire getItineraire(double x1, double y1, double x2, double y2) {
		// TODO Auto-generated method stub
		return null;
	}

}