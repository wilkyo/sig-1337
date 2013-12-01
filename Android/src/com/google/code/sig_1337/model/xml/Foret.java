package com.google.code.sig_1337.model.xml;

import java.util.List;

public class Foret extends Structure implements IForet {

	public Foret(String name, List<ITriangles> triangles) {
		super(name, triangles);
	}

	public StructureType getType() {
		return StructureType.Foret;
	}

}
