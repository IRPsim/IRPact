package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModuleWithNGenericSubModules<M extends ConsumerAgentModule>
        extends AbstractConsumerAgentModuleWithGenericSubModules<M> {

    protected final int NUMBER_OF_ELEMENTS;
    protected int nullElements;

    public AbstractConsumerAgentModuleWithNGenericSubModules(int numberOfElements) {
        super(numberOfElements);
        this.nullElements = numberOfElements;
        NUMBER_OF_ELEMENTS = numberOfElements;
        initNullList();
    }

    protected final void initNullList() {
        while(MODULE_LIST.size() < NUMBER_OF_ELEMENTS) {
            MODULE_LIST.add(null);
        }
    }

    protected void setSubModule(int index, M module) {
        if(MODULE_LIST.get(index) == null && module != null) {
            nullElements--;
        }
        if(MODULE_LIST.get(index) != null && module == null) {
            nullElements++;
        }
        MODULE_LIST.set(index, module);
    }

    protected M getSubModule(int index) {
        return MODULE_LIST.get(index);
    }

    @SuppressWarnings("unchecked")
    protected <R> R getSubModuleAs(int index) {
        return (R) getSubModule(index);
    }

    protected M getValidSubModule(int index) {
        M module = MODULE_LIST.get(index);
        if(module == null) {
            throw new NoSuchElementException("missing module at index " + index);
        }
        return module;
    }

    @SuppressWarnings("unchecked")
    protected <R> R getValidSubModuleAs(int index) {
        return (R) getValidSubModule(index);
    }

    @Override
    public int numberOfModules() {
        return NUMBER_OF_ELEMENTS;
    }

    @Override
    public List<M> iterateModules() {
        if(nullElements != 0) {
            throw new IllegalStateException("list contains null (" + getName() + " @ " + getClass() + ")");
        }
        return MODULE_LIST;
    }
}
