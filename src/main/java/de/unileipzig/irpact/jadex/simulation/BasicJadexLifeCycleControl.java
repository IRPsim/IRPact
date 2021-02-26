package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgent;
import de.unileipzig.irpact.jadex.util.JadexSystemOut;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexLifeCycleControl implements JadexLifeCycleControl {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControl.class);

    protected final Lock SYNC_LOCK = new ReentrantLock();
    protected final NavigableMap<Timestamp, List<SyncTask>> syncTasks = new TreeMap<>(Comparable::compareTo);

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

    @Override
    public int getHashCode() {
        return Objects.hash(
                IsEquals.getHashCode(current),
                IsEquals.getHashCode(controlAgent)
        );
    }

    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }

    protected Timestamp now() {
        return environment.getTimeModel().now();
    }

    public Agent getControlAgent() {
        return controlAgent;
    }

    public void setControlAgent(Agent controlAgent) {
        this.controlAgent = controlAgent;
    }

    public Timestamp getCurrent() {
        return current;
    }

    public void setCurrent(Timestamp current) {
        this.current = current;
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

    //=========================
    //terminate
    //=========================

    protected void prepareTermination() {
        JadexSystemOut.redirect();
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

    //=========================
    //sync
    //=========================

    protected boolean isValid(Timestamp ts) {
        return environment.getTimeModel().isValid(ts);
    }

    @Override
    public boolean registerSyncTask(Timestamp ts, SyncTask task) {
        if(isValid(ts)) {
            List<SyncTask> taskList = syncTasks.computeIfAbsent(ts, _ts -> new ArrayList<>());
            taskList.add(task);
            LOGGER.debug(IRPSection.SIMULATION_LICECYCLE, "add sync task '{}' at '{}'", task.getName(), ts);
            return true;
        } else {
            LOGGER.debug(IRPSection.SIMULATION_LICECYCLE, "ignore invalid timestamp ({})", ts);
            return false;
        }
    }

    @Override
    public void waitForSynchronisationIfRequired(Agent agent) {
        if(syncTasks.isEmpty()) {
            return;
        }

        Timestamp now = now();
        if(current != now) {
            SYNC_LOCK.lock();
            try {
                if(current == now) {
                    return;
                }
                NavigableMap<Timestamp, List<SyncTask>> tasks = syncTasks.headMap(now, true);
                LOGGER.debug("[{} @ {}] check for sync tasks: {}", agent.getName(), now, count(tasks));
                if(!tasks.isEmpty()) {
                    List<Timestamp> toRemove = new ArrayList<>();
                    for(Map.Entry<Timestamp, List<SyncTask>> entry: tasks.entrySet()) {
                        toRemove.add(entry.getKey());
                        for(SyncTask task: entry.getValue()) {
                            LOGGER.debug(IRPSection.SIMULATION_LICECYCLE, "execute sync task '{}' at '{}'", task.getName(), entry.getKey());
                            task.run();
                        }
                    }
                    for(Timestamp ts: toRemove) {
                        syncTasks.remove(ts);
                    }
                }
                current = now;
            } finally {
                SYNC_LOCK.unlock();
            }
        }
    }

    protected static int count(NavigableMap<Timestamp, List<SyncTask>> tasks) {
        return tasks.isEmpty()
                ? 0
                : tasks.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }
}
