package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformMultiModule2_2<I, O, I2, O2, M extends Module2<I2, O2>>
        extends AbstractModule2<I, O>
        implements UniformMultiModule2<I, O, I2, O2> {

    protected M submodule1;
    protected M submodule2;

    public AbstractUniformMultiModule2_2() {
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
        getNonnullSubmodule1().setSharedData(sharedData);
        getNonnullSubmodule2().setSharedData(sharedData);
    }

    protected void setSharedDataThis(SharedModuleData sharedData) {
        this.sharedData = sharedData;
    }

    protected void validateSubmodules() throws Throwable {
        getNonnullSubmodule1().validate();
        getNonnullSubmodule2().validate();
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
        getNonnullSubmodule1().initialize(environment);
        getNonnullSubmodule2().initialize(environment);
    }

    protected abstract void initializeSelf(SimulationEnvironment environment) throws Throwable;

    @Override
    public int getSubmoduleCount() {
        return 2;
    }

    @Override
    public M getSubmodule(int index) {
        switch (index) {
            case 0:
                return getSubmodule1();
            case 1:
                return getSubmodule2();
            default:
                throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    public void setSubmodule1(M submodule1) {
        this.submodule1 = submodule1;
    }

    public M getSubmodule1() {
        return submodule1;
    }

    public M getNonnullSubmodule1() {
        M submodule = getSubmodule1();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }

    public void setSubmodule2(M submodule2) {
        this.submodule2 = submodule2;
    }

    public M getSubmodule2() {
        return submodule2;
    }

    public M getNonnullSubmodule2() {
        M submodule = getSubmodule2();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }
}
