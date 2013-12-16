package com.google.code.sig_1337.model.xml.structure;


public class Foret extends Structure implements IForet {

	public Foret(String name, Long id) {
		super(name, id);
	}

	public StructureType getType() {
		return StructureType.Foret;
	}

}
