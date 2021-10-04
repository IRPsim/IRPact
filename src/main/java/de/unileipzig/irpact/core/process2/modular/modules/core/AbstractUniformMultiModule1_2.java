package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractUniformMultiModule1_2<I, O, I2, O2, M extends Module2<I2, O2>>
        extends AbstractModule2<I, O>
        implements UniformMultiModule2<I, O, I2, O2> {

    protected M submodule;

    public AbstractUniformMultiModule1_2() {
    }

    @Override
    public void validate() throws Throwable {
        validateSubmodules();
        validateSelf();
    }

    protected void validateSubmodules() throws Throwable {
        getNonnullSubmodule().validate();
    }

    protected abstract void validateSelf() throws Throwable;

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        initalizeSubmodules(environment);
        initalizeSelf(environment);
    }

    protected void initalizeSubmodules(SimulationEnvironment environment) throws Throwable {
        getNonnullSubmodule().initialize(environment);
    }

    protected abstract void initalizeSelf(SimulationEnvironment environment) throws Throwable;

    @Override
    public int getSubmoduleCount() {
        return 1;
    }

    @Override
    public M getSubmodule(int index) {
        if(index == 0) {
            return getSubmodule();
        } else {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    public void setSubmodule(M submodule) {
        this.submodule = submodule;
    }

    public M getSubmodule() {
        return submodule;
    }

    public M getNonnullSubmodule() {
        M submodule = getSubmodule();
        if(submodule == null) {
            throw new NullPointerException();
        }
        return submodule;
    }
}
