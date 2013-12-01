package com.google.code.sig_1337.model.xml;

import java.util.List;

public class Bassin extends Structure implements IBassin {

	public Bassin(String name, List<ITriangles> triangles) {
		super(name, triangles);
	}

	public StructureType getType() {
		return StructureType.Bassin;
	}

}
