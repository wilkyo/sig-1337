package com.google.code.sig_1337.model.xml.structure;

import com.google.code.sig_1337.model.xml.IVoisins;
import com.google.code.sig_1337.model.xml.Voisins;

/**
 * Building.
 */
public class Building extends Structure implements IBuilding {
	
	private IVoisins voisins;
	
	public Building(String name, Long id) {
		super(name, id);
		voisins = new Voisins();
	}

	public StructureType getType() {
		return StructureType.Building;
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

	@Override
	public IVoisins getVoisins() {
		return voisins;
	}
}
