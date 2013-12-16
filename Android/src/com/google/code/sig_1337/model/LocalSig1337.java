package com.google.code.sig_1337.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.code.sig_1337.itineraire.Itineraire;
import com.google.code.sig_1337.model.handler.LocalHandler;
import com.google.code.sig_1337.model.xml.ArbreDecision;
import com.google.code.sig_1337.model.xml.ArbreDecision.BoundingBox;
import com.google.code.sig_1337.model.xml.Graph;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.Route;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.route.Routes;
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
	private final ArbreDecision tree;

	/**
	 * Default constructor.
	 */
	public LocalSig1337() {
		super();
		graph = new Graph();
		tree = new ArbreDecision(null);
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
	public ArbreDecision getTree() {
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
	public long getStructureId(double x, double y) {
		BoundingBox bb = tree.locate(x, y);
		if (bb == null) {
			return -1;
		} else {
			return bb.getId();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStructureName(double x, double y) {
		BoundingBox bb = tree.locate(x, y);
		if (bb == null) {
			return null;
		} else {
			return bb.getName();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoutes getItineraire(IBuilding start, IBuilding end) {
		IItineraire it = Itineraire.CalculItineraire(start, end, graph);
		if (it == null) {
			return null;
		} else {
			IRoutes routes = new Routes();
			routes.add(new Route(RouteType.Itineraire, it.toArray(new IPoint[it
					.size()])));
			return routes;
		}
	}
}
