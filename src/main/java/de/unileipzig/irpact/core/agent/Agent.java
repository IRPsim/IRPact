package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface Agent extends SimulationEntity, Acting {

    SocialGraph.Node getSocialGraphNode();

    void setSocialGraphNode(SocialGraph.Node node);
}
