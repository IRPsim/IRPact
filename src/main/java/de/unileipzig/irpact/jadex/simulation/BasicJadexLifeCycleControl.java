package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexLifeCycleControl implements JadexLifeCycleControl {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControl.class);

    protected CountDownLatch startSynchronizer;
    protected KillSwitch killSwitch;

    protected IInternalAccess controlAgent;
    protected IExternalAccess platform;
    protected ISimulationService simulationService;
    protected IClockService clockService;

    public BasicJadexLifeCycleControl() {
        killSwitch = new KillSwitch();
        killSwitch.setControl(this);
    }

    public void setPlatform(IExternalAccess platform) {
        this.platform = platform;
    }

    public void setSimulationService(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public void setClockService(IClockService clockService) {
        this.clockService = clockService;
    }

    public void setTotalNumberOfAgents(int count) {
        startSynchronizer = new CountDownLatch(count);
    }

    public void setKillSwitchTimeout(long timeout) {
        killSwitch.setTimeout(timeout);
    }

    @Override
    public void registerSimulationAgentAccess(IInternalAccess controlAgent) {
        this.controlAgent = controlAgent;
    }

    @Override
    public void reportAgentCreated(Agent agent) {
        startSynchronizer.countDown();
    }

    @Override
    public void waitForCreationFinished() throws InterruptedException {
        startSynchronizer.await();
    }

    @Override
    public void initialize() {
        killSwitch.start();
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public void pause() {
        simulationService.pause().get();
        clockService.stop();
    }

    @Override
    public void resume() {
        clockService.start();
        simulationService.start().get();
    }

    @Override
    public void pulse() {
        killSwitch.reset();
    }

    protected void initTerminate() {
        //TODO hier vllt den spamfilter einbauen
    }

    @Override
    public IFuture<Map<String, Object>> terminate() {
        LOGGER.info("terminate");
        killSwitch.finished();
        initTerminate();
        return platform.killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateTimeout() {
        LOGGER.info("terminate with timeout");
        initTerminate();
        return platform.killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateWithError(Exception e) {
        LOGGER.info("terminate with error");
        killSwitch.finished();
        initTerminate();
        return platform.killComponent(e);
    }
}
