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
    default boolean hasName(String input) {
        return false;
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
    default void lockAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void releaseAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean aquireAction() {
        return false;
    }

    @Override
    default void setSocialGraphNode(SocialGraph.Node node) {
    }
}
