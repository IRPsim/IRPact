package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface Agent extends SimulationEntity {

    boolean tryAquireAction();

    boolean tryAquireSelf();

    void allowAquire();

    void aquireFailed();

    void actionPerformed();

    void releaseAquire();

    SocialGraph.Node getSocialGraphNode();

    void setSocialGraphNode(SocialGraph.Node node);
}
