package geometry.arbreDependance;

import geometry.model.Point;
import geometry.model.Polygone;
import geometry.model.Segment;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ArbreDependance {

	public static ArbreDependance create(List<Polygone> polygons) {
		Point topLeft = null;
		Point bottomRight = null;
		List<Segment> l = new ArrayList<Segment>();
		for (Polygone p : polygons) {
			// Add segments.
			Point[] points = p.points;
			for (int i = 0; i < points.length - 1; ++i) {
				l.add(new Segment(points[i], points[i + 1]));
			}
			l.add(new Segment(points[points.length - 1], points[0]));
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
				bottomRight.x - topLeft.x, bottomRight.y - topLeft.y), l);
	}

	private static ArbreDependance trapezoidalMap(Rectangle2D.Double bounds,
			List<Segment> segments) {
		SearchGraph graph = new SearchGraph(bounds);
		TrapezoidalMap map = graph.getMap();
		ArbreDependance ad = new ArbreDependance();
		// Random permutation.
		{
			List<Segment> tmp = new ArrayList<Segment>(segments);
			segments.clear();
			for (Segment s : tmp) {
				segments.add((int) (Math.random() * segments.size()), s);
			}
			tmp.clear();
		}
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
			}
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
		while (q.x > dj.right.x) {
			// If dj.right is above the segment.
			if (s.auDessus(dj.right)) {
				dj = dj.rightBottomNeighbor;
			} else {
				dj = dj.rightTopNeighbor;
			}
			// Add dj to the list.
			l.add(dj);
		}
		return l.toArray(new Trapezoid[l.size()]);
	}

}
