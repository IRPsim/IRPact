package de.unileipzig.irpact.core.process2.modular.modules.core;

/**
 * @author Daniel Abitz
 */
public interface MultiModule2_2<I, O> extends Module2<I, O> {

    int getSubmoduleCount();

    <A, B> Module2<A, B> getSubmodule(int index);
}
