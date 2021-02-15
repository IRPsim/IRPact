package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface SimulationTask extends Task {

    void run(SimulationEnvironment environment);
}
