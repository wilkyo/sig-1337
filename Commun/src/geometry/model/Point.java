package geometry.model;

/**
 * Classe contenant les informations d'un point
 */
public class Point implements Comparable<Point> {
	/**
	 * La position en X du point
	 */
	public double x;
	/**
	 * La position en Y du point
	 */
	public double y;

	/**
	 * Construit un point de coordonnée x,y
	 * 
	 * @param x
	 *            La position en X
	 * @param y
	 *            La position en Y
	 */
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Construit un point de coordonnée p.x, p.y
	 * 
	 * @param p
	 *            le point à recopier
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + Double.doubleToLongBits(x));
		result = (int) (prime * result + Double.doubleToLongBits(y));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return (proche(other.x, x) && proche(other.y, y));
	}

	/**
	 * Applique une translation de vecteur s
	 * 
	 * @param s
	 *            le vecteur de translation
	 * @return Le point translaté
	 */
	public Point translation(Segment s) {
		return new Point(x + s.fin.x - s.debut.x, y + s.fin.y - s.debut.y);
	}

	/**
	 * Applique une homothetie de coefficiant lamda
	 * 
	 * @param lambda
	 *            le coefficiant de l'homothetie
	 * @return le point après homothétie
	 */
	public Point homothetie(double lambda) {
		return new Point(lambda * x, lambda * y);
	}

	/**
	 * L'écart minimun entre 2 points
	 */
	private static final double EPSILON = 0.00000001;

	/**
	 * Fonction vérifiant que 2 valeurs sont assez éloignés
	 * 
	 * @param x
	 *            un des valeurs à comparer
	 * @param y
	 *            un des valeurs à comparer
	 * @return Vrai si les valeurs sont proche par rapport à EPSILON
	 */
	private static boolean proche(double x, double y) {
		return (x-y < EPSILON) && (y-x < EPSILON);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Point p) {
		if (proche(x, p.x) && proche(y, p.y))
			return 0;
		if (y < p.y)
			return 1;
		else if (y > p.y)
			return -1;
		else if (x < p.x)
			return -1;
		else if (x > p.x)
			return 1;
		else
			return 0;
	}

}