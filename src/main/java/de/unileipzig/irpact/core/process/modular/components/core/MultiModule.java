package de.unileipzig.irpact.core.process.modular.components.core;

import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface MultiModule<M extends Module> extends Module {

    int numberOfModules();

    Stream<M> streamModules();

    Iterable<M> iterateModules();
}
