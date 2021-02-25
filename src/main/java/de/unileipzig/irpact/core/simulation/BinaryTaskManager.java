package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.BinaryData;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface BinaryTaskManager {

    void handle(Collection<? extends BinaryData> rawData);

    void handle(BinaryData data);

    void runAppTasks();

    void runSimulationTasks(SimulationEnvironment environment);
}
