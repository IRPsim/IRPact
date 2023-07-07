package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface Agent extends SimulationEntity, Acting {

    SocialGraph.Node getSocialGraphNode();

    void setSocialGraphNode(SocialGraph.Node node);

    //=========================
    //v2
    //=========================

    default <A extends Agent> boolean is2(Class<A> c) {
        return c.isInstance(this);
    }

    default <A extends Agent> A as2(Class<A> c) {
        return c.cast(this);
    }

    default boolean isActor2() {
        return false;
    }
    default ActingAgent asActor2() {
        throw new UnsupportedOperationException("unsupported agent (" + getClass().getSimpleName() + ")");
    }

    default boolean isAttributable2() {
        return false;
    }
    default AttributableAgent asAttributable2() {
        throw new UnsupportedOperationException("unsupported agent (" + getClass().getSimpleName() + ")");
    }
}
