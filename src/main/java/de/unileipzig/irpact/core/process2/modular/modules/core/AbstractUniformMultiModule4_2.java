package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformMultiModule4_2<I, O, I2, O2, M extends Module2<I2, O2>>
        extends AbstractModule2<I, O>
        implements UniformMultiModule2<I, O, I2, O2> {

    protected M submodule1;
    protected M submodule2;
    protected M submodule3;
    protected M submodule4;

    public AbstractUniformMultiModule4_2() {
    }

    @Override
    public void validate() throws Throwable {
        validateSubmodules();
        validateSelf();
    }

    protected void validateSubmodules() throws Throwable {
        getNonnullSubmodule1().validate();
        getNonnullSubmodule2().validate();
        getNonnullSubmodule3().validate();
        getNonnullSubmodule4().validate();
    }

    protected abstract void validateSelf() throws Throwable;

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        initializeSubmodules(environment);
        initializeSelf(environment);
    }

    protected void initializeSubmodules(SimulationEnvironment environment) throws Throwable {
        getNonnullSubmodule1().initialize(environment);
        getNonnullSubmodule2().initialize(environment);
        getNonnullSubmodule3().initialize(environment);
        getNonnullSubmodule4().initialize(environment);
    }

    protected abstract void initializeSelf(SimulationEnvironment environment) throws Throwable;

    @Override
    public int getSubmoduleCount() {
        return 4;
    }

    @Override
    public M getSubmodule(int index) {
        switch (index) {
            case 0:
                return getSubmodule1();
            case 1:
                return getSubmodule2();
            case 2:
                return getSubmodule3();
            case 3:
                return getSubmodule4();
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

    public void setSubmodule3(M submodule3) {
        this.submodule3 = submodule3;
    }

    public M getSubmodule3() {
        return submodule3;
    }

    public M getNonnullSubmodule3() {
        M submodule = getSubmodule3();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }

    public void setSubmodule4(M submodule4) {
        this.submodule4 = submodule4;
    }

    public M getSubmodule4() {
        return submodule4;
    }

    public M getNonnullSubmodule4() {
        M submodule = getSubmodule4();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }
}
