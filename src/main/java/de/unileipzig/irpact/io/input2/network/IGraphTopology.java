package de.unileipzig.irpact.io.input2.network;

import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface IGraphTopology {

    boolean use();

    <V, E> GraphTopology<V, E> createInstance();
}
