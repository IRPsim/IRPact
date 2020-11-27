package de.unileipzig.irpact.core.misc;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InitializationData {

    String getName();

    SimulationEnvironment getEnvironment();
}
