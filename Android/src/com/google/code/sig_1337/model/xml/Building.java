package com.google.code.sig_1337.model.xml;

import java.util.List;


/**
 * Building.
 */
public class Building extends Structure implements IBuilding {

	private List<IPoint> neighborhood;

	public Building(String name, List<IPoint> neighborhood) {
		super(name);
		this.neighborhood = neighborhood;
	}

	public StructureType getType() {
		return StructureType.Building;
	}

	@Override
	public List<IPoint> getNeighborhood() {
		return neighborhood;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(this == o) {
			return true;
		}
		if(this.getClass() != o.getClass()){
			return false;
		}
		Building b = (Building) o;
		return b.getName().equals(getName());
	}
}
