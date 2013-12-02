package com.google.code.sig_1337.itineraire;

import com.google.code.sig_1337.model.xml.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe permettant de calculer l'itinéraire entre un point départ et un point
 * arrivé
 */
public class Itineraire {
	/**
	 * Classe internet permettant de garder l'état d'un noeud
	 *
	 */
	private static class State implements Comparable<State> {
		/**
		 * Le chemin parcouru
		 */
		public ArrayList<IPoint> chemin;
		/**
		 * Le coût de ce chemin
		 */
		public double value;

		/**
		 * Crée l'état de départ
		 * @param depart le point de l'etat
		 */
		public State(IPoint depart) {
			chemin = new ArrayList<IPoint>();
			chemin.add(depart);
			this.value = 0;
		}

		/**
		 * Créer un noeud avec le chemin list et le coût value
		 * @param list le chemin
		 * @param value le coût du chemin
		 */
		public State(ArrayList<IPoint> list, double value) {
			chemin = list;
			this.value = value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(State o) {
			if (value < o.value)
				return -1;
			else if (value == o.value)
				return 0;
			else
				return 1;
		}

		/**
		 * Retourne le dernier point du chemin
		 * @return
		 */
		public IPoint getHead() {
			return chemin.get(chemin.size() - 1);
		}
	}

	/**
	 * Calcul d'un itinéraire avec algo de type A* (fonction de décision : vol
	 * d'oiseau avec arrivé + distance parcouru)
	 * 
	 * @param depart
	 *            Le point de départ de l'itinéraire
	 * @param arrive
	 *            Le point d'arrivé de l'itinéaire
	 * @param listAdjacence
	 *            La liste d'adjacence (un graphe)
	 * @return la liste des points du parcours commençant par le point d'arrivé,
	 *         null si pas de chemin
	 */
	public static ArrayList<IPoint> CalculItineraire(Point depart, Point arrive,
			HashMap<IPoint, ArrayList<IPoint>> listAdjacence) {
		ArrayList<IPoint> res = new ArrayList<IPoint>();
		res.add(depart);
		if (!depart.equals(arrive)) {
			ArrayList<State> listState = new ArrayList<State>();
			listState.add(new State(depart));
			return CalculItineraireRec(listState, arrive, listAdjacence);
		}
		return res;
	}

	/**
	 * 
	 * @param listState la liste des états à traiter classer par coût
	 * @param arrive Le point d'arrive
	 * @param listAdjacence la liste d'adjacence
	 * @return l'itinéraire calculé, null si aucun itinéraire
	 */
	private static ArrayList<IPoint> CalculItineraireRec(
			ArrayList<State> listState, Point arrive,
			HashMap<IPoint, ArrayList<IPoint>> listAdjacence) {
		if (listState.isEmpty())
			return null;
		else {
			// Je recupère le cas le plus intéressant
			State head = listState.remove(0);
			// je regarde si je suis à l'arrivé
			if (head.getHead().equals(arrive))
				return head.chemin;
			else {
				//je génère les nouveaux cas
				ArrayList<IPoint> voisin = listAdjacence.get(head.getHead());
				for (IPoint point : voisin) {
					// Evite de retourner dans un état précedant
					if (!head.chemin.contains(point)) {
						ArrayList<IPoint> newChemin = new ArrayList<IPoint>(
								head.chemin);
						newChemin.add(point);
						State ajout = new State(newChemin, calculValue(
								newChemin, arrive));
						//insertion dans la liste du nouveau état
						int i;
						for (i = 0; i < listState.size(); i++) {
							if (listState.get(i).compareTo(ajout) == 1) {
								listState.add(i, ajout);
								break;
							}
						}
						if(i == listState.size())
							listState.add(ajout);
					}
				}
				//appel récursif pour traiter les nouveaux états
				return CalculItineraireRec(listState, arrive, listAdjacence);
			}
		}
	}

	/**
	 * Calcul le coût d'un chemin
	 * @param chemin le chemin
	 * @param arrive le point d'arrivé 
	 * @return Le coût du chemin
	 */
	private static double calculValue(ArrayList<IPoint> chemin, IPoint arrive) {
		double res = 0;
		for (int i = 0; i < chemin.size() - 1; i++) {
			IPoint p1 = chemin.get(i);
			IPoint p2 = chemin.get(i+1);
			res += Math.sqrt(Math.pow(p2.getLongitude() - p1.getLongitude(), 2)
					+ Math.pow(p2.getLatitude() - p1.getLatitude(), 2));
		}
		IPoint fin = chemin.get(chemin.size() - 1);
		res += Math.sqrt(Math.pow(arrive.getLatitude() - fin.getLatitude(), 2)
				+ Math.pow(arrive.getLongitude() - fin.getLongitude(), 2));
		return res;
	}

}
