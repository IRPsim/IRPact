package de.unileipzig.irpact.core.simulation.tasks;

/**
 * Runs before the simulation is started.
 *
 * @author Daniel Abitz
 */
public interface AppTask extends BinaryTask {

    void run() throws Exception;
}
