package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformMultiModule6_2<I, O, I2, O2, M extends Module2<I2, O2>>
        extends AbstractModule2<I, O>
        implements UniformMultiModule2<I, O, I2, O2> {

    protected M submodule1;
    protected M submodule2;
    protected M submodule3;
    protected M submodule4;
    protected M submodule5;
    protected M submodule6;

    public AbstractUniformMultiModule6_2() {
    }

    @Override
    public void validate() throws Throwable {
        validateSubmodules();
        validateSelf();
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        traceSetSharedData();
        super.setSharedData(sharedData);
        setSharedDataThis(sharedData);
        getNonnullSubmodule1().setSharedData(sharedData);
        getNonnullSubmodule2().setSharedData(sharedData);
        getNonnullSubmodule3().setSharedData(sharedData);
        getNonnullSubmodule4().setSharedData(sharedData);
        getNonnullSubmodule5().setSharedData(sharedData);
        getNonnullSubmodule6().setSharedData(sharedData);
    }

    protected void setSharedDataThis(SharedModuleData sharedData) {
        this.sharedData = sharedData;
    }

    protected void validateSubmodules() throws Throwable {
        getNonnullSubmodule1().validate();
        getNonnullSubmodule2().validate();
        getNonnullSubmodule3().validate();
        getNonnullSubmodule4().validate();
        getNonnullSubmodule5().validate();
        getNonnullSubmodule6().validate();
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
        getNonnullSubmodule3().initialize(environment);
        getNonnullSubmodule4().initialize(environment);
        getNonnullSubmodule5().initialize(environment);
        getNonnullSubmodule6().initialize(environment);
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
        getNonnullSubmodule1().initializeNewInput(input2);
        getNonnullSubmodule2().initializeNewInput(input2);
        getNonnullSubmodule3().initializeNewInput(input2);
        getNonnullSubmodule4().initializeNewInput(input2);
        getNonnullSubmodule5().initializeNewInput(input2);
        getNonnullSubmodule6().initializeNewInput(input2);
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
        getNonnullSubmodule1().setup(environment);
        getNonnullSubmodule2().setup(environment);
        getNonnullSubmodule3().setup(environment);
        getNonnullSubmodule4().setup(environment);
        getNonnullSubmodule5().setup(environment);
        getNonnullSubmodule6().setup(environment);
    }

    protected abstract void setupSelf(SimulationEnvironment environment) throws Throwable;

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
            case 4:
                return getSubmodule5();
            case 5:
                return getSubmodule6();
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

    public void setSubmodule5(M submodule5) {
        this.submodule5 = submodule5;
    }

    public M getSubmodule5() {
        return submodule5;
    }

    public M getNonnullSubmodule5() {
        M submodule = getSubmodule5();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }

    public void setSubmodule6(M submodule6) {
        this.submodule6 = submodule6;
    }

    public M getSubmodule6() {
        return submodule6;
    }

    public M getNonnullSubmodule6() {
        M submodule = getSubmodule6();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }
}
