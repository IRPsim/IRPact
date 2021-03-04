package de.unileipzig.irpact.experimental.tests;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * Special agent for testing.
 *
 * @author Daniel Abitz
 */
public interface TestAgent extends Agent {

    @Override
    default SimulationEnvironment getEnvironment() {
        return null;
    }

    @Override
    default String getName() {
        return null;
    }

    @Override
    default SocialGraph.Node getSocialGraphNode() {
        return null;
    }

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
    default void setSocialGraphNode(SocialGraph.Node node) {
    }
}
