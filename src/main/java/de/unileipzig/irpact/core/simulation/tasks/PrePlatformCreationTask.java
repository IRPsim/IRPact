package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.core.misc.InitializationStage;

/**
 * @author Daniel Abitz
 */
public interface PrePlatformCreationTask extends InitalizationStageTask {

    @Override
    default InitializationStage getStage() {
        return InitializationStage.PRE_PLATFORM_CREATION;
    }
}
