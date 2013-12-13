package com.google.code.sig_1337.model.xml.structure;

import java.util.List;

import com.google.code.sig_1337.model.xml.IPoint;


/**
 * Interface for buildings.
 */
public interface IBuilding extends IStructure {

	public List<IPoint> getNeighborhood(); 
}
