package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationEnvironmentBase;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironment extends SimulationEnvironmentBase implements JadexSimulationEnvironment {

    //Jadex
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

    public void setCache(JadexSimulationCache cache) {
        this.cache = cache;
    }

    public void setMessageSystem(JadexMessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void setTimeModule(JadexTimeModule timeModule) {
        this.timeModule = timeModule;
    }

    public IClockService getClockService() {
        return clockService;
    }

    public ISimulationService getSimulationService() {
        return simulationService;
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
        return (JadexMessageSystem) messageSystem;
    }

    @Override
    public JadexSimulationCache getCache() {
        return (JadexSimulationCache) cache;
    }

    @Override
    public JadexTimeModule getTimeModule() {
        return (JadexTimeModule) timeModule;
    }
}
