package de.unileipzig.irpact.util.sensitivities;

/**
 * @author Daniel Abitz
 */
public interface Sensitifity<I> {

    double[] getDeltas(int count);

    double getDelta(int count, int step);

    void apply(I input, double delta);
}
