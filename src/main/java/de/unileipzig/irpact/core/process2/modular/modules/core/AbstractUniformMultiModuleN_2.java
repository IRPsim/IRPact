package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.ListSupplier;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;
import java.util.stream.Collectors;

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

    protected void sortPriority() {
        MODULES.sort(PRIORITY_COMPARATOR);
    }

    @Override
    public void validate() throws Throwable {
        validateSubmodules();
        validateSelf();
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        setSharedDataThis(sharedData);
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).setSharedData(sharedData);
        }
    }

    protected void setSharedDataThis(SharedModuleData sharedData) {
        this.sharedData = sharedData;
    }

    protected void validateSubmodules() throws Throwable {
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).validate();
        }
    }

    protected abstract void validateSelf() throws Throwable;

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        initializeSelf(environment);
        initializeSubmodules(environment);
        setInitalized();
    }

    protected void initializeSubmodules(SimulationEnvironment environment) throws Throwable {
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).initialize(environment);
        }
    }

    protected abstract void initializeSelf(SimulationEnvironment environment) throws Throwable;

    @Override
    public void initializeNewInput(I input) throws Throwable {
        traceNewInput(input);
        initializeNewInputSelf(input);
        initializeNewInputSubmodules(input);
    }

    protected void initializeNewInputSubmodules(I input) throws Throwable {
        I2 input2 = castInput(input);
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).initializeNewInput(input2);
        }
    }

    protected abstract I2 castInput(I input);

    protected abstract void initializeNewInputSelf(I input) throws Throwable;

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        traceModuleSetup();
        setupSelf(environment);
        setupSubmodules(environment);
        setSetupCalled();
    }

    protected void setupSubmodules(SimulationEnvironment environment) throws Throwable {
        for(int i = 0; i < getSubmoduleCount(); i++) {
            getNonnullSubmodule(i).setup(environment);
        }
    }

    protected abstract void setupSelf(SimulationEnvironment environment) throws Throwable;

    protected List<String> listSubmoduleNames() {
        return MODULES.stream()
                .map(Nameable::getName)
                .collect(Collectors.toList());
    }

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

    public void add(M module) {
        MODULES.add(module);
    }

    public void set(int index, M module) {
        while(MODULES.size() <= index + 1) {
            MODULES.add(null);
        }
        MODULES.set(index, module);
    }
}
