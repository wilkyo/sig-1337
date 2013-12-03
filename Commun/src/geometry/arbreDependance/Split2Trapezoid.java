package geometry.arbreDependance;

import geometry.model.Segment;

import java.util.ArrayList;
import java.util.List;

public class Split2Trapezoid {

	public Trapezoid[] initial;
	public Segment segment;
	public Trapezoid left;
	public Trapezoid right;
	public Trapezoid[] top;
	public Trapezoid[] bottom;

	public Split2Trapezoid(Trapezoid[] initial, Segment segment) {
		this.initial = initial;
		this.segment = segment;
	}

}
