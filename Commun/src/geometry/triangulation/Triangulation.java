package geometry.triangulation;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.gui.Fenetre;
import geometry.gui.Panneau;
import geometry.model.Point;
import geometry.model.Polygone;
import geometry.model.Segment;

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
		if (DEBUG)
			System.out.println("PartitioningPolygon...");

		ArrayList<Polygone> res = new ArrayList<Polygone>();
		ArrayList<Point> sommets = new ArrayList<Point>(
				Arrays.asList(polygon.points));
		sommets.remove(0); // Suppression du point de retour pour l'algo

		if (DEBUG)
			creerFenetre(polygon);

		int i = 0;
		int cpt = 0;
		Point[] pts = {};
		Polygone poly = new Polygone(sommets.toArray(new Point[0]));
		while (sommets.size() > 3) {
			if (DEBUG)
				System.out.println("ETAPE " + i + ": " + sommets.size()
						+ "\nSommets " + sommets.toString());
			Point courant = sommets.get(i);
			Point prec = sommets.get((i - 1 + sommets.size()) % sommets.size());
			Point suivant = sommets.get((i + 1) % sommets.size());
			Segment test = new Segment(prec, suivant);
			ArrayList<Point> intersection = poly.intersect(test);
			if (DEBUG)
				System.out.println("Intersections "
						+ Arrays.toString(intersection.toArray(new Point[0]))
						+ "\nMilieu " + test.milieu());
			boolean contient = poly.contains(test.milieu());
			if ((intersection.size() == 0 || intersection.size() == 2)
					&& contient) {
				Point[] newtriangle = { courant, prec, suivant };
				res.add(new Polygone(newtriangle));
				sommets.remove(i);
				poly = new Polygone(sommets.toArray(pts));
				if (DEBUG && !REFRESH_ALWAYS)
					refreshPanel(poly);
				cpt = 0;
			} else {
				i++;
			}
			i = i % sommets.size();

			if (DEBUG && REFRESH_ALWAYS)
				refreshPanel(poly);
			if (DEBUG)
				System.out.println("Res "
						+ Arrays.toString(res.toArray(new Polygone[0])));

			if (cpt == sommets.size() * 2) {
				if (sommets.size() == 4) {
					Segment s1 = new Segment(sommets.get(0), sommets.get(1));
					Segment s2 = new Segment(sommets.get(2), sommets.get(3));
					Point inter = s1.intersection(s2);
					if (inter != null) {
						Point[] tri1 = { sommets.remove(3), sommets.remove(0),
								inter };
						res.add(new Polygone(tri1));
					} else {
						Segment s3 = new Segment(sommets.get(0), sommets.get(3));
						Segment s4 = new Segment(sommets.get(2), sommets.get(1));
						Point inter1 = s3.intersection(s4);
						if (inter1 != null) {
							Point[] tri1 = { sommets.remove(0),
									sommets.remove(1), inter };
							res.add(new Polygone(tri1));
						} else {
							sommets.remove(0);
						}
					}
				} else {
					sommets.remove(i);
					cpt = 0;
				}
			}
			cpt++;
		}
		res.add(new Polygone(sommets.toArray(pts)));
		return res;
	}

	private static Fenetre fenetre;
	private static Panneau panneau;
	private static Point[] bounds;
	private static boolean DEBUG = false;
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
