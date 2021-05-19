package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.misc.InitializationStage;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * Runs before the simulation is started.
 *
 * @author Daniel Abitz
 */
public interface InitalizationStageTask extends BinaryTask {

    InitializationStage getStage();

    void run(SimulationEnvironment environment) throws Exception;
}
