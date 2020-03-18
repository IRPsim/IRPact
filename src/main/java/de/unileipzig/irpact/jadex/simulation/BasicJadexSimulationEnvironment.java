package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.annotation.ToImpl;
import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.commons.concurrent.ResettableTimer;
import de.unileipzig.irpact.core.simulation.EventManager;
import de.unileipzig.irpact.core.simulation.SimulationEnvironmentBase;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironment extends SimulationEnvironmentBase implements JadexSimulationEnvironment {

    //Jadex
    private IExternalAccess platform;
    private IClockService clockService;
    private ISimulationService simulationService;
    private IExternalAccess simulationAgent;
    //util
    private ResettableTimer activityTimer;

    public BasicJadexSimulationEnvironment() {
    }

    //=========================
    //setter
    //=========================

    public void setPlatform(IExternalAccess platform) {
        this.platform = platform;
    }

    public void setSimulationAgent(IExternalAccess simulationAgent) {
        this.simulationAgent = simulationAgent;
    }

    public void setClockService(IClockService clockService) {
        this.clockService = clockService;
    }

    public void setSimulationService(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public void setMessageSystem(JadexMessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void setTimeModule(JadexTimeModule timeModule) {
        this.timeModule = timeModule;
    }

    public void setConfig(JadexSimulationConfiguration simulationConfig) {
        this.simulationConfiguration = simulationConfig;
    }

    public IClockService getClockService() {
        return clockService;
    }

    public ISimulationService getSimulationService() {
        return simulationService;
    }

    public void setTimer(ResettableTimer activityTimer) {
        this.activityTimer = activityTimer;
    }

    public void setEventManager(JadexEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void validateAll() {
        validate();
        Check.requireNonNull(timeModule, "timeModule");
        Check.requireNonNull(clockService, "clockService");
        Check.requireNonNull(simulationService, "simulationService");
        Check.requireNonNull(platform, "platform");
    }

    public void prepare() {
        simulationService.pause();
        clockService.setClock(IClock.TYPE_CONTINUOUS, platform.searchService(new ServiceQuery<>(IThreadPoolService.class)).get());
    }

    public void waitForSimulation(Set<IExternalAccess> accessSet) {
        while(accessSet.size() != getConfiguration().getAccesses().size()) {
            //Thread.onSpinWait();
            //besser waere eine CountDownLatch, aber fuers testen reicht das
            ConcurrentUtil.sleepSilently(10);
        }
        //access equals geht nicht, also muss id genutzt werden
        for(IExternalAccess access: accessSet) {
            IComponentIdentifier id = access.getId();
            String name = id.getLocalName();
            IComponentIdentifier configId = getConfiguration().getIdentifier(name);
            if(id != configId) {
                throw new IllegalStateException("Missing Agent: " + name);
            }
        }
    }

    @ToImpl
    public void initialize() {
        Check.requireNonNull(simulationAgent, "simulationAgent");
    }

    @ToImpl
    public void start() {
        simulationService.start();
    }

    //=========================
    //JadexSimulationEnvironment
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
    public JadexTimeModule getTimeModule() {
        return (JadexTimeModule) timeModule;
    }

    @Override
    public JadexSimulationConfiguration getConfiguration() {
        return (JadexSimulationConfiguration) super.getConfiguration();
    }

    @Override
    public JadexEventManager getEventManager() {
        return (JadexEventManager) super.getEventManager();
    }

    //=========================
    //util
    //=========================

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void poke() {
        if(activityTimer != null) {
            activityTimer.reset();
        }
    }
}
