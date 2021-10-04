package de.unileipzig.irpact.core.process2.modular.modules.core;

/**
 * @author Daniel Abitz
 */
public interface MultiModule2<I, O, I2, O2> extends Module2<I, O> {

    int getSubmoduleCount();

    Module2<I2, O2> getSubmodule(int index);
}
