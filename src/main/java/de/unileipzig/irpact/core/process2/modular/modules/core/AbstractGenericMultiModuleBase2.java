package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGenericMultiModuleBase2<I, O, I2>
        extends AbstractModule2<I, O>
        implements MultiModule2<I, O>, HelperAPI2 {

    public AbstractGenericMultiModuleBase2() {
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
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

        traceModuleInitalization();
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

    protected List<String> listSubmoduleNames() {
        return IntStream.range(0, getSubmoduleCount())
                .mapToObj(this::getSubmodule)
                .map(Nameable::getName)
                .collect(Collectors.toList());
    }

    @Override
    public abstract int getSubmoduleCount();

    @SuppressWarnings("unchecked")
    @Override
    public abstract Module2<I2, ?> getSubmodule(int index);

    public Module2<I2, ?> getNonnullSubmodule(int index) {
        Module2<I2, ?> submodule = getSubmodule(index);
        if(submodule == null) {
            throw new NullPointerException("index: " + index);
        }
        return submodule;
    }
}
