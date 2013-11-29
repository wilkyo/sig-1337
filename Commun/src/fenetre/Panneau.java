package fenetre;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import base.Point;
import base.Polygone;
import base.Segment;

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
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;
		double x = ((point.x - bounds[0].x) * (getWidth() / w));
		double y = ((point.y - bounds[0].y) * (getHeight() / h));
		g.drawOval((int) x - TAILLEPOINT, (int) y - TAILLEPOINT,
				TAILLEPOINT * 2, TAILLEPOINT * 2);
		g.setColor(Color.red);
		g.fillOval((int) x - TAILLEPOINT, (int) y - TAILLEPOINT,
				TAILLEPOINT * 2, TAILLEPOINT * 2);
		g.setColor(Color.black);
	}

	private void draw(Segment segment, Graphics g) {
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;
		g.drawLine((int) ((segment.debut.x - bounds[0].x) * (getWidth() / w)),
				(int) ((segment.debut.y - bounds[0].y) * (getHeight() / h)),
				(int) ((segment.fin.x - bounds[0].x) * (getWidth() / w)),
				(int) ((segment.fin.y - bounds[0].y) * (getHeight() / h)));

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
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (Point p : points)
			draw(p, g);

		for (Segment s : segments)
			draw(s, g);

		for (Polygone poly : polyedres)
			draw(poly, g);
	}
}
