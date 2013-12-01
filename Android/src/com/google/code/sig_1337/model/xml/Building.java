package com.google.code.sig_1337.model.xml;

import java.util.List;

/**
 * Building.
 */
public class Building extends Structure implements IBuilding {

	public Building(String name, List<ITriangles> triangles) {
		super(name, triangles);
	}

	public StructureType getType() {
		return StructureType.Building;
	}

}
