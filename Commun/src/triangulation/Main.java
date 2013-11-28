package triangulation;

import java.util.ArrayList;

import fenetre.Fenetre;
import fenetre.Panneau;
import base.Point;
import base.Polyedre;
import base.Segment;

public class Main {

	public static void main(String[] args) {
		Point[] pts = {
		new Point(193.064963817596,478.478240966797),
		new Point(193.091571331024,478.481788635254),
		new Point(193.066000938416,478.482322692871),
		new Point(193.060231208801,478.482513427734),
		new Point(193.083441257477,478.48560333252),
		new Point(193.074560165405,478.48575592041),
		new Point(193.06412935257,478.486099243164),
		new Point(193.028235435486,478.481330871582),
		};
		
		Polyedre p = new Polyedre(pts);
		Panneau pan = new Panneau();
		pan.add(p);
		new Fenetre(pan).show();
		
		ArrayList<Polyedre> res = Triangulation.partitionning_polygon(p);
		ArrayList<Fenetre> listfen = new ArrayList<>();
				Panneau pan1 = new Panneau();
		for (Polyedre poly : res) {
			pan1.add(poly);
		}
		listfen.add(new Fenetre(pan1));
		listfen.add(new Fenetre(pan));
		for (Fenetre fen : listfen) {
			fen.show();
		}
	}

}