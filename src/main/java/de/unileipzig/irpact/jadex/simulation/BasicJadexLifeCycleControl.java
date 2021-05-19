package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.log.InfoTag;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.simulation.tasks.Task;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgent;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
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
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexLifeCycleControl implements JadexLifeCycleControl {

    private static final Function<? super Timestamp, ? extends Set<SyncTask>> SET_CREATOR = ts -> new TreeSet<>(Task.PRIORITY_COMPARATOR);
    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControl.class);

    protected final Lock SYNC_LOCK = new ReentrantLock();
    protected final NavigableMap<Timestamp, Set<SyncTask>> syncTasks = new TreeMap<>(Comparable::compareTo);

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

    protected int nonFatalErrors = 0;
    protected int fatalErrors = 0;

    public BasicJadexLifeCycleControl() {
        killSwitch = new KillSwitch();
        killSwitch.setControl(this);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                ChecksumComparable.getChecksum(current),
                ChecksumComparable.getChecksum(controlAgent)
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

    public long getKillSwitchTimeout() {
        return killSwitch.getTimeout();
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

    @Override
    public void handleNonFatalError(Exception e) {
        nonFatalErrors++;
        LOGGER.error(InfoTag.NON_FATAL_ERROR + " non fatal error occurred, continue simulation", e);
    }

    @Override
    public void handleFatalError(Exception e) {
        fatalErrors++;
        LOGGER.error(InfoTag.FATAL_ERROR + " fatal error occurred, stop simulation", e);
        terminateWithError(e);
    }

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

    protected int countSyncTasks() {
        return syncTasks.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

    @Override
    public boolean registerSyncTask(Timestamp ts, SyncTask task) {
        if(isValid(ts)) {
            Set<SyncTask> taskList = syncTasks.computeIfAbsent(ts, SET_CREATOR);
            taskList.add(task);
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "add sync task '{}' at '{}' (total = {})", task.getName(), ts, countSyncTasks());
            return true;
        } else {
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "ignore invalid timestamp ({})", ts);
            return false;
        }
    }

    @Override
    public void waitForYearChangeIfRequired(Agent agent) {
        JadexTimeModel timeModel = environment.getTimeModel();
        if(!timeModel.hasYearChange()) {
            return;
        }

        SYNC_LOCK.lock();
        try {
            if(timeModel.hasYearChange()) {
                LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] perform year change", agent.getName());
                timeModel.performYearChange();
            }
        } finally {
            SYNC_LOCK.unlock();
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
                NavigableMap<Timestamp, Set<SyncTask>> tasks = syncTasks.headMap(now, true);
                LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{} @ {}] check for sync tasks: {}", agent.getName(), now, count(tasks));
                if(!tasks.isEmpty()) {
                    List<Timestamp> toRemove = new ArrayList<>();
                    for(Map.Entry<Timestamp, Set<SyncTask>> entry: tasks.entrySet()) {
                        toRemove.add(entry.getKey());
                        for(SyncTask task: entry.getValue()) {
                            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] execute sync task '{}' at '{}'", agent.getName(), task.getName(), entry.getKey());
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

    protected static int count(NavigableMap<Timestamp, Set<SyncTask>> tasks) {
        return tasks.isEmpty()
                ? 0
                : tasks.values()
                .stream()
                .mapToInt(Set::size)
                .sum();
    }
}
