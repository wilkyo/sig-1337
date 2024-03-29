package com.google.code.sig_1337.itineraire;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.code.sig_1337.model.graph.Node;
import com.google.code.sig_1337.model.xml.IGraph;
import com.google.code.sig_1337.model.xml.IItineraire;
import com.google.code.sig_1337.model.xml.IPoint;
import com.google.code.sig_1337.model.xml.Point;
import com.google.code.sig_1337.model.xml.structure.IBuilding;

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
		public IItineraire chemin;
		/**
		 * Le coût de ce chemin
		 */
		public double value;

		/**
		 * Créer un noeud avec le chemin list et le coût value
		 * @param list le chemin
		 * @param value le coût du chemin
		 */
		public State(IItineraire list, double value) {
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
	 *            Le batiment de départ de l'itinéraire
	 * @param arrive
	 *            Le batiment d'arrivé de l'itinéaire
	 * @param iGraph
	 *            La liste d'adjacence (un graphe)
	 * @return la liste des points du parcours commençant par le point d'arrivé,
	 *         null si pas de chemin
	 */
	public static IItineraire CalculItineraire(IBuilding depart, IBuilding arrive,
			IGraph iGraph) {
		IItineraire res = new com.google.code.sig_1337.model.xml.Itineraire();
		IPoint milieu = calculMilieu(arrive);
		if (!depart.equals(arrive)) {
			ArrayList<State> liststate = new ArrayList<Itineraire.State>();
			for (IPoint point : depart.getVoisins()) {
				for (IPoint pingraph: iGraph.keySet()) {
					if(pingraph.equals(point)) {
						IItineraire l = new com.google.code.sig_1337.model.xml.Itineraire();
						l.add(pingraph);
						State t = new State(l,calculValue(l, milieu));
						int i;
						for (i = 0; i < liststate.size();i++) {
							if(liststate.get(i).compareTo(t) == 1) {
								liststate.add(i, t);
								break;
							}
						}
						if(i == liststate.size())
							liststate.add(t);
						break;
					}
				}
			}
			if(liststate.size()==0)
				Log.d("pouet", "why?");
			return CalculItineraireRec(liststate, arrive, iGraph);
		}
		else {
			res.add(arrive.getVoisins().get(0));
			return res;
		}
	}

	/**
	 * 
	 * @param listState
	 * 				  la liste des états à traiter classer par coût
	 * @param arrive
	 * 				  Le point d'arrive
	 * @param iGraph
	 * 				  le graphe
	 * @return
	 * 				  l'itinéraire calculé, null si aucun itinéraire
	 */
	private static IItineraire CalculItineraireRec(
			ArrayList<State> listState, IBuilding arrive,
			IGraph iGraph) {
		if (listState.isEmpty())
			return null;
		else {
			// Je recupère le cas le plus intéressant
			State head = listState.remove(0);
			// je regarde si je suis à l'arrivé
			if (arrive.getVoisins().contains(head.getHead()))
				return head.chemin;
			else {
				//Calcul du milieu du batiment
				IPoint milieu = calculMilieu(arrive);
				//je génère les nouveaux cas
				
				List<IPoint> voisin = iGraph.get(head.getHead());
				if(voisin == null)
					voisin = new ArrayList<IPoint>();
				for (IPoint point : voisin) {
					// Evite de retourner dans un état précedant
					if (!head.chemin.contains(point)) {
						IItineraire newChemin = new com.google.code.sig_1337.model.xml.Itineraire();
						newChemin.addAll(head.chemin);
						newChemin.add(point);
						State ajout = new State(newChemin, calculValue(
								newChemin, milieu));
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
				return CalculItineraireRec(listState, arrive, iGraph);
			}
		}
	}

	/**
	 * Calcul le coût d'un chemin
	 * @param chemin le chemin
	 * @param arrive le point d'arrivé 
	 * @return Le coût du chemin
	 */
	private static double calculValue(List<IPoint> chemin, IPoint arrive) {
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

	
	private static IPoint calculMilieu(IBuilding building) {
		List<Node> list = building.getVoisins();
		IPoint milieu = list.get(0);
		for(int i = 1; i < list.size(); i++) {
			IPoint p = list.get(i);
			double lon = (milieu.getLongitude() + p.getLongitude())/2;
			double rellon = (milieu.getRelativeLongitude() + p.getRelativeLongitude())/2;
			double lat = (milieu.getLatitude() + p.getLatitude())/2;
			double rellat = (milieu.getRelativeLatitude() + p.getRelativeLatitude())/2;
			milieu = new Point(lon,lat,rellon,rellat);
		}
		return milieu;
	}
}
