package de.unileipzig.irpact.commons.checksum;

import java.util.Collection;
import java.util.Map;

/**
 * {@link #getChecksum(Object)} checks for collection or map and uses {@link #getCollectionChecksum(Collection)} or
 * {@link #getMapChecksum(Map)}.
 *
 * @author Daniel Abitz
 */
public class SmartChecksumCalculator extends ChecksumCalculator {

    public static final SmartChecksumCalculator INSTANCE = new SmartChecksumCalculator();

    public SmartChecksumCalculator() {
    }

    public int getSystemChecksum(Object value) {
        return value == null
                ? ChecksumComparable.NULL_CHECKSUM
                : System.identityHashCode(value);
    }

    @Override
    public int getChecksum(Object value) {
        if(value == null) {
            return ChecksumComparable.NULL_CHECKSUM;
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
}
