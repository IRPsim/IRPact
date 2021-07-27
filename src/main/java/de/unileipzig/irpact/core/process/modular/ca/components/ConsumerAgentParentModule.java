package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.process.modular.components.core.ParentModule;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentParentModule extends ParentModule<ConsumerAgentModule>, ConsumerAgentModule {

    @Override
    default void handleMissingParametersRecursively(ConsumerAgentMPM model) {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.handleMissingParametersRecursively(model);
        }
    }

    @Override
    default void handleNewProductRecursively(Product newProduct) {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.handleNewProductRecursively(newProduct);
        }
    }

    //=========================
    //InitalizablePart
    //=========================

    @Override
    default void preAgentCreation() throws MissingDataException {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.preAgentCreation();
        }
    }

    @Override
    default void preAgentCreationValidation() throws ValidationException {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.preAgentCreationValidation();
        }
    }

    @Override
    default void postAgentCreation() throws MissingDataException, InitializationException {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.postAgentCreation();
        }
    }

    @Override
    default void postAgentCreationValidation() throws ValidationException {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.postAgentCreationValidation();
        }
    }

    @Override
    default void preSimulationStart() throws MissingDataException {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.preSimulationStart();
        }
    }

    @Override
    default void postSimulation() {
        for(ConsumerAgentModule subModule: iterateModules()) {
            subModule.postSimulation();
        }
    }
}
