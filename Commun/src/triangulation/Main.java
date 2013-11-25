package triangulation;

import java.util.ArrayList;

import fenetre.Fenetre;
import fenetre.Panneau;
import base.Point;
import base.Polyedre;
import base.Segment;

public class Main {

	public static void main(String[] args) {
		Point[] pts = {new Point(100, 100), new Point(150, 100), new Point(100, 150),new Point(0,100),new Point(50,50)};

		
		Polyedre p = new Polyedre(pts);
		ArrayList<Polyedre> res = Triangulation.partitionning_polygon(p);
		ArrayList<Fenetre> listfen = new ArrayList<>();
				Panneau pan1 = new Panneau();
		for (Polyedre poly : res) {
			pan1.add(poly);
		}
		listfen.add(new Fenetre(pan1));
		Panneau pan = new Panneau();
		pan.add(p);
		listfen.add(new Fenetre(pan));
		for (Fenetre fen : listfen) {
			fen.show();
		}
	}

}