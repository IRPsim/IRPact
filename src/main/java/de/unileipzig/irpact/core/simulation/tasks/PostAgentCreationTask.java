package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.misc.InitializationStage;

/**
 * Runs before the simulation is started.
 *
 * @author Daniel Abitz
 */
public interface PostAgentCreationTask extends InitalizationStageTask {

    @Override
    default InitializationStage getStage() {
        return InitializationStage.POST_AGENT_CREATION;
    }
}
