package de.unileipzig.irpact.commons.checksum;

import de.unileipzig.irpact.commons.Nameable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * {@link #getChecksum(Object)} checks for collection or map and uses {@link #getCollectionChecksum(Collection)} or
 * {@link #getMapChecksum(Map)}.
 *
 * @author Daniel Abitz
 */
public class SmartChecksumCalculator extends ChecksumCalculator {

    public static final SmartChecksumCalculator INSTANCE = new SmartChecksumCalculator();
    private static final Function<? super Nameable, ?> GET_NAMED_CHECKSUM = INSTANCE::getNamedChecksum;

    public SmartChecksumCalculator() {
    }

    @Override
    public int getChecksum(Object value) {
        if(value == null) {
            return NUll_CHECKSUM;
        }
        if(value instanceof ChecksumComparable) {
            return ((ChecksumComparable) value).getChecksum();
        }
        if(value instanceof Collection<?>) {
            return getCollectionChecksum((Collection<?>) value);
        }
        if(value instanceof Map<?, ?>) {
            return getMapChecksum((Map<?, ?>) value);
        }
        return value.hashCode();
    }

    public static <T extends Nameable> Function<? super T, ?> getNamedChecksumFunction() {
        return GET_NAMED_CHECKSUM;
    }
}
