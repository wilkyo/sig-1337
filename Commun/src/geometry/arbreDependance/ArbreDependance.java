package geometry.arbreDependance;

import geometry.model.OrderedSegment;
import geometry.model.Point;
import geometry.model.Polygone;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

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
		ArbreDependance ad = new ArbreDependance(bounds,
				polygons.toArray(new StructurePolygon[polygons.size()]), map,
				graph);
		// Random permutation.
		randomize(polygons);
		// For each polygon.
		for (StructurePolygon sp : polygons) {
			// Add the polygon to the map.
			Polygone p = sp.polygon;
			List<OrderedSegment> segments = randomize(p);
			// For each segment.
			for (OrderedSegment s : segments) {
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
			labels(sp.structure, t);
		}
		return ad;
	}

	/**
	 * Page 137.
	 */
	public static Trapezoid[] followSegment(SearchGraph d, OrderedSegment s) {
		// Left point and right point of the segment.
		Point p = s.debut;
		Point q = s.fin;
		// Find d0.
		Trapezoid d0 = d.locate(p, s);
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

	private static List<OrderedSegment> randomize(Polygone polygon) {
		List<OrderedSegment> l = new ArrayList<OrderedSegment>();
		Point[] pts = polygon.points;
		for (int i = 0; i < pts.length - 1; ++i) {
			Point p = pts[i];
			Point q = pts[i + 1];
			if (q.x < p.x) {
				Point tmp = p;
				p = q;
				q = tmp;
			}
			l.add((int) (Math.random() * l.size()), new OrderedSegment(p, q));
		}
		Point p = pts[pts.length - 1];
		Point q = pts[0];
		if (q.x < p.x) {
			Point tmp = p;
			p = q;
			q = tmp;
		}
		l.add((int) (Math.random() * l.size()), new OrderedSegment(p, q));
		return l;
	}

	private static void labels(Structure structure, Trapezoid trapezoid) {
		if (trapezoid != null && !trapezoid.colored) {
			trapezoid.structure = structure;
			trapezoid.colored = true;
			labels(structure, trapezoid.leftTopNeighbor);
			labels(structure, trapezoid.leftBottomNeighbor);
			labels(structure, trapezoid.rightTopNeighbor);
			labels(structure, trapezoid.rightBottomNeighbor);
		}
	}

	private Rectangle2D.Double bounds;
	private StructurePolygon[] polygons;
	private TrapezoidalMap map;
	private SearchGraph graph;

	public ArbreDependance(Rectangle2D.Double bounds,
			StructurePolygon[] polygons, TrapezoidalMap map, SearchGraph graph) {
		super();
		this.bounds = bounds;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(map.toString());
		sb.append('\n');
		sb.append(graph.toString());
		return sb.toString();
	}

	public void out(File file) throws IOException {
		BufferedImage img = new BufferedImage((int) (bounds.getWidth() * 200000),
				(int) (bounds.getHeight() * 200000), BufferedImage.TYPE_INT_ARGB);
		out(img);
		ImageIO.write(img, "png", file);
	}

	public void out(BufferedImage img) {
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		map.out(bounds, img, g2d);
	}

}
