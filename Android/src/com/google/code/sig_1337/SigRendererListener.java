package com.google.code.sig_1337;

import java.util.EventListener;

import com.google.code.sig_1337.model.xml.structure.IStructure;

public interface SigRendererListener extends EventListener {

	/**
	 * Called when a structure has been selected by user.
	 * 
	 * @param structure
	 *            selected structure.
	 */
	public void onStructureSelected(IStructure structure);

}
