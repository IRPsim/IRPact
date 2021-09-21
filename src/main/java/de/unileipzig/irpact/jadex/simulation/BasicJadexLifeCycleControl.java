package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.exception.TerminationException;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("DefaultAnnotationParam")
@Reference(local = true, remote = true)
public class BasicJadexLifeCycleControl implements JadexLifeCycleControl {

    private static final Function<? super Timestamp, ? extends Set<SyncTask>> SET_CREATOR = ts -> new TreeSet<>(Task.PRIORITY_COMPARATOR);
    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLifeCycleControl.class);

    protected final Lock SYNC_LOCK = new ReentrantLock();
    protected final NavigableMap<Timestamp, Set<SyncTask>> syncTasks = new TreeMap<>(Comparable::compareTo);

    protected final Lock LAST_SYNC_LOCK = new ReentrantLock();
    protected final NavigableMap<Timestamp, Set<SyncTask>> lastSyncTasks = new TreeMap<>(Comparable::compareTo);
    protected CountDownLatch lastLatch;

    protected final Set<SyncTask> newYearTasks = new TreeSet<>(Task.PRIORITY_COMPARATOR);
    protected final Set<SyncTask> lastYearTasks = new TreeSet<>(Task.PRIORITY_COMPARATOR);

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

    protected TerminationState state = TerminationState.RUNNING;

    protected final long HARD_KILL_TIMER = TimeUnit.MINUTES.toMillis(5);
    protected int nonFatalErrors = 0;
    protected int fatalErrors = 0;
    protected Thread hardKillThread;
    protected Consumer<? super Throwable> onHardKill;

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

    public int getTotalNumberOfAgents() {
        return totalNumberOfAgents;
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

    public void setKillSwitchTimeout(long timeout, TimeUnit unit) {
        killSwitch.setTimeout(timeout, unit);
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
    public void setOnHardKill(Consumer<? super Throwable> onHardKill) {
        this.onHardKill = onHardKill;
    }

    @Override
    public void terminationFinished() {
        if(hardKillThread != null) {
            hardKillThread.interrupt();
            hardKillThread = null;
        }
    }

    @Override
    public void handleNonFatalError(Throwable t) {
        nonFatalErrors++;
        LOGGER.error(InfoTag.NON_FATAL_ERROR + " non fatal error occurred, continue simulation", t);
    }

    @Override
    public void handleFatalError(Throwable t) {
        fatalErrors++;
        LOGGER.error(InfoTag.FATAL_ERROR + " fatal error occurred, stop simulation", t);
        terminateWithError(t);
    }

    @Override
    public void prepareTermination() {
        killSwitch.cancel();
        JadexSystemOut.redirect();
    }

    @Override
    public IFuture<Map<String, Object>> terminate() {
        LOGGER.info("terminate");
        prepareTermination();
        state = TerminationState.NORMAL;
        return platform.killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateTimeout() {
        LOGGER.info("terminate with timeout");
        JadexSystemOut.redirect();
        state = TerminationState.TIMEOUT;
        //timeoutexception einbauen?
        return killPlatform(killSwitch.createException());
    }

    @Override
    public IFuture<Map<String, Object>> terminateWithError(Throwable t) {
        LOGGER.info("terminate with error");
        prepareTermination();
        state = TerminationState.ERROR;
        //map to exception for jadex
        Exception e = t instanceof Exception
                ? (Exception) t
                : new TerminationException(t);
        return killPlatform(e);
    }

    private IFuture<Map<String, Object>> killPlatform(Exception e) {
        startHardKill(e);
        return platform.killComponent(e);
    }

    @Override
    public TerminationState getTerminationState() {
        return state;
    }

    protected void startHardKill(Throwable cause) {
        final Consumer<? super Throwable> onHardKill = this.onHardKill;
        LOGGER.warn("prepare hard kill measure (time: {})", HARD_KILL_TIMER);
        hardKillThread = new Thread(() -> {
            try {
                Thread.sleep(HARD_KILL_TIMER);
                if(onHardKill != null) {
                    onHardKill.accept(cause);
                } else {
                    LOGGER.warn("no hard kill callback found, calling System.exit");
                    System.exit(1);
                }
            } catch (InterruptedException e) {
                LOGGER.info("hard kill canceled");
            }
        }, "HardKill-Thread");
        hardKillThread.start();
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
    public boolean registerSyncTaskAsFirstAction(Timestamp ts, SyncTask task) {
        if(isValid(ts)) {
            Set<SyncTask> taskList = syncTasks.computeIfAbsent(ts, SET_CREATOR);
            taskList.add(task);
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "add 'run first' sync task '{}' at '{}' (total = {})", task.getName(), ts, countSyncTasks());
            return true;
        } else {
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "ignore invalid timestamp ({})", ts);
            return false;
        }
    }

    @Override
    public boolean registerSyncTaskAsLastAction(Timestamp ts, SyncTask task) {
        if(isValid(ts)) {
            Set<SyncTask> taskList = lastSyncTasks.computeIfAbsent(ts, SET_CREATOR);
            taskList.add(task);
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "add 'run last' sync task '{}' at '{}' (total = {})", task.getName(), ts, countSyncTasks());
            return true;
        } else {
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "ignore invalid timestamp ({})", ts);
            return false;
        }
    }

    @Override
    public boolean registerSyncTaskAsFirstAnnualAction(SyncTask task) {
        return newYearTasks.add(task);
    }

    @Override
    public boolean registerSyncTaskAsLastAnnualAction(SyncTask task) {
        return lastYearTasks.add(task);
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
                timeModel.performYearChange(
                        year -> {
                            LOGGER.trace("end-of-year-tasks for year {}: {}", year, lastYearTasks.size());
                            for(SyncTask task: lastYearTasks) {
                                LOGGER.trace("execute task '{}'", task.getName());
                                task.run();
                            }
                        },
                        year -> {
                            if(timeModel.endTimeReached()) {
                                LOGGER.trace("start-of-year-tasks for year {}: END REACHED", year);
                            } else {
                                LOGGER.trace("start-of-year-tasks for year {}: {}", year, newYearTasks.size());
                                for(SyncTask task: newYearTasks) {
                                    LOGGER.trace("execute task '{}'", task.getName());
                                    task.run();
                                }
                            }
                        }
                );
            }
        } finally {
            SYNC_LOCK.unlock();
        }
    }

    @Override
    public void waitForSynchronisationAtStartIfRequired(Agent agent) {
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
                    for(Map.Entry<Timestamp, Set<SyncTask>> entry: tasks.entrySet()) {
                        for(SyncTask task: entry.getValue()) {
                            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] execute 'first' sync task '{}' at '{}'", agent.getName(), task.getName(), entry.getKey());
                            task.run();
                        }
                    }
                    tasks.clear();
                }
                current = now;
            } finally {
                SYNC_LOCK.unlock();
            }
        }
    }

    public void waitForSynchronisationAtEndIfRequired(Agent agent) {
        if(lastSyncTasks.isEmpty()) {
            return;
        }

        Timestamp now = now();
        NavigableMap<Timestamp, Set<SyncTask>> tasks = syncTasks.headMap(now, true);
        if(tasks.isEmpty()) {
            return;
        }

        CountDownLatch latch = getLatch();

        boolean isLast;
        LAST_SYNC_LOCK.lock();
        try {
            //lock noetig?
            isLast = latch.getCount() == 1L;
        } finally {
            LAST_SYNC_LOCK.unlock();
        }

        if(isLast) {
            for(Map.Entry<Timestamp, Set<SyncTask>> entry: tasks.entrySet()) {
                for(SyncTask task: entry.getValue()) {
                    LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] execute 'last' sync task' '{}' at '{}'", agent.getName(), task.getName(), entry.getKey());
                    task.run();
                }
            }
        }

        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            terminateWithError(e);
        }

        if(isLast) {
            resetLatch();
        }
    }

    protected CountDownLatch getLatch() {
        if(lastLatch == null) {
            LAST_SYNC_LOCK.lock();
            try {
                if(lastLatch == null) {
                    resetLatch();
                }
            } finally {
                LAST_SYNC_LOCK.unlock();
            }
        }
        return lastLatch;
    }

    protected void resetLatch() {
        lastLatch = new CountDownLatch(getTotalNumberOfAgents());
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
