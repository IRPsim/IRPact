package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationEnvironmentBase;
import de.unileipzig.irpact.core.simulation.Timestamp;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironment extends SimulationEnvironmentBase implements JadexSimulationEnvironment {

    //cache
    private Map<String, IExternalAccess> accessMap = new HashMap<>();
    private Map<String, IComponentIdentifier> componentIdentifierMap = new HashMap<>();
    private Map<String, JadexAgent> agentMap = new HashMap<>();
    private Map<Class<?>, Set<JadexAgent>> agentTypeMap = new HashMap<>();
    //Jadex
    private JadexMessageSystem msgSystem = new JadexMessageSystem(this, JadexMessageSystem.Mode.BASIC);
    private IExternalAccess platform;
    private IClockService clockService;
    private ISimulationService simulationService;

    public BasicJadexSimulationEnvironment() {
    }

    //=========================
    //...
    //=========================

    public void setPlatform(IExternalAccess platform) {
        this.platform = platform;
    }

    public void setClockService(IClockService clockService) {
        this.clockService = clockService;
    }

    public void setSimulationService(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    //=========================
    //experimental
    //=========================

    @Override
    public synchronized void register(String name, IExternalAccess access) {
        if(accessMap.containsKey(name)) {
            throw new IllegalArgumentException("IExternalAccess already exists: " + name);
        }
        accessMap.put(name, access);
    }

    @Override
    public synchronized void registerInternal(String name, IComponentIdentifier identifier, JadexAgent agent) {
        if(componentIdentifierMap.containsKey(name)) {
            throw new IllegalArgumentException("IComponentIdentifier already exists: " + name);
        }
        if(agentMap.containsKey(name)) {
            throw new IllegalArgumentException("JadexAgent already exists: " + name);
        }
        componentIdentifierMap.put(name, identifier);
        agentMap.put(name, agent);
        Set<JadexAgent> agents = agentTypeMap.computeIfAbsent(agent.getClass(), _type -> new HashSet<>());
        agents.add(agent);
    }

    @Override
    public IExternalAccess getExternalAccess(String agentName) {
        return accessMap.get(agentName);
    }

    @Override
    public IComponentIdentifier getComponentIdentifier(String name) {
        return componentIdentifierMap.get(name);
    }

    @Override
    public JadexAgent getAgent(String name) {
        return agentMap.get(name);
    }

    @Override
    public Set<JadexAgent> getAgents(Class<?> type) {
        return agentTypeMap.get(type);
    }

    @Override
    public Collection<IExternalAccess> getAccesses() {
        return accessMap.values();
    }

    @Override
    public Collection<IComponentIdentifier> getComponentIdentifiers() {
        return componentIdentifierMap.values();
    }

    //=========================
    //stuff
    //=========================

    @Override
    public IExternalAccess getPlatform() {
        return platform;
    }

    @Override
    public JadexMessageSystem getMessageSystem() {
        return msgSystem;
    }

    @Override
    public long getStarttime() {
        return clockService.getStarttime();
    }

    @Override
    public long getSimulationTime() {
        return clockService.getTime();
    }

    @Override
    public double getTick() {
        return clockService.getTick();
    }

    @Override
    public long getSystemTime() {
        return System.currentTimeMillis();
    }

    @Override
    public Timestamp getTimestamp() {
        return new Timestamp(
                mode,
                getSystemTime(),
                getSimulationTime(),
                getTick()
        );
    }
}
