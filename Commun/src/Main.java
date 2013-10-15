import java.util.ArrayList;

import fenetre.Fenetre;
import fenetre.Panneau;
import base.Point;
import base.Segment;
import base.Polyedre;


public class Main {
	public static void main(String[] arg) {
		Point[] A = { new Point(400, 400), new Point(100, 100),
				new Point(200, 200), new Point(400, 100), new Point(70, 70), new Point(300,250) };
		
		Polyedre p = new Polyedre(A);
		ArrayList<Segment> envconv	= p.envellopeConvexN3();
//		Point p = new Point(180,70);
		
		Panneau panneau = new Panneau();
		for (Point pt : A) {
			panneau.add(pt);
		}
		for (Segment segment : envconv) {
			panneau.add(segment);
		}
		//panneau.add(p);
//		panneau.add(p);

		//Point p = s1.intersection(s2);
		
		//if (p!= null)
		//	panneau.add(p);
		
		Fenetre fenetre = new Fenetre(panneau);
		fenetre.show();

	}
}
