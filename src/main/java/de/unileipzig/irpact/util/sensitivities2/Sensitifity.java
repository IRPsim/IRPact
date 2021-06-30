package de.unileipzig.irpact.util.sensitivities2;

/**
 * @author Daniel Abitz
 */
public interface Sensitifity<I> {

    void apply(I instance, int numberOfSteps, int step);
}
