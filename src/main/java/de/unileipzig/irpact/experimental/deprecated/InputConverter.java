package de.unileipzig.irpact.experimental.deprecated;

import de.unileipzig.irpact.experimental.deprecated.input.IRoot;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InputConverter {

    SimulationEnvironment build(IRoot input);
}
