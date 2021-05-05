package de.unileipzig.irpact.commons.eval;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public class Inverse implements Eval {

    public Inverse() {
    }

    @Override
    public double evaluate(double x) {
        return 1.0 / x;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.NONNULL_CHECKSUM;
    }
}
