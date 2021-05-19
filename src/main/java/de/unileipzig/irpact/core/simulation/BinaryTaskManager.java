package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.util.data.BinaryData;
import de.unileipzig.irpact.core.misc.InitializationStage;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface BinaryTaskManager {

    void handle(Collection<? extends BinaryData> rawData);

    void handle(BinaryData data);

    void runInitializationStageTasks(InitializationStage stage, SimulationEnvironment environment);

    void runAllInitializationStageTasks(SimulationEnvironment environment);
}
