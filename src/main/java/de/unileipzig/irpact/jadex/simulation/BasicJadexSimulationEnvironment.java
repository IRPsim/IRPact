package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationEnvironmentBase;
import de.unileipzig.irpact.jadex.agent.simulation.JadexSimulationCache;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironment extends SimulationEnvironmentBase implements JadexSimulationEnvironment {

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

    public void setCache(JadexSimulationCache cache) {
        this.cache = cache;
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
    public JadexSimulationCache getCache() {
        return (JadexSimulationCache) cache;
    }

    @Override
    public long getSimulationStarttime() {
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
}
