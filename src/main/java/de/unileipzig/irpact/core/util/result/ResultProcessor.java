package de.unileipzig.irpact.core.util.result;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface ResultProcessor {

    void apply(SimulationEnvironment environment);
}
