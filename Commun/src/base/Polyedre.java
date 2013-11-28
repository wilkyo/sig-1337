package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import triangulation.Triangulation;

/**
 * Classe contenant les informations d'un polyedre
 *
 */
public class Polyedre {
	/**
	 * Les points du polyedre
	 */
	public Point[] points;

	/**
	 * Construit un polyèdre à partir de la liste de points
	 * @param points
	 */
	public Polyedre(Point[] points) {
		super();
		this.points = points;
	}

	/**
	 * Renvoie vrai si le point est dans le polygone
	 * @param p le point à tester
	 * @return
	 */
	public boolean contains(Point p) {
		ArrayList<Point> inter = intersect(p);
		return (inter.size()%2 != 0);
	}

	/**
	 * Implementation de l'enveloppe convexe en O(n³)
	 * @return l'enveloppe convexe
	 */
	public ArrayList<Segment> envellopeConvexN3() {
		ArrayList<Segment> res = new ArrayList<Segment>();

		for (Point p1 : points) {
			for (Point p2 : points) {
				Segment s = new Segment(p1, p2);
				boolean ok = true;
				for (Point p3 : points) {
					if (s.produitVectoriel(new Segment(p1, p3)) < 0) {
						ok = false;
						break;
					}
				}
				if (ok)
					res.add(s);
			}
		}
		return res;
	}

	/**
	 * Implementation de l'envellope convexe en O(n log(n))
	 * 
	 * @TODO: Finir l'implementation
	 * @return l'enveloppe convexe
	 */
	public ArrayList<Segment> envellopeConvex() {
		ArrayList<Segment> res = new ArrayList<Segment>();
		ArrayList<Point> pointtrie = new ArrayList<Point>();
		Point P = points[0];
		int index = 0;
		int i = 0;
		for (Point p : points) {
			if (P.y > p.y) {
				P = p;
				index = i;
			} else if (P.y == p.y && P.x > P.x) {
				P = p;
				index = i;
			}
			i++;
		}
		points[index] = points[0];
		points[0] = P;
		final Point p1 = P;
		Point[] tabs = points.clone();
		Point x = new Point(0, P.y);
		final Segment xp = new Segment(P, x);
		Arrays.sort(tabs, 1, tabs.length, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				Segment Po1 = new Segment(p1, o1);
				Segment Po2 = new Segment(p1, o2);
				double p1 = Math.acos(xp.produitScalaire(Po1)
						/ (xp.longueur() * Po1.longueur()));
				double p2 = Math.acos(xp.produitScalaire(Po2)
						/ (xp.longueur() * Po2.longueur()));
				return (int) (p1 - p2);
			}
		});
		Vector<Point> pile = new Vector<Point>();
		pile.add(P);
		pile.add(tabs[1]);
		return null;
	}

	/**
	 * Calcule le produit vectoriel p1p2 . p1p3
	 * @param p1 le premier point
	 * @param p2 le deuxime point
	 * @param p3 le troisieme point
	 * @return le produit vectoriel
	 */
	private double produitvectoriel(Point p1, Point p2, Point p3) {
		return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
	}

	/**
	 * Renvoie si le segment est a gauche du polyèdre
	 * @TODO implementer la fonction si nécessaire ou la supprimer
	 * @param s le segment
	 * @return vrai si le segment est à droite
	 * 
	 */
	public boolean agauche(Segment s) {
		return false;
	}

	/**
	 * Returns the list of triangles composing the Polygon.
	 * 
	 * @return List<Polyedre> of the triangles.
	 */
	public List<Polyedre> toTriangles() {
		return Triangulation.partitionning_polygon(this);
	}
	
	/**
	 * Calcule l'intersection d'un point p avec un point situé à l'infini
	 * @param p le point à prendre en compte
	 * @return les intersections avec le polyèdre
	 */
	public ArrayList<Point> intersect(Point p) {
		Point I  = new Point(10000,0);
		Segment PI = new Segment(p, I);
		return intersect(PI);
	}
	
	/**
	 * Calcule les intersections d'un segment avec le polyèdre
	 * @param s le segment à tester
	 * @return les points resultant de l'intersection
	 */
	public ArrayList<Point> intersect(Segment s) {
		ArrayList<Point> res = new ArrayList<>();
		for(int j = 0; j < points.length; j++) {
			Segment s1;
			if(j == (points.length-1))
				s1 = new Segment(points[j],points[0]);
			else
				s1 = new Segment(points[j], points[j+1]);
			Point p = s1.intersection(s);
			if(!res.contains(p) && p != null)
				res.add(p);
		}
		return res;
	}
}
