package de.unileipzig.irpact.core.process.modular.components.core;

/**
 * @author Daniel Abitz
 */
public interface CalculationModule<I> extends Module {

    double calculate(I input) throws Throwable;
}
