package geometry.arbreDependance;

import geometry.model.OrderedSegment;

public class SplitTrapezoid {

	public Trapezoid initial;
	public OrderedSegment segment;
	public Trapezoid a;
	public Trapezoid b;
	public Trapezoid c;
	public Trapezoid d;

	public SplitTrapezoid(Trapezoid initial, OrderedSegment segment) {
		super();
		this.initial = initial;
		this.segment = segment;
	}

}
