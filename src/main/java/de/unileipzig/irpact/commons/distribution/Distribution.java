package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface Distribution<T> extends Nameable {

    default Distribution<T> copyDistribution() {
        throw new UnsupportedOperationException();
    }

    T drawValue();
}
