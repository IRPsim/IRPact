package de.unileipzig.irpact.v2.io;

import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.v2.io.input.IRoot;

/**
 * @author Daniel Abitz
 */
public interface InputConverter {

    SimulationEnvironment build(IRoot input);
}
