package com.google.code.sig_1337.model.xml.structure;

import com.google.code.sig_1337.model.xml.IVoisins;

/**
 * Interface for buildings.
 */
public interface IBuilding extends IStructure {

	public abstract IVoisins getVoisins();
}
