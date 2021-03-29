package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.InitalizablePart;

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
        NOT,
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

    Object terminate();

    Object terminateTimeout();

    Object terminateWithError(Exception e);

    TerminationState getTerminationState();

    //=========================
    //sync
    //=========================

    boolean registerSyncTask(Timestamp ts, SyncTask task);

    /**
     * Waits until the synchronisation is finished (if required).
     */
    void waitForSynchronisationIfRequired(Agent agent);
}
