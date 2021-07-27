package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.process.modular.components.core.MultiModule;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentMultiModule extends MultiModule<ConsumerAgentModule>, ConsumerAgentModule {

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

    @Override
    default ConsumerAgentModule searchModule(String name) {
        if(Objects.equals(getName(), name)) {
            return this;
        } else {
            for(ConsumerAgentModule subModule: iterateModules()) {
                ConsumerAgentModule found = subModule.searchModule(name);
                if(found != null) {
                    return found;
                }
            }
            return null;
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
