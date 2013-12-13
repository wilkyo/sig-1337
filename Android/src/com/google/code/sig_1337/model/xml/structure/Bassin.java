package com.google.code.sig_1337.model.xml.structure;

public class Bassin extends Structure implements IBassin {

	public Bassin(String name) {
		super(name);
	}

	public StructureType getType() {
		return StructureType.Bassin;
	}

}
