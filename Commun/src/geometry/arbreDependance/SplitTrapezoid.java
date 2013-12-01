package geometry.arbreDependance;

import geometry.model.Segment;

public class SplitTrapezoid {

	public Trapezoid initial;
	public Segment segment;
	public Trapezoid a;
	public Trapezoid b;
	public Trapezoid c;
	public Trapezoid d;

	public SplitTrapezoid(Trapezoid initial, Segment segment) {
		super();
		this.initial = initial;
		this.segment = segment;
	}

}
