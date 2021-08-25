package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentMultiModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModuleWithGenericSubModules<M extends ConsumerAgentModule>
        extends AbstractConsumerAgentModule
        implements ConsumerAgentMultiModule<M> {

    protected final List<M> MODULE_LIST;

    public AbstractConsumerAgentModuleWithGenericSubModules() {
        this(-1);
    }

    public AbstractConsumerAgentModuleWithGenericSubModules(int numberOfElements) {
        MODULE_LIST = createList(numberOfElements);
    }

    protected final <R> List<R> createList(int numberOfElements) {
        if(numberOfElements < 1) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(numberOfElements);
        }
    }

    protected void addSubModule(M subModule) {
        MODULE_LIST.add(subModule);
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.preAgentCreation();
            }
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.preAgentCreationValidation();
            }
        }
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.postAgentCreation();
            }
        }
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.postAgentCreationValidation();
            }
        }
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.preSimulationStart();
            }
        }
    }

    @Override
    public void postSimulation() {
        for(M submodule: MODULE_LIST) {
            if(submodule != null) {
                submodule.postSimulation();
            }
        }
    }

    @Override
    public int numberOfModules() {
        return MODULE_LIST.size();
    }

    @Override
    public Stream<M> streamModules() {
        return iterateModules().stream();
    }

    @Override
    public List<M> iterateModules() {
        return MODULE_LIST;
    }
}
