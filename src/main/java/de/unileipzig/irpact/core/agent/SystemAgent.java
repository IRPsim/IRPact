package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.exception.SystemAgentException;
import de.unileipzig.irpact.core.network.SocialGraph;

import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public interface SystemAgent extends Agent {

    @Override
    default long getActingOrder() {
        throw new SystemAgentException();
    }

    @Override
    default int getMaxNumberOfActions() {
        throw new SystemAgentException();
    }

    @Override
    default void allowAttention() {
        throw new SystemAgentException();
    }

    @Override
    default boolean tryAquireAttention() {
        throw new SystemAgentException();
    }

    @Override
    default void actionPerformed() {
        throw new SystemAgentException();
    }

    @Override
    default void releaseAttention() {
        throw new SystemAgentException();
    }

    @Override
    default void aquireDataAccess() {
        throw new SystemAgentException();
    }

    @Override
    default boolean tryAquireDataAccess() {
        throw new SystemAgentException();
    }

    @Override
    default boolean tryAquireDataAccess(long time, TimeUnit unit) {
        throw new SystemAgentException();
    }

    @Override
    default void releaseDataAccess() {
        throw new SystemAgentException();
    }

    @Override
    default SocialGraph.Node getSocialGraphNode() {
        throw new SystemAgentException();
    }

    @Override
    default void setSocialGraphNode(SocialGraph.Node node) {
        throw new SystemAgentException();
    }
}
