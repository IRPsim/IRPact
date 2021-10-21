package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InitializationHandler extends Nameable {

    void initalize(SimulationEnvironment environment) throws Throwable;
}
