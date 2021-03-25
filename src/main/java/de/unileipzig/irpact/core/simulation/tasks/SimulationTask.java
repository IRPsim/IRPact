package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * Runs after the simulation is started.
 *
 * @author Daniel Abitz
 */
public interface SimulationTask extends BinaryTask {

    void run(SimulationEnvironment environment) throws Exception;
}
