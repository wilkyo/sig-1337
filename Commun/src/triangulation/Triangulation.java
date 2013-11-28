package triangulation;

import java.util.ArrayList;
import java.util.Arrays;

import base.Point;
import base.Polygone;
import base.Segment;
import fenetre.Fenetre;
import fenetre.Panneau;

/**
 * Classe permettant de triangulariser un polygone
 */
public class Triangulation {

	/**
	 * Triangule un polygone
	 * 
	 * @param polygon
	 *            le polygone à partitionner
	 * @return les triangles résultant de la triangularisation
	 */
	public static ArrayList<Polygone> partitionningPolygon(Polygone polygon) {
		System.out.println("PartitioningPolygon...");

		ArrayList<Polygone> res = new ArrayList<Polygone>();
		ArrayList<Point> sommets = new ArrayList<Point>(
				Arrays.asList(polygon.points));
		sommets.remove(0); // Suppression du point de retour pour l'algo

		if (cpt++ == 0) // TODO En attendant
			return res;
		if (DEBUG)
			creerFenetre(polygon);

		int i = 0;
		Point[] pts = {};
		Polygone poly = new Polygone(sommets.toArray(new Point[0]));
		while (sommets.size() > 3) {
			System.out.println("ETAPE " + i + ": " + sommets.size());
			System.out.println("Sommets " + sommets.toString());
			Point courant = sommets.get(i);
			Point prec = sommets.get((i - 1 + sommets.size()) % sommets.size());
			Point suivant = sommets.get((i + 1) % sommets.size());
			Segment test = new Segment(prec, suivant);
			ArrayList<Point> intersection = poly.intersect(test);
			System.out.println("Intersections "
					+ Arrays.toString(intersection.toArray(new Point[0])));
			System.out.println("Milieu " + test.milieu());
			boolean contient = poly.contains(test.milieu());
			if ((intersection.size() == 0 || intersection.size() == 2)
					&& contient) {
				Point[] newtriangle = { courant, prec, suivant };
				res.add(new Polygone(newtriangle));
				sommets.remove(i);
				poly = new Polygone(sommets.toArray(pts));
				if (DEBUG && !REFRESH_ALWAYS)
					refreshPanel(poly);
			} else {
				i++;
			}
			i = i % sommets.size();

			if (DEBUG && REFRESH_ALWAYS)
				refreshPanel(poly);
			System.out.println("Res "
					+ Arrays.toString(res.toArray(new Polygone[0])));
		}
		res.add(new Polygone(sommets.toArray(pts)));
		return res;
	}

	private static Fenetre fenetre;
	private static Panneau panneau;
	private static Point[] bounds;
	private static int cpt = 0;
	private static boolean DEBUG = true;
	private static boolean REFRESH_ALWAYS = true;

	/**
	 * Crée la fenêtre ou la prépare pour le nouveau polygone.
	 * 
	 * @param poly
	 */
	private static void creerFenetre(Polygone poly) {
		bounds = poly.getBounds();
		if (fenetre == null) {
			panneau = new Panneau(bounds);
			panneau.add(poly);
			fenetre = new Fenetre("Triangulation", panneau);
		} else {
			panneau = new Panneau(bounds);
			panneau.add(poly);
			fenetre.setContentPane(panneau);
			fenetre.revalidate();
		}
	}

	/**
	 * Raffraichis le panneau avec le nouveau polygone.
	 * 
	 * @param poly
	 */
	private static void refreshPanel(Polygone poly) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Point[] ptsss = new Point[poly.points.length + 1];
		for (int k = 0; k < poly.points.length; k++)
			ptsss[k] = poly.points[k];
		ptsss[ptsss.length - 1] = ptsss[0];
		panneau = new Panneau(bounds);
		panneau.add(new Polygone(ptsss));
		fenetre.setContentPane(panneau);
		fenetre.revalidate();
	}
}
