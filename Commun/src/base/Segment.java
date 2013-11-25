package base;

/**
 * Classe contenant les informations d'un segment
 */
public class Segment {
	/**
	 * Le point du début du segment
	 */
	public Point debut;
	/**
	 * Le point de fin du segment
	 */
	public Point fin;

	/**
	 * Construit un segment à partir de deux point
	 * @param debut le point de debut
	 * @param fin le point de fin
	 */
	public Segment(Point debut, Point fin) {
		super();
		this.debut = debut;
		this.fin = fin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[" + debut + ", " + fin + "]";
	}

	/**
	 * Retourne le hashcode de la classe parent
	 * @return le hashcode de la classe parent
	 */
	public int superHashCode() {
		return super.hashCode();
	}

	/**
	 * Trie le segment par ses valeurs en y puis en x
	 * @return le segment trié
	 */
	public Segment trie() {
		assert !debut.equals(fin);
		if (debut.y > fin.y || (debut.y == fin.y && debut.x <= fin.x))
			return new Segment(debut, fin);
		else
			return new Segment(fin, debut);
	}

	/**
	 * Retourne le point du milieu du segment
	 * @return le milieu du segment
	 */
	public Point milieu() {
		return new Point((debut.x+fin.x)/2,(debut.y+fin.y)/2);
	}
	
	/**
	 * I don't know what i'm doing
	 * @param y 
	 * @return
	 */
	public Point intersectionHorizontale(float y) {
		if ((debut.y - y) * (fin.y - y) > 0) // debut, fin du m�me cot�
			return null;
		else if (debut.y == fin.y) // segment horizontal 
			return null;
		else {
			float x = ((fin.y - y) * debut.x + (y - debut.y)*fin.x)
					/ (fin.y - debut.y);
			return new Point(x, y);
		}
	}

	/**
	 * Calcule le produit scalaire avec le segment passé en paramêtre
	 * @param s le segment avec qui calculer le produit scalaire
	 * @return le produit scalaire
	 */
	public float produitScalaire(Segment s) {
		return (fin.x - debut.x) * (s.fin.x - s.debut.x) + (fin.y - debut.y)
				* (s.fin.y - s.debut.y);
	}

	/**
	 * Calcule le produit vectoriel avec le segment passé en paramêtre
	 * @param s le segment avec qui calculer le produit vectoriel
	 * @return le produit scalaire
	 */
	public float produitVectoriel(Segment s) {
		return (fin.x - debut.x) * (s.fin.y - s.debut.y) - (fin.y - debut.y)
				* (s.fin.x - s.debut.x);
	}

	/**
	 * Calcule l'homothétie sur le segment de coéfficiant lambda
	 * @param lambda le coefficiant d'homothétie
	 * @return le segment homothétié
	 */
	public Segment homothetie(float lambda) {
		return new Segment(debut.homothetie(lambda),fin.homothetie(lambda));
	}
	
	/**
	 * Vérifie que le point p est dans le segment
	 * @param p le point
	 * @return Vrai si le point est dans le segment, faux sinon
	 */
	public boolean contains(Point p) {
		Segment s = new Segment(debut,p);
		return (produitVectoriel(s) == 0
				&& produitScalaire(s) >= 0
				&& s.produitScalaire(s) <= produitScalaire(this));
	}

	/**
	 * Calcule la projectiob horizontale du point sur le segment
	 * @param p le point
	 * @return le point sur le segment
	 */
	public Point projectionHorizontale(Point p) {
		Point pointHorizon = new Point(p.x+1,p.y);
		Segment demiHorizon = new Segment(p,pointHorizon);
		if (produitVectoriel(demiHorizon) == 0)
				return null;
		else {
			float lambda = - produitVectoriel(new Segment(debut,p)) / produitVectoriel(demiHorizon);
			return p.translation(demiHorizon.homothetie(lambda));
		}
	}
	
	/**
	 * Calcule l'intersection avec le segment s
	 * @param s le segment
	 * @return le point au niveau de l'intersection, null si le point n'éxiste pas
	 */
	public Point intersection(Segment s) {
		if (s.produitVectoriel(this) == 0)
			return null;

		float a = (s.produitVectoriel(new Segment(debut, s.debut)))
				/ s.produitVectoriel(this);

		if (a < 0 || a > 1)
			return null;
		else {
			Point p = new Point(debut.x + a * (fin.x - debut.x), debut.y + a
					* (fin.y - debut.y));
			Segment strie = s.trie();
			if(p.x >= strie.debut.x && p.x <= strie.fin.x)
				return p;
			else
				return null;
		}
	}
	
	/**
	 * Calcule la longueur du segment
	 * @return la longueur du segment
	 */
	public double longueur() {
		return Math.sqrt(Math.pow(fin.x-debut.x,2)+Math.pow(fin.y-debut.y, 2));	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return debut.hashCode() + fin.hashCode();
	}
}
