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
    default int getMaxNumberOfActions() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void allowAttention() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean tryAquireAttention() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void actionPerformed() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void releaseAttention() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void setSocialGraphNode(SocialGraph.Node node) {
    }
}
