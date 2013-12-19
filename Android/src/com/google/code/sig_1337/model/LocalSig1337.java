package com.google.code.sig_1337.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.google.code.sig_1337.LocalActivity;
import com.google.code.sig_1337.R;
import com.google.code.sig_1337.itineraire.Itineraire;
import com.google.code.sig_1337.model.graph.DijkstraGraph;
import com.google.code.sig_1337.model.graph.Node;
import com.google.code.sig_1337.model.handler.LocalHandler;
import com.google.code.sig_1337.model.xml.ArbreDecision;
import com.google.code.sig_1337.model.xml.ArbreDecision.BoundingBox;
import com.google.code.sig_1337.model.xml.Graph;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.route.IRoutes;
import com.google.code.sig_1337.model.xml.route.Route;
import com.google.code.sig_1337.model.xml.route.RouteType;
import com.google.code.sig_1337.model.xml.route.Routes;
import com.google.code.sig_1337.model.xml.structure.IBuilding;
import com.google.code.sig_1337.model.xml.structure.IStructure;

/**
 * Local version of the sig.<br/>
 * Loaded from map.xml.
 */
public class LocalSig1337 extends Sig1337Base implements ILocalSig1337 {

	private static final int GRAPH = R.raw.graph;
	private Context mContext;
	/**
	 * Graph.
	 */
	private final IGraph graph;
	/**
	 * The real graph.
	 */
	private DijkstraGraph dijkstra;

	/**
	 * Tree.
	 */
	private final ArbreDecision tree;

	/**
	 * Default constructor.
	 */
	public LocalSig1337(LocalActivity mContext) {
		super();
		this.mContext = mContext;
		graph = new Graph();
		// TODO Ouais ouais, je le charge au démarrage de l'activité...
		try {
			BufferedReader f = new BufferedReader(new InputStreamReader(
					mContext.getResources().openRawResource(GRAPH)));
			dijkstra = new DijkstraGraph(f.readLine(), bounds);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.v("graph", dijkstra.toString());
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
		IStructure s = getStructure(getStructureId(x, y));
		if (s == null) {
			return null;
		} else {
			return s.getName();
		}
	}

	private static final boolean OLD_VERSION = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoutes getItineraire(IBuilding start, IBuilding end) {
		if (OLD_VERSION) {
			IItineraire it = Itineraire.CalculItineraire(start, end, graph);
			if (it == null) {
				return null;
			} else {
				IRoutes routes = new Routes();
				routes.add(new Route(RouteType.Itineraire, it
						.toArray(new IPoint[0])));
				return routes;
			}
		} else {
			List<Node> path = dijkstra.dijkstra(start, end);
			dijkstra.clean();
			Toast.makeText(mContext,
					path.isEmpty() ? "No Path" : "There is a Path...",
					Toast.LENGTH_LONG).show();
			IRoutes routes = new Routes();
			List<IPoint> points = new ArrayList<IPoint>();
			for (Node n : path) {
				points.add(new Point(n.getLongitude(), n.getLatitude(), n
						.getLongitude() - bounds.getMinLon(), n.getLatitude()
						- bounds.getMinLat()));
			}
			routes.add(new Route(RouteType.Itineraire, points
					.toArray(new IPoint[points.size()])));
			return routes;
		}
	}
}
