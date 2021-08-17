package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface SimulationEntity extends Nameable {

    SimulationEnvironment getEnvironment();
}
