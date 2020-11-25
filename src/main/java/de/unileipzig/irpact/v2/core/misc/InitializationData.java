package de.unileipzig.irpact.v2.core.misc;

import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InitializationData {

    String getName();

    SimulationEnvironment getEnvironment();
}
