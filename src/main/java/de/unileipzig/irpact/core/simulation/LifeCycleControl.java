package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.Initialization;

/**
 * Controls the live cycle of the simulation.
 *
 * @author Daniel Abitz
 */
public interface LifeCycleControl extends Initialization {

    void reportAgentCreated(Agent agent);

    void waitForCreationFinished() throws InterruptedException;

    void pause();

    void resume();

    void pulse();

    Object terminate();

    Object terminateTimeout();

    Object terminateWithError(Exception e);
}
