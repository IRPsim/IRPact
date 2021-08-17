package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork extends InitalizablePart {

    GraphTopologyScheme getGraphTopologyScheme();

    void setGraphTopologyScheme(GraphTopologyScheme topologyScheme);

    SocialGraph getGraph();
}
