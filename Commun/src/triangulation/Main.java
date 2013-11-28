package triangulation;

import java.util.ArrayList;

import base.Point;
import base.Polygone;
import fenetre.Fenetre;
import fenetre.Panneau;

public class Main {

	public static void main(String[] args) {
		Point[] pts = { new Point(100, 100), new Point(150, 100),
				new Point(100, 150), new Point(0, 100), new Point(50, 50) };

		Polygone p = new Polygone(pts);
		ArrayList<Polygone> res = Triangulation.partitionningPolygon(p);
		ArrayList<Fenetre> listfen = new ArrayList<>();
		Panneau pan1 = new Panneau();
		for (Polygone poly : res) {
			pan1.add(poly);
		}
		new Fenetre("Test", pan1);
		Panneau pan = new Panneau();
		pan.add(p);
		new Fenetre("Test2", pan);
	}

}