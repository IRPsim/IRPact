package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Comparator;

/**
 * @author Daniel Abitz
 */
public interface InitializationHandler extends Nameable {

    Comparator<InitializationHandler> PRIORITY_COMPARATOR = Comparator.comparingInt(InitializationHandler::getPriority);

    int LOW_PRIORITY = 10;
    int NORM_PRIORITY = 5;
    int HIGH_PRIORITY = 1;

    int getPriority();

    void initalize(SimulationEnvironment environment) throws Throwable;
}
