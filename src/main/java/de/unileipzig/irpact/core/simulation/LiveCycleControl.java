package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.Agent;

/**
 * Controls the live cycle of the simulation.
 *
 * @author Daniel Abitz
 */
public interface LiveCycleControl {

    void reportAgentCreated(Agent agent);

    void waitForCreationFinished() throws InterruptedException;

    Object terminate();

    Object terminateWithError(Exception e);
}
