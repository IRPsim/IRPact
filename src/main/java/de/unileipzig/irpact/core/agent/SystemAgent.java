package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public interface SystemAgent extends Agent {

    @Override
    default void lockAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void actionPerformed() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void releaseAction() {
        throw new UnsupportedOperationException();
    }

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
