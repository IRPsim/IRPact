package de.unileipzig.irpact.v2.core.simulation;

import de.unileipzig.irpact.v2.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface SimulationEntity extends Nameable {

    SimulationEnvironment getEnvironment();
}
