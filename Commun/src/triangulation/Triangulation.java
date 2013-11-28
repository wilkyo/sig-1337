package triangulation;

import java.util.ArrayList;
import java.util.Arrays;
import base.Point;
import base.Polyedre;
import base.Segment;

/**
 * Classe permettant de triangulariser un polygone
 */
public class Triangulation {

	/**
	 * Triangule un polygone
	 * @param p le polygone à partitionner
	 * @return les triangles résultant de la triangularisation 
	 */
	public static ArrayList<Polyedre> partitionning_polygon(Polyedre p) {
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
			}
			if(i == sommets.size())
				i = 0;
		}
		res.add(new Polyedre(sommets.toArray(pts)));
		return res;
	}
}
