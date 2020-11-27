package de.unileipzig.irpact.io;

import de.unileipzig.irpact.io.input.IRoot;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InputConverter {

    SimulationEnvironment build(IRoot input);
}
