package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentParentModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModuleWithNSubModules
        extends AbstractConsumerAgentModule
        implements ConsumerAgentParentModule {

    protected final List<ConsumerAgentModule> MODULE_LIST;
    protected final int NUMBER_OF_ELEMENTS;
    protected int nullElements;

    public AbstractConsumerAgentModuleWithNSubModules(int numberOfElements) {
        this.nullElements = numberOfElements;
        NUMBER_OF_ELEMENTS = numberOfElements;
        MODULE_LIST = createNullList(numberOfElements);
    }

    protected <R> List<R> createNullList(int numberOfElements) {
        List<R> list = new ArrayList<>();
        for(int i = 0; i < numberOfElements; i++) {
            list.add(null);
        }
        return list;
    }

    protected void updateModuleList(int index, ConsumerAgentModule module) {
        if(MODULE_LIST.get(index) == null && module != null) {
            nullElements--;
        }
        if(MODULE_LIST.get(index) != null && module == null) {
            nullElements++;
        }
        MODULE_LIST.set(index, module);
    }

    @Override
    public int numberOfModules() {
        return NUMBER_OF_ELEMENTS;
    }

    @Override
    public Stream<ConsumerAgentModule> streamModules() {
        return iterateModules().stream();
    }

    @Override
    public List<ConsumerAgentModule> iterateModules() {
        if(nullElements != 0) {
            throw new IllegalStateException("list contains null");
        }
        return MODULE_LIST;
    }
}
