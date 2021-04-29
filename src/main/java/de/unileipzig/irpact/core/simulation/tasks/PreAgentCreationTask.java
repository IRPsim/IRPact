package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.misc.InitializationStage;

/**
 * @author Daniel Abitz
 */
public interface PreAgentCreationTask extends InitalizationStageTask {

    @Override
    default InitializationStage getStage() {
        return InitializationStage.PRE_AGENT_CREATION;
    }
}
