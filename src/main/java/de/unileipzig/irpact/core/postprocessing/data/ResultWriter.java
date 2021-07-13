package de.unileipzig.irpact.core.postprocessing.data;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface ResultWriter {

    void applyAndWrite(SimulationEnvironment environment);
}
