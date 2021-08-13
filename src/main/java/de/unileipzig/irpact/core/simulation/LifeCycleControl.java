package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.util.function.Consumer;

/**
 * Controls the live cycle of the simulation.
 *
 * @author Daniel Abitz
 */
public interface LifeCycleControl extends InitalizablePart {

    /**
     * @author Daniel Abitz
     */
    enum TerminationState {
        RUNNING,
        NORMAL,
        TIMEOUT,
        ERROR
    }

    void startKillSwitch();

    void setTotalNumberOfAgents(int count);

    void reportAgentCreated(Agent agent);

    void waitForCreationFinished() throws InterruptedException;

    Object waitForTermination();

    void start();

    void pause();

    void resume();

    void pulse();

    //=========================
    //terminate
    //=========================

    void setOnHardKill(Consumer<? super Throwable> consumer);

    void terminationFinished();

    void handleNonFatalError(Throwable t);

    void handleFatalError(Throwable t);

    Object terminate();

    Object terminateTimeout();

    Object terminateWithError(Throwable t);

    TerminationState getTerminationState();

    //=========================
    //sync
    //=========================

    boolean registerSyncTaskAsFirstAction(Timestamp ts, SyncTask task);

    boolean registerSyncTaskAsLastAction(Timestamp ts, SyncTask task);

    boolean registerSyncTaskAsFirstAnnualAction(SyncTask task);

    boolean registerSyncTaskAsLastAnnualAction(SyncTask task);

    /**
     * Waits until the synchronisation is finished (if required).
     */
    void waitForYearChangeIfRequired(Agent agent);

    /**
     * Waits until the synchronisation is finished (if required).
     *
     * @param agent current agent
     */
    void waitForSynchronisationAtStartIfRequired(Agent agent);

    /**
     * Waits until the synchronisation is finished (if required).
     *
     * @param agent current agent
     */
    void waitForSynchronisationAtEndIfRequired(Agent agent);
}
