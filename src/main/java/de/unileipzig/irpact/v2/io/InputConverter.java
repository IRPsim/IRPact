package de.unileipzig.irpact.v2.io;

import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InputConverter {

    SimulationEnvironment build(Object input);
}
