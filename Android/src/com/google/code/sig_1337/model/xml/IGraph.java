package com.google.code.sig_1337.model.xml;

import java.util.concurrent.ConcurrentMap;
import java.util.List;

/**
 * Interface the graph (list of sommet)
 *
 */
public interface IGraph extends ConcurrentMap<IPoint, List<IPoint>>{

}
