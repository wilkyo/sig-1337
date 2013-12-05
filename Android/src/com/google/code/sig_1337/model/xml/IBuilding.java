package com.google.code.sig_1337.model.xml;

import java.util.List;


/**
 * Interface for buildings.
 */
public interface IBuilding extends IStructure {

	public List<IPoint> getNeighborhood(); 
}
