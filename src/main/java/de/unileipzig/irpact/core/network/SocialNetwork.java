package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork extends Initialization {

    GraphTopologyScheme getGraphTopologyScheme();

    void setGraphTopologyScheme(GraphTopologyScheme topologyScheme);

    SocialGraph getGraph();
}
