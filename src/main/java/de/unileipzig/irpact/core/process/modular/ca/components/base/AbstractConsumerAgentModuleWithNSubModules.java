package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModuleWithNSubModules
        extends AbstractConsumerAgentModuleWithNGenericSubModules<ConsumerAgentModule> {

    public AbstractConsumerAgentModuleWithNSubModules(int numberOfElements) {
        super(numberOfElements);
    }
}
