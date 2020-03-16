package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.Scheme;

/**
 * @author Daniel Abitz
 */
public interface GraphTopologyScheme extends Scheme {

    void initalize(AgentGraph graph);
}
