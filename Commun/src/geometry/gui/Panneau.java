package geometry.gui;

import geometry.model.Point;
import geometry.model.Polygone;
import geometry.model.Segment;

import java.awt.*;

import javax.swing.*;

import java.util.*;

public class Panneau extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Segment> segments = new ArrayList<Segment>();
	ArrayList<Polygone> polyedres = new ArrayList<Polygone>();

	public static final int TAILLEPOINT = 4;
	private Point[] bounds;

	public Panneau() {
		super();
		this.bounds = new Point[] { new Point(0, 0), new Point(400, 300) };
	}

	public Panneau(Point[] bounds) {
		super();
		this.bounds = bounds;
	}

	public void add(Point point) {
		points.add(point);
	}

	public void add(Segment segment) {
		segments.add(segment);
	}

	public void add(Polygone polyedre) {
		polyedres.add(polyedre);
	}

	private void draw(Point point, Graphics g) {
		g.drawOval((int) point.x - TAILLEPOINT, (int) point.y - TAILLEPOINT,
				TAILLEPOINT, TAILLEPOINT);
		g.setColor(Color.red);
		g.fillOval((int) point.x - TAILLEPOINT, (int) point.y - TAILLEPOINT,
				TAILLEPOINT, TAILLEPOINT);
		g.setColor(Color.black);
	}

	private void draw(Segment segment, Graphics g) {
		g.drawLine((int) segment.debut.x, (int) segment.debut.y,
				(int) segment.fin.x, (int) segment.fin.y);

	}

	private void draw(Polygone polygone, Graphics g) {
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;
		int taille = polygone.points.length;
		int[] x = new int[taille];
		int[] y = new int[taille];

		for (int i = 0; i < taille; i++) {
			x[i] = (int) ((polygone.points[i].x - bounds[0].x)
					* this.getWidth() / w);
			y[i] = (int) ((polygone.points[i].y - bounds[0].y)
					* this.getHeight() / h);
		}

		g.drawPolygon(x, y, taille);
		g.setColor(Color.yellow);
		g.fillPolygon(x, y, taille);
		g.setColor(Color.black);
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (Point p : points)
			draw(p, g);

		for (Segment s : segments)
			draw(s, g);

		for (Polygone poly : polyedres)
			draw(poly, g);
	}
}
