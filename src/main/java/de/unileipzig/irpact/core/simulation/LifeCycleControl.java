package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.Initialization;

/**
 * Controls the live cycle of the simulation.
 *
 * @author Daniel Abitz
 */
public interface LifeCycleControl extends Initialization {

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

    void addSynchronisationPoint(Timestamp ts);

    /**
     * Tests if the simulation requires a synchronisation step (e.g. global data update).
     *
     * @return true: yes
     */
    boolean requiresSynchronisation(Agent agent);

    /**
     * Waits until the synchronisation is finished.
     *
     * @return If there was an exception, if yes: cancel all.
     */
    boolean waitForSynchronisation(Agent agent);

    /**
     * Finish the synchronisation process.
     */
    void releaseSynchronisation();

    Object terminate();

    Object terminateTimeout();

    Object terminateWithError(Exception e);

    TerminationState getTerminationState();
}
