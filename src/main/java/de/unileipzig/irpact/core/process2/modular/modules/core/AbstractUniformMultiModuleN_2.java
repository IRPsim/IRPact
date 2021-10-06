package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.util.ListSupplier;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformMultiModuleN_2<I, O, I2, O2, M extends Module2<I2, O2>>
        extends AbstractModule2<I, O>
        implements UniformMultiModule2<I, O, I2, O2> {

    protected final ListSupplier SUPPLIER;
    protected final List<M> MODULES;

    public AbstractUniformMultiModuleN_2() {
        this(ListSupplier.ARRAY);
    }

    public AbstractUniformMultiModuleN_2(ListSupplier supplier) {
        this.SUPPLIER = supplier;
        MODULES = supplier.newList();
    }

    @Override
    public void validate() throws Throwable {
        validateSubmodules();
        validateSelf();
    }

    protected void validateSubmodules() throws Throwable {
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).validate();
        }
    }

    protected abstract void validateSelf() throws Throwable;

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        initializeSubmodules(environment);
        initializeSelf(environment);
    }

    protected void initializeSubmodules(SimulationEnvironment environment) throws Throwable {
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).initialize(environment);
        }
    }

    protected abstract void initializeSelf(SimulationEnvironment environment) throws Throwable;

    @Override
    public int getSubmoduleCount() {
        return MODULES.size();
    }

    @Override
    public M getSubmodule(int index) {
        return MODULES.get(index);
    }

    public M getNonnullSubmodule(int index) {
        M submodule = getSubmodule(index);
        if(submodule == null) {
            throw new NullPointerException("index: " + index);
        }
        return submodule;
    }

    public void set(int index, M module) {
        while(MODULES.size() <= index + 1) {
            MODULES.add(null);
        }
        MODULES.set(index, module);
    }
}
