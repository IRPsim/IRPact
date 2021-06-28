package de.unileipzig.irpact.core.start;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface IRPactRestoreUpdater extends IRPactInputParser {

    //=========================
    //util
    //=========================

    void setEnvironment(SimulationEnvironment environment);
}
