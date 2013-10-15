package base;


public class Point implements Comparable<Point>{
	public float x;
	public float y;
	
	public Point(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}



	public Point translation(Segment s) {
		return new Point(x+s.fin.x-s.debut.x,y+s.fin.y-s.debut.y);
	}
	
	public Point homothetie(float lambda) {
		return new Point(lambda*x,lambda*y);
	}
	
	private static final float EPSILON = (float) 0.001;
	
	private static boolean proche(float x, float y) {
		return (x-y < EPSILON) && (y-x < EPSILON);
	}

	
	public int compareTo(Point p) {
		if (proche (x,p.x) && proche (y,p.y))
			return 0;

		if (y < p.y)
			return 1;
		else if (y>p.y)
			return -1;
		else if (x < p.x)
			return -1;
		else if (x>p.x)
			return 1;
		else 
			return 0;
	}
}