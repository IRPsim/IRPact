package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Comparator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface Reevaluator<I> extends Nameable {

    Comparator<Reevaluator<?>> PRIORITY_COMPARATOR = Comparator.comparingInt(Reevaluator::getPriority);

    int LOW_PRIORITY = 10;
    int NORM_PRIORITY = 5;
    int HIGH_PRIORITY = 1;

    int getPriority();

    void setSharedData(SharedModuleData sharedData);

    void initializeReevaluator(SimulationEnvironment environment) throws Throwable;

    void reevaluate(I input, List<PostAction2> actions) throws Throwable;
}
