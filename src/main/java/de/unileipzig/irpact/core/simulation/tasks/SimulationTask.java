package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface SimulationTask extends BinaryTask {

    void run(SimulationEnvironment environment) throws Exception;
}
