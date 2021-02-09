package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public interface SystemAgent extends Agent {

    @Override
    default boolean aquireAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SocialGraph.Node getSocialGraphNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void setSocialGraphNode(SocialGraph.Node node) {
        throw new UnsupportedOperationException();
    }
}
