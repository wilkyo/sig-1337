package triangulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import triangulation.Labelled_Point;
import base.Point;
import base.Polyedre;

/**
 * Implementation de l'algorithme de triangularisation de polygone données dans le livre 
 * @author Jean-Baptiste Perrin
 *
 */
public class Triangulation {
		
	public static ArrayList<Polyedre> partitionning_polygon(Polyedre p ) {
		ArrayList<Polyedre> res = new ArrayList<>();
		Queue<Labelled_Point> queue = new LinkedList<>();
		ArrayList<Labelled_Point> copy = new ArrayList<>();
		
		//Attribution d'un label à chaque point (O(n))
		for(int i = 0; i<p.points.length; i++) {
			Point prec, suiv, curr = p.points[i];
			// Affectation de prec et de suiv suivant l'endroit ou l'on se trouve
			if(i == 0) {
				prec = p.points[p.points.length-1];
				suiv = p.points[i+1];
			} else if(i ==(p.points.length - 1)) {
				prec = p.points[i-1];
				suiv = p.points[0];				
			} else {
				prec = p.points[i-1];
				suiv = p.points[i+1];
			}
			
			if(curr.y > prec.y && curr.y > suiv.y) {
				//calculer l'angle intérieure entre les deux segments
				float angle = 3;
				if(angle < Math.PI)
					copy.add(new Labelled_Point(curr, Labelled_Point.Point_Type.START));
				else
					copy.add(new Labelled_Point(curr, Labelled_Point.Point_Type.SPLIT));
			} else if((curr.y >= prec.y && curr.y <= suiv.y)||(curr.y <= prec.y && curr.y >= suiv.y)) {
				copy.add(new Labelled_Point(curr, Labelled_Point.Point_Type.REGULAR));
			} else if(curr.y < prec.y && curr.y < suiv.y) {
				float angle = 3;
				if(angle < Math.PI)
					copy.add(new Labelled_Point(curr, Labelled_Point.Point_Type.END));
				else
					copy.add(new Labelled_Point(curr, Labelled_Point.Point_Type.MERGE));				
			}
		}
		//trie des points par ordre décroissant des y (a modifier car en O(n²) au pire cas)
		while (!copy.isEmpty()) {
			int i = 0;
			for(int k = 1; k < copy.size(); k++) {
				Point ini = copy.get(i);
				Point courant = copy.get(k);
				if(ini.y < courant.y)
					i = k;
				else if(ini.y == courant.y && ini.x > courant.y)
					i = k;
			}
			queue.add(copy.get(i));
			copy.remove(i);
		}
		
		while(!queue.isEmpty()) {
			switch(queue.poll().getType()) {
			case START :
				handle_start_vertex();
				break;
			case END :
				handle_end_vertex();
				break;
			case MERGE:
				handle_merge_vertex();
				break;
			case REGULAR:
				handle_regular_vertex();
				break;
			case SPLIT:
				handle_split_vertex();
				break;
			default:
				System.err.println("Cas impossible");
				break;
			}
		}
		
		
		
		return res;
	}
	
	private static void handle_split_vertex() {
		
	}
	
	private static void handle_end_vertex() {
		
	}
	
	private static void handle_start_vertex() {
		
	}
	
	private static void handle_regular_vertex() {
		
	}
	
	private static void handle_merge_vertex() {
		
	}
}
