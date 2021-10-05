package de.unileipzig.irpact.core.process2.modular.modules.core;

/**
 * @author Daniel Abitz
 */
public interface UniformMultiModule2<I, O, I2, O2> extends MultiModule2_2<I, O> {

    int getSubmoduleCount();

    @SuppressWarnings("unchecked")
    Module2<I2, O2> getSubmodule(int index);
}
