package com.google.code.sig_1337.model.xml;


/**
 * Building.
 */
public class Building extends Structure implements IBuilding {

	public Building(String name) {
		super(name);
	}

	public StructureType getType() {
		return StructureType.Building;
	}

}
