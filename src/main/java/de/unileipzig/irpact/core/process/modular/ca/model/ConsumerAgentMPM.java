package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ModularProcessModel;
import de.unileipzig.irpact.core.process.modular.ModulePlan;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentMPM extends ModularProcessModel {

    ConsumerAgentEvaluationModule getStartModule();

    void handleRestoredPlan(ModulePlan plan);

    //=========================
    //InitalizablePart
    //=========================

    @Override
    default void preAgentCreation() throws MissingDataException {
        getStartModule().preAgentCreation();
    }

    @Override
    default void preAgentCreationValidation() throws ValidationException {
        getStartModule().preAgentCreationValidation();
    }

    @Override
    default void postAgentCreation() throws MissingDataException, InitializationException {
        getStartModule().postAgentCreation();
    }

    @Override
    default void postAgentCreationValidation() throws ValidationException {
        getStartModule().postAgentCreationValidation();
    }

    @Override
    default void preSimulationStart() throws MissingDataException {
        getStartModule().preSimulationStart();
    }

    @Override
    default void postSimulation() {
        getStartModule().postSimulation();
    }
}
