package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ModularProcessModel;
import de.unileipzig.irpact.core.process.modular.ModularProcessPlan;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentPostAction;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentMPM extends ModularProcessModel {

    ConsumerAgentEvaluationModule getStartModule();

    void execute(ConsumerAgentPostAction action) throws Throwable;

    void handleRestoredPlan(ModularProcessPlan plan);

    @Override
    default ConsumerAgentModule searchModule(String name) {
        ConsumerAgentEvaluationModule startModule = getStartModule();
        return startModule == null ? null : startModule.searchModule(name);
    }

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
