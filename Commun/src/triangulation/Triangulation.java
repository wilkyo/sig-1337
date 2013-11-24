package triangulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import triangulation.Labelled_Point;
import triangulation.Labelled_Point.Point_Type;
import base.Point;
import base.Polyedre;
import base.Segment;

/**
 * Implementation de l'algorithme de triangularisation de polygone données dans
 * le livre
 * 
 * @author Jean-Baptiste Perrin
 * 
 */
public class Triangulation {

	public static ArrayList<Polyedre> partitionning_polygon(Polyedre p) {
		/*ArrayList<Polyedre> res = new ArrayList<>();
		ArrayList<Polyedre> monotone = new ArrayList<>();

		ArrayList<Labelled_Point> labelled_Points = label_point(p.points);

		boolean a_monotoniser = false;
		for (Labelled_Point labelled_Point : labelled_Points) {
			if (labelled_Point.getType() == Point_Type.MERGE
					|| labelled_Point.getType() == Point_Type.SPLIT) {
				a_monotoniser = true;
				break;
			}
		}

		for (Labelled_Point labelled_Point : labelled_Points) {
			System.out.println(labelled_Point);
		}

		if (a_monotoniser)
			monotone = monetise(labelled_Points);
		else
			monotone.add(p); // on a juste a triangulariser le polygone
								// originale
		// algo triangularisation sur les polygones y-monotone

		return res;*/
		ArrayList<Polyedre> res = new ArrayList<Polyedre>();
		ArrayList<Point> sommets = new ArrayList<>(Arrays.asList(p.points));
		int i = 0;
		Point[] pts = {};
		Polyedre poly = new Polyedre(p.points);
		while(sommets.size() > 3) {
			Point courant = sommets.get(i);
			Point prec;
			if(i == 0) {
				prec = sommets.get(sommets.size()-1);
			} else {
				prec = sommets.get(i-1);
			}
			Point suivant;
			if(i == (sommets.size()-1)) {
				suivant = sommets.get(0);
			} else {
				suivant = sommets.get(i+1);
			}
			Segment test = new Segment(prec, suivant);
			ArrayList<Point> intersection = poly.intersect(test);
			boolean contient = poly.contains(test.milieu());
			if(intersection.size() == 2 && contient) {
				Point[] newtriangle = {courant, prec, suivant};  
				res.add(new Polyedre(newtriangle));
				sommets.remove(i);
				poly = new Polyedre(sommets.toArray(pts));
			}
			else {
				i++;
				if(i == sommets.size())
					i = 0;
			}
		}
		res.add(new Polyedre(sommets.toArray(pts)));
		return res;
	}

	private static void handle_split_vertex(
			HashMap<Segment, Labelled_Point> helper) {

	}

	private static void handle_end_vertex(
			HashMap<Segment, Labelled_Point> helper) {

	}

	private static void handle_start_vertex(
			HashMap<Segment, Labelled_Point> helper) {

	}

	private static void handle_regular_vertex(
			HashMap<Segment, Labelled_Point> helper) {

	}

	private static void handle_merge_vertex(
			HashMap<Segment, Labelled_Point> helper) {

	}

	private static ArrayList<Labelled_Point> label_point(Point[] p) {
		ArrayList<Labelled_Point> res = new ArrayList<>();

		for (int i = 0; i < p.length; i++) {
			Point prec, suiv, curr = p[i];
			// Affectation de prec et de suiv suivant l'endroit ou l'on se
			// trouve
			if (i == 0) {
				prec = p[p.length - 1];
				suiv = p[i + 1];
			} else if (i == (p.length - 1)) {
				prec = p[i - 1];
				suiv = p[0];
			} else {
				prec = p[i - 1];
				suiv = p[i + 1];
			}

			if (curr.y > prec.y && curr.y > suiv.y) {
				// calculer l'angle intérieure entre les deux segments
				Segment cp = new Segment(curr, prec);
				Segment cs = new Segment(curr, suiv);
				double angle = Math.acos(cp.produitScalaire(cs)
						/ (cp.longueur() * cs.longueur()));
				angle *= 2;
				System.out.println(angle);
				if (angle < Math.PI)
					res.add(new Labelled_Point(curr,
							Labelled_Point.Point_Type.START));
				else
					res.add(new Labelled_Point(curr,
							Labelled_Point.Point_Type.SPLIT));
			} else if ((curr.y >= prec.y && curr.y <= suiv.y)
					|| (curr.y <= prec.y && curr.y >= suiv.y)) {
				res.add(new Labelled_Point(curr,
						Labelled_Point.Point_Type.REGULAR));
			} else if (curr.y < prec.y && curr.y < suiv.y) {
				Segment cp = new Segment(curr, prec);
				Segment cs = new Segment(curr, suiv);
				double angle = Math.acos(cp.produitScalaire(cs)
						/ (cp.longueur() * cs.longueur()));
				angle *= 2;
				System.out.println(angle);
				if (angle < Math.PI)
					res.add(new Labelled_Point(curr,
							Labelled_Point.Point_Type.END));
				else
					res.add(new Labelled_Point(curr,
							Labelled_Point.Point_Type.MERGE));
			}
		}
		return res;
	}

	private static ArrayList<Polyedre> monetise(
			ArrayList<Labelled_Point> labelled_point) {
		ArrayList<Polyedre> res = new ArrayList<>();
		Queue<Labelled_Point> queue = new LinkedList<>();
		HashMap<Segment, Labelled_Point> helper = new HashMap<>();

		while (!labelled_point.isEmpty()) {
			int i = 0;
			for (int k = 1; k < labelled_point.size(); k++) {
				Point ini = labelled_point.get(i);
				Point courant = labelled_point.get(k);
				if (ini.y < courant.y)
					i = k;
				else if (ini.y == courant.y && ini.x > courant.y)
					i = k;
			}
			queue.add(labelled_point.get(i));
			labelled_point.remove(i);
		}

		while (!queue.isEmpty()) {
			switch (queue.poll().getType()) {
			case START:
				handle_start_vertex(helper);
				break;
			case END:
				handle_end_vertex(helper);
				break;
			case MERGE:
				handle_merge_vertex(helper);
				break;
			case REGULAR:
				handle_regular_vertex(helper);
				break;
			case SPLIT:
				handle_split_vertex(helper);
				break;
			default:
				System.err.println("Cas impossible");
				break;
			}
		}
		return res;
	}

}
