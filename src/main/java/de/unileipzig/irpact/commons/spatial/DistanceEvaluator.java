package de.unileipzig.irpact.commons.spatial;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface DistanceEvaluator extends ChecksumComparable {

    boolean isDisabled();

    double evaluate(double distance);
}
