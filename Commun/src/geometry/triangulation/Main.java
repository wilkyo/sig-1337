package geometry.triangulation;

import java.util.ArrayList;

import geometry.gui.Fenetre;
import geometry.gui.Panneau;
import geometry.model.Point;
import geometry.model.Polygone;

public class Main {

	public static void main(String[] args) {

		Point[] pts = { new Point(100, 100), new Point(150, 100),
				new Point(100, 150), new Point(0, 100), new Point(50, 50) };

		Polygone p = new Polygone(pts);
		ArrayList<Polygone> res = Triangulation.partitionningPolygon(p);
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