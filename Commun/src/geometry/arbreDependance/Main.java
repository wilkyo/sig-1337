package geometry.arbreDependance;

import geometry.gui.Fenetre;
import geometry.gui.Panneau;
import geometry.model.Point;
import geometry.model.Polygone;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		Polygone p1 = new Polygone(new Point(200, 200), new Point(250, 200),
				new Point(200, 250), new Point(100, 200), new Point(150, 150));
		Polygone p2 = new Polygone(new Point(260, 110), new Point(300, 120),
				new Point(340, 160), new Point(300, 150), new Point(267, 130));
		Panneau pan1 = new Panneau();
		pan1.add(p1);
		pan1.add(p2);
		List<Polygone> list = new ArrayList<Polygone>();
		list.add(p1);
		list.add(p2);
		ArbreDependance ad = ArbreDependance.create(list);
		new Fenetre("Test", pan1);
	}

}