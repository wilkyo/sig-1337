package geometry.arbreDependance;

import geometry.model.Point;
import geometry.model.Polygone;
import geometry.model.Segment;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import data.model.structure.Building;
import data.model.structure.Structure;

public class ArbreDependance {

	public static interface Callback {
		public void action(ArbreDependance ad);
	}

	public static ArbreDependance create(Collection<Building> collection) {
		return create(collection, null);
	}

	public static ArbreDependance create(Collection<Building> collection,
			Callback callback) {
		List<StructurePolygon> l = new ArrayList<StructurePolygon>();
		for (Structure s : collection) {
			l.add(new StructurePolygon(s));
		}
		return create3(l, callback);
	}

	public static ArbreDependance create3(List<StructurePolygon> polygons,
			Callback callback) {
		Point topLeft = null;
		Point bottomRight = null;
		for (StructurePolygon sp : polygons) {
			Polygone p = sp.polygon;
			// Resize bounding box.
			Point[] bounds = p.getBounds();
			if (topLeft == null) {
				topLeft = new Point(bounds[0]);
				bottomRight = new Point(bounds[1]);
			} else {
				topLeft.x = Math.min(topLeft.x, bounds[0].x);
				topLeft.y = Math.min(topLeft.y, bounds[0].y);
				bottomRight.x = Math.max(bottomRight.x, bounds[1].x);
				bottomRight.y = Math.max(bottomRight.y, bounds[1].y);
			}
		}
		return trapezoidalMap(new Rectangle2D.Double(topLeft.x, topLeft.y,
				bottomRight.x - topLeft.x, bottomRight.y - topLeft.y),
				polygons, callback);
	}

	private static ArbreDependance trapezoidalMap(Rectangle2D.Double bounds,
			List<StructurePolygon> polygons, Callback callback) {
		SearchGraph graph = new SearchGraph(bounds);
		TrapezoidalMap map = graph.getMap();
		ArbreDependance ad = new ArbreDependance(
				polygons.toArray(new StructurePolygon[polygons.size()]), map,
				graph);
		// Random permutation.
		randomize(polygons);
		// For each polygon.
		for (StructurePolygon sp : polygons) {
			// Add the polygon to the map.
			Polygone p = sp.polygon;
			List<Segment> segments = randomize(p);
			// For each segment.
			for (Segment s : segments) {
				// Find the set of trapezoids intersecting the segment.
				Trapezoid[] l = followSegment(graph, s);
				// Only one trapezoid.
				if (l.length == 1) {
					// Replace the trapezoid in the map.
					SplitTrapezoid split = map.split(l[0], s);
					// Replace the trapezoid in the graph.
					l[0].leaf.split(split);
				} else {
					// More than one.
					Split2Trapezoid split = map.split2(l, s);
					// Replace the trapezoids in the graph.
					graph.split2(split);
				}
				if (callback != null) {
					callback.action(ad);
				}
			}
			// Labels the trapezoids.
			Trapezoid t = graph.locate(p.points[0]);
			labelsLeft(sp.structure, t);
			labelsRight(sp.structure, t);
		}
		return ad;
	}

	/**
	 * Page 137.
	 */
	public static Trapezoid[] followSegment(SearchGraph d, Segment s) {
		// Left point and right point of the segment.
		Point p = new Point(s.debut);
		Point q = new Point(s.fin);
		if (q.x < p.x) {
			Point tmp = q;
			q = p;
			p = tmp;
		}
		// Find d0.
		Trapezoid d0 = d.locate(p);
		List<Trapezoid> l = new ArrayList<Trapezoid>();
		l.add(d0);
		// While q lies at the right of dj.
		Trapezoid dj = d0;
		while (dj != null && dj.right != null && q.x > dj.right.x) {
			// If dj.right is above the segment.
			if (s.auDessus(dj.right)) {
				dj = dj.rightBottomNeighbor;
			} else {
				dj = dj.rightTopNeighbor;
			}
			// Add dj to the list.
			if (dj != null) {
				l.add(dj);
			}
		}
		return l.toArray(new Trapezoid[l.size()]);
	}

	private static void randomize(List<StructurePolygon> polygons) {
		List<StructurePolygon> tmp = new ArrayList<StructurePolygon>(polygons);
		polygons.clear();
		for (StructurePolygon s : tmp) {
			polygons.add((int) (Math.random() * polygons.size()), s);
		}
		tmp.clear();
	}

	private static List<Segment> randomize(Polygone polygon) {
		List<Segment> l = new ArrayList<Segment>();
		Point[] pts = polygon.points;
		for (int i = 0; i < pts.length - 1; ++i) {
			Point p = pts[i];
			Point q = pts[i + 1];
			if (q.x < p.x) {
				Point tmp = p;
				p = q;
				q = tmp;
			}
			l.add((int) (Math.random() * l.size()), new Segment(p, q));
		}
		Point p = pts[pts.length - 1];
		Point q = pts[0];
		if (q.x < p.x) {
			Point tmp = p;
			p = q;
			q = tmp;
		}
		l.add((int) (Math.random() * l.size()), new Segment(p, q));
		return l;
	}

	private static void labelsLeft(Structure structure, Trapezoid trapezoid) {
		if (trapezoid != null) {
			trapezoid.structure = structure;
			labelsLeft(structure, trapezoid.leftTopNeighbor);
			labelsLeft(structure, trapezoid.leftBottomNeighbor);
		}
	}

	private static void labelsRight(Structure structure, Trapezoid trapezoid) {
		if (trapezoid != null) {
			trapezoid.structure = structure;
			labelsRight(structure, trapezoid.rightTopNeighbor);
			labelsRight(structure, trapezoid.rightBottomNeighbor);
		}
	}

	private StructurePolygon[] polygons;
	private TrapezoidalMap map;
	private SearchGraph graph;

	public ArbreDependance(StructurePolygon[] polygons, TrapezoidalMap map,
			SearchGraph graph) {
		super();
		this.polygons = polygons;
		this.map = map;
		this.graph = graph;
	}

	public StructurePolygon[] getPolygons() {
		return polygons;
	}

	public TrapezoidalMap getMap() {
		return map;
	}

	public SearchGraph getGraph() {
		return graph;
	}

	public void toXML(StringBuffer buff, String indent) {
		graph.toXML(buff, indent);
	}

}
