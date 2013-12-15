package com.google.code.sig_1337.model;

import com.google.code.sig_1337.model.xml.ArbreDecision;
import com.google.code.sig_1337.model.xml.IGraph;

/**
 * Local sig.
 */
public interface ILocalSig1337 extends ISig1337 {

	/**
	 * Get the graph.
	 * 
	 * @return the graph.
	 */
	public IGraph getGraph();

	/**
	 * Get the tree.
	 * 
	 * @return the tree.
	 */
	public ArbreDecision getTree();

}
