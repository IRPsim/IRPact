package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface Reevaluator<I> extends Nameable {

    void initializeReevaluator(SimulationEnvironment environment) throws Throwable;

    void reevaluate(I input, List<PostAction2> actions) throws Throwable;
}
