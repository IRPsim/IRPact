package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModuleWithSubModules
        extends AbstractConsumerAgentModuleWithGenericSubModules<ConsumerAgentModule> {

    public AbstractConsumerAgentModuleWithSubModules() {
        super();
    }

    public AbstractConsumerAgentModuleWithSubModules(int numberOfElements) {
        super(numberOfElements);
    }
}
