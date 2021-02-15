package de.unileipzig.irpact.core.simulation.tasks;

/**
 * @author Daniel Abitz
 */
public interface NonSimulationTask extends Task {

    void run() throws Exception;
}
