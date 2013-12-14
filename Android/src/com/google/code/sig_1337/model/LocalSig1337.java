package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.code.sig_1337.itineraire.Itineraire;
import com.google.code.sig_1337.model.handler.LocalHandler;
import com.google.code.sig_1337.model.xml.Graph;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.Tree;
import com.google.code.sig_1337.model.xml.structure.IBuilding;

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
	 * Tree.
	 */
	private final Tree tree;

	/**
	 * Default constructor.
	 */
	public LocalSig1337() {
		super();
		graph = new Graph();
		tree = new Tree(null);
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
	public Tree getTree() {
		return tree;
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
	public int getStructureId(double x, double y) {
		return tree.locate(x, y).getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStructureName(double x, double y) {
		return tree.locate(x, y).getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IItineraire getItineraire(IBuilding start, IBuilding end) {
		return Itineraire.CalculItineraire(start, end, graph);
	}
}
