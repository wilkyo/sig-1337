package geometry.gui;

import geometry.arbreDependance.ArbreDependance;
import geometry.arbreDependance.Trapezoid;
import geometry.arbreDependance.TrapezoidalMap;
import geometry.model.Point;
import geometry.model.Polygone;
import geometry.model.Segment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Segment> segments = new ArrayList<Segment>();
	ArrayList<Polygone> polyedres = new ArrayList<Polygone>();
	ArrayList<ArbreDependance> arbres = new ArrayList<ArbreDependance>();

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

	public void add(ArbreDependance arbre) {
		arbres.add(arbre);
	}

	private void draw(Point point, Graphics g) {
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;
		double x = ((point.x - bounds[0].x) * (getWidth() / w));
		double y = ((point.y - bounds[0].y) * (getHeight() / h));
		g.setColor(Color.red);
		g.fillOval((int) x - TAILLEPOINT, (int) y - TAILLEPOINT,
				TAILLEPOINT * 2, TAILLEPOINT * 2);
		g.setColor(Color.black);
		g.drawOval((int) x - TAILLEPOINT, (int) y - TAILLEPOINT,
				TAILLEPOINT * 2, TAILLEPOINT * 2);
	}

	private void draw(Segment segment, Graphics g) {
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;
		g.setColor(Color.black);
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

		g.setColor(Color.yellow);
		g.fillPolygon(x, y, taille);
		g.setColor(Color.black);
		g.drawPolygon(x, y, taille);
	}

	private void draw(ArbreDependance arbre, Graphics g) {
		// Map.
		Graphics2D g2d = (Graphics2D) g;
		Composite c = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		INDEX = 0;
		TrapezoidalMap map = arbre.getMap();
		for (Trapezoid t : map.getTrapezoids()) {
			draw(t, g);
			++INDEX;
		}
		g2d.setComposite(c);

		// Structures.
		for (Trapezoid t : map.getTrapezoids()) {
			if (t.bottom != null) {
				draw(t.bottom, g);
			}
			if (t.top != null) {
				draw(t.top, g);
			}
		}
		/*
		 * StructurePolygon[] polygons = arbre.getPolygons(); for (int i = 0; i
		 * < polygons.length; ++i) { draw2(polygons[i].polygon, g); }
		 */
	}

	public static Color[] COLORS;
	public static int INDEX = 0;

	static {
		COLORS = new Color[256];
		for (int i = 0; i < COLORS.length; ++i) {
			COLORS[i] = new Color((int) (Math.random() * 128 + 64),
					(int) (Math.random() * 128 + 64),
					(int) (Math.random() * 128 + 64));
		}
	}

	private void draw(Trapezoid trapezoid, Graphics g) {
		double w = bounds[1].x - bounds[0].x;
		double h = bounds[1].y - bounds[0].y;

		Point left = trapezoid.left;
		Point right = trapezoid.right;
		Segment top = trapezoid.top;
		Segment bottom = trapezoid.bottom;
		g.setColor(COLORS[INDEX]);
		int[] x = new int[4];
		int[] y = new int[4];
		// Left.
		x[0] = 0;
		if (left != null) {
			x[0] = (int) (left.x * (getWidth() / w));
		}
		x[1] = x[0];
		y[0] = 0;
		if (top != null && left != null) {
			y[0] = (int) (top.getY(left.x) * (getHeight() / h));
		}
		y[1] = (int) (getHeight() * (getHeight() / h));
		if (bottom != null && left != null) {
			y[1] = (int) (bottom.getY(left.x) * (getHeight() / h));
		}
		// Right.
		x[2] = (int) (getWidth() * (getWidth() / w));
		if (right != null) {
			x[2] = (int) (right.x * (getWidth() / w));
		}
		x[3] = x[2];
		y[2] = (int) (getHeight() * (getHeight() / h));
		if (bottom != null && right != null) {
			y[2] = (int) (bottom.getY(right.x) * (getHeight() / h));
		}
		y[3] = 0;
		if (top != null && right != null) {
			y[3] = (int) (top.getY(right.x) * (getHeight() / h));
		}
		g.fillPolygon(x, y, 4);
		g.setColor(Color.black);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clipRect(0, 0, getWidth(), getHeight());
		for (Point p : points)
			draw(p, g);

		for (Segment s : segments)
			draw(s, g);

		for (Polygone poly : polyedres)
			draw(poly, g);

		for (ArbreDependance arbre : arbres)
			draw(arbre, g);
	}
}
