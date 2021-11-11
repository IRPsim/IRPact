package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public interface Module2<I, O> extends Nameable {

    Comparator<Module2<?, ?>> PRIORITY_COMPARATOR = Comparator.comparingInt(Module2::getPriority);

    int LOW_PRIORITY = 10;
    int NORM_PRIORITY = 5;
    int HIGH_PRIORITY = 1;

    int getPriority();

    default Module2<?, ?> containsLoop(Deque<Module2<?, ?>> currentPath, Set<Module2<?, ?>> allModules) {
        allModules.add(this);
        if(currentPath.contains(this)) {
            return this;
        } else {
            return null;
        }
    }

    void setSharedData(SharedModuleData sharedData);

    void validate() throws Throwable;

    void initialize(SimulationEnvironment environment) throws Throwable;
    
    void initializeNewInput(I input) throws Throwable;

    O apply(I input, List<PostAction2> actions) throws Throwable;
}
