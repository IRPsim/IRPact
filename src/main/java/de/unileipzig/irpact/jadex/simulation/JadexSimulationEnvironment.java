package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    IExternalAccess getPlatform();

    @Override
    JadexMessageSystem getMessageSystem();

    //=========================
    //cache
    //=========================

    void register(String name, IExternalAccess access);

    void registerInternal(String name, IComponentIdentifier identifier, JadexAgent agent);

    IExternalAccess getExternalAccess(String agentName);

    IComponentIdentifier getComponentIdentifier(String name);

    JadexAgent getAgent(String name);

    Set<JadexAgent> getAgents(Class<?> type);

    Collection<IExternalAccess> getAccesses();

    Collection<IComponentIdentifier> getComponentIdentifiers();
}
