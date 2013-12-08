package geometry.model;

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
	 * 
	 * @param debut
	 *            le point de debut
	 * @param fin
	 *            le point de fin
	 */
	public Segment(Point debut, Point fin) {
		super();
		this.debut = debut;
		this.fin = fin;
	}

	public Segment(Segment segment) {
		super();
		this.debut = new Point(segment.debut);
		this.fin = new Point(segment.fin);
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
	 * 
	 * @return le hashcode de la classe parent
	 */
	public int superHashCode() {
		return super.hashCode();
	}

	/**
	 * Trie le segment par ses valeurs en y puis en x
	 * 
	 * @return le segment tri��
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
	 * 
	 * @return le milieu du segment
	 */
	public Point milieu() {
		return new Point((debut.x + fin.x) / 2, (debut.y + fin.y) / 2);
	}

	/**
	 * I don't know what i'm doing
	 * 
	 * @param y
	 * @return
	 */
	public Point intersectionHorizontale(double y) {
		if ((debut.y - y) * (fin.y - y) > 0) // debut, fin du m���me cot���
			return null;
		else if (debut.y == fin.y) // segment horizontal
			return null;
		else {
			double x = ((fin.y - y) * debut.x + (y - debut.y) * fin.x)
					/ (fin.y - debut.y);
			return new Point(x, y);
		}
	}

	/**
	 * Calcule le produit scalaire avec le segment pass�� en param��tre
	 * 
	 * @param s
	 *            le segment avec qui calculer le produit scalaire
	 * @return le produit scalaire
	 */
	public double produitScalaire(Segment s) {
		return (fin.x - debut.x) * (s.fin.x - s.debut.x) + (fin.y - debut.y)
				* (s.fin.y - s.debut.y);
	}

	/**
	 * Calcule le produit vectoriel avec le segment passé en paramètre
	 * 
	 * @param s
	 *            le segment avec qui calculer le produit vectoriel
	 * @return le produit scalaire
	 */
	public double produitVectoriel(Segment s) {
		return (fin.x - debut.x) * (s.fin.y - s.debut.y) - (fin.y - debut.y)
				* (s.fin.x - s.debut.x);
	}

	/**
	 * Calcule l'homothétie sur le segment de coéfficient lambda
	 * 
	 * @param lambda
	 *            le coefficiant d'homothétie
	 * @return le segment homothétie
	 */
	public Segment homothetie(double lambda) {
		return new Segment(debut.homothetie(lambda), fin.homothetie(lambda));
	}

	/**
	 * V��rifie que le point p est dans le segment
	 * 
	 * @param p
	 *            le point
	 * @return Vrai si le point est dans le segment, faux sinon
	 */
	public boolean contains(Point p) {
		Segment s = new Segment(debut, p);
		return (produitVectoriel(s) == 0 && produitScalaire(s) >= 0 && s
				.produitScalaire(s) <= produitScalaire(this));
	}

	/**
	 * Calcule la projectiob horizontale du point sur le segment
	 * 
	 * @param p
	 *            le point
	 * @return le point sur le segment
	 */
	public Point projectionHorizontale(Point p) {
		Point pointHorizon = new Point(p.x + 1, p.y);
		Segment demiHorizon = new Segment(p, pointHorizon);
		if (produitVectoriel(demiHorizon) == 0)
			return null;
		else {

			double lambda = -produitVectoriel(new Segment(debut, p))
					/ produitVectoriel(demiHorizon);
			return p.translation(demiHorizon.homothetie(lambda));
		}
	}

	/**
	 * Calcule l'intersection avec le segment s
	 * 
	 * @param s
	 *            le segment
	 * @return le point au niveau de l'intersection, null si le point n'��xiste
	 *         pas
	 */
	public Point intersection(Segment s) {
		if (s.produitVectoriel(this) == 0)
			return null;

		double a = (s.produitVectoriel(new Segment(debut, s.debut)))
				/ s.produitVectoriel(this);

		if (a < 0 || a > 1)
			return null;
		else {
			Point p = new Point(debut.x + a * (fin.x - debut.x), debut.y + a
					* (fin.y - debut.y));
			Segment strie = s.trie();
			if (p.x >= strie.debut.x && p.x <= strie.fin.x)
				return p;
			else
				return null;
		}
	}

	/**
	 * Calcule la longueur du segment
	 * 
	 * @return la longueur du segment
	 */
	public double longueur() {
		return Math.sqrt(Math.pow(fin.x - debut.x, 2)
				+ Math.pow(fin.y - debut.y, 2));
	}

	public double slope() {
		return (fin.y - debut.y) / (fin.x - debut.x);
	}

	public double determinant(Point point) {
		return (fin.x - debut.x) * (point.y - debut.y) - (fin.y - debut.y)
				* (point.x - debut.x);
	}

	public boolean auDessus(Point point) {
		return determinant(point) < 0;
	}

	public double getY(double x) {
		if (x >= debut.x && x <= fin.x) {
			double m = (fin.y - debut.y) / (fin.x - debut.x);
			return (m * (x - debut.x)) + debut.y;
		} else {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return debut.hashCode() + fin.hashCode();
	}
}
