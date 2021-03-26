package de.unileipzig.irpact.commons.eval;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Eval extends ChecksumComparable {

    double evaluate(double x);
}
