package de.unileipzig.irpact.commons.spatial;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface DistanceEvaluator extends IsEquals {

    boolean isDisabled();

    double evaluate(double distance);
}
