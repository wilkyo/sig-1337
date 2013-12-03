package geometry.arbreDependance;

import geometry.model.Polygone;
import data.model.Node;
import data.model.structure.Structure;

public class StructurePolygon {

	public Structure structure;
	public Polygone polygon;

	public StructurePolygon(Structure structure) {
		this.structure = structure;
		polygon = Node.toPolygon(structure.getNodes());
	}

}
