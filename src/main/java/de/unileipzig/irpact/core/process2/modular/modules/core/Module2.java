package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface Module2<I, O> {

    void validate() throws Throwable;

    void initialize(SimulationEnvironment environment) throws Throwable;

    O apply(I input, List<PostAction2> actions) throws Throwable;
}
