package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public interface SimulationControl {

    void reportAgentCreation();

    void waitForCreationFinished() throws InterruptedException;

    Object terminate();

    Object terminateWithError(Exception e);
}
