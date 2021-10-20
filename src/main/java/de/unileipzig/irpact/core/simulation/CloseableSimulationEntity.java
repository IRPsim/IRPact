package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface CloseableSimulationEntity extends Nameable {

    void closeEntity() throws Throwable;
}
