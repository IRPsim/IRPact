package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.network.SocialGraph;

/**
 * @author Daniel Abitz
 */
public interface SystemAgent extends Agent {

    @Override
    default boolean tryAquireAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean tryAquireSelf() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void aquireFailed() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void allowAquire() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void actionPerformed() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void releaseAquire() {
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
