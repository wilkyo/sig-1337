package com.google.code.sig_1337.model.xml;


public class Foret extends Structure implements IForet {

	public Foret(String name) {
		super(name);
	}

	public StructureType getType() {
		return StructureType.Foret;
	}

}
