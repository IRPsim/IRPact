package de.unileipzig.irpact.commons.eval;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Eval extends IsEquals {

    double evaluate(double x);
}
