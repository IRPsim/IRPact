package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgent;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexLifeCycleControl implements JadexLifeCycleControl {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControl.class);

    protected final Lock SYNC_LOCK = new ReentrantLock();
    protected final Condition SYNC_CON = SYNC_LOCK.newCondition();
    protected boolean isSync = false;
    protected boolean noSync = false;
    protected final Map<Timestamp, Object> syncPoints = new HashMap<>();
    protected final Object SYNC_HELPER = new Object();

    protected CountDownLatch startSynchronizer;
    protected int totalNumberOfAgents;
    protected int createdAgents = 0;
    protected KillSwitch killSwitch;
    protected JadexSimulationEnvironment environment;
    protected Timestamp current;

    protected Agent controlAgent;
    protected IInternalAccess controlAgentAccess;
    protected IExternalAccess platform;
    protected ISimulationService simulationService;
    protected IClockService clockService;

    protected TerminationState state = TerminationState.NOT;

    public BasicJadexLifeCycleControl() {
        killSwitch = new KillSwitch();
        killSwitch.setControl(this);
    }

    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }

    protected Timestamp now() {
        return environment.getTimeModel().now();
    }

    @Override
    public void setPlatform(IExternalAccess platform) {
        this.platform = platform;
    }

    @Override
    public void startKillSwitch() {
        killSwitch.start();
    }

    @Override
    public void setSimulationService(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public ISimulationService getSimulationService() {
        return simulationService;
    }

    @Override
    public void setClockService(IClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public IClockService getClockService() {
        return clockService;
    }

    @Override
    public void setTotalNumberOfAgents(int count) {
        startSynchronizer = new CountDownLatch(count);
        totalNumberOfAgents = count;
    }

    public void setKillSwitchTimeout(long timeout) {
        killSwitch.setTimeout(timeout);
    }

    @Override
    public void registerSimulationAgentAccess(SimulationAgent agent, IInternalAccess access) {
        this.controlAgent = agent;
        this.controlAgentAccess = access;
    }

    @Override
    public void reportAgentCreated(Agent agent) {
        startSynchronizer.countDown();
        incCreatedAgents();
    }

    protected synchronized void incCreatedAgents() {
        createdAgents++;
    }

    @Override
    public void waitForCreationFinished() throws InterruptedException {
        startSynchronizer.await();
    }

    @Override
    public IFuture<Map<String, Object>> waitForTermination() {
        return platform.waitForTermination();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public void start() {
        resume();
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

    @Override
    public void addSynchronisationPoint(Timestamp ts) {
        Timestamp now = now();
        if(ts.isBefore(now)) {
            syncPoints.put(ts, SYNC_HELPER);
            LOGGER.debug(IRPSection.SIMULATION_LICECYCLE, "add new sync point: {}", ts);
        } else {
            LOGGER.debug(IRPSection.SIMULATION_LICECYCLE, "ignore invalid timestamp");
        }
    }

    @Override
    public boolean requiresSynchronisation(Agent agent) {
        if(syncPoints.isEmpty()) {
            return false;
        }

        Timestamp now = now();
        if(current != now) {
            SYNC_LOCK.lock();
            try {
                if(current == now) {
                    return isSync;
                }
                current = now;
                isSync = false;
                for(Timestamp syncPoint: syncPoints.keySet()) {
                    if(now.isAfterOrEquals(syncPoint)) {
                        isSync = true;
                        syncPoints.remove(syncPoint);
                        return isSync;
                    }
                }
            } finally {
                SYNC_LOCK.unlock();
            }
        }
        return isSync;
    }

    @Override
    public boolean waitForSynchronisation(Agent agent) {
        try {
            waitForSynchronisation0();
            return true;
        } catch (InterruptedException e) {
            LOGGER.warn(IRPSection.SIMULATION_LICECYCLE, "await interrupted for agent '{}'", agent.getName());
            return false;
        }
    }

    private void waitForSynchronisation0() throws InterruptedException {
        SYNC_LOCK.lock();
        try {
            while(isSync) {
                SYNC_CON.await();
            }
        } finally {
            SYNC_LOCK.unlock();
        }
    }

    @Override
    public void releaseSynchronisation() {
        if(isSync) {
            LOGGER.info(IRPSection.SIMULATION_LICECYCLE, "release syncronisation ({})", now());
            isSync = false;
            releaseAll();
        } else {
            LOGGER.info(IRPSection.SIMULATION_LICECYCLE, "nothing to release");
        }
    }

    private void releaseAll() {
        SYNC_LOCK.lock();
        try {
            SYNC_CON.signalAll();
        } finally {
            SYNC_LOCK.unlock();
        }
    }

    protected void prepareTermination() {
        //TODO hier vllt den spamfilter einbauen
    }

    @Override
    public IFuture<Map<String, Object>> terminate() {
        LOGGER.info("terminate");
        killSwitch.finished();
        prepareTermination();
        state = TerminationState.NORMAL;
        return platform.killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateTimeout() {
        LOGGER.info("terminate with timeout");
        prepareTermination();
        state = TerminationState.TIMEOUT;
        return platform.killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateWithError(Exception e) {
        LOGGER.info("terminate with error");
        killSwitch.finished();
        prepareTermination();
        state = TerminationState.ERROR;
        return platform.killComponent(e);
    }

    @Override
    public TerminationState getTerminationState() {
        return state;
    }
}
