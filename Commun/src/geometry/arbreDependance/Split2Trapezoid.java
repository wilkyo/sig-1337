package geometry.arbreDependance;

import geometry.model.OrderedSegment;

public class Split2Trapezoid {

	public Trapezoid[] initial;
	public OrderedSegment segment;
	public Trapezoid left;
	public Trapezoid right;
	public Trapezoid[] top;
	public Trapezoid[] bottom;

	public Split2Trapezoid(Trapezoid[] initial, OrderedSegment segment) {
		this.initial = initial;
		this.segment = segment;
	}

}
