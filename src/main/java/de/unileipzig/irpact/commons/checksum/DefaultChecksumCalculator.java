package de.unileipzig.irpact.commons.checksum;

import de.unileipzig.irpact.commons.Nameable;

import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class DefaultChecksumCalculator extends ChecksumCalculator {

    public static final DefaultChecksumCalculator INSTANCE = new DefaultChecksumCalculator();
    private static final Function<? super Nameable, ?> GET_NAMED_CHECKSUM = INSTANCE::getNamedChecksum;

    public DefaultChecksumCalculator() {
    }

    @Override
    public int getChecksum(Object value) {
        if(value == null) {
            return NUll_CHECKSUM;
        }
        if(value instanceof ChecksumComparable) {
            return ((ChecksumComparable) value).getChecksum();
        }
        return value.hashCode();
    }

    public static <T extends Nameable> Function<? super T, ?> getNamedChecksumFunction() {
        return GET_NAMED_CHECKSUM;
    }
}
