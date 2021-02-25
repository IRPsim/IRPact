package de.unileipzig.irpact.commons.spatial;

/**
 * @author Daniel Abitz
 */
public interface DistanceEvaluator {

    boolean isDisabled();

    double evaluate(double distance);
}
