package de.unileipzig.irpact.v2.commons.time;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface Timestamp extends Comparable<Timestamp> {

    ZonedDateTime getTime();

    default boolean isAfter(Timestamp other) {
        return getTime().isAfter(other.getTime());
    }

    default boolean isAfterOrEquals(Timestamp other) {
        return isEqual(other) || isAfter(other);
    }

    default boolean isBefore(Timestamp other) {
        return getTime().isBefore(other.getTime());
    }

    default boolean isBeforeOrEqual(Timestamp other) {
        return isEqual(other) || isBefore(other);
    }

    default boolean isEqual(Timestamp other) {
        return getTime().isEqual(other.getTime());
    }
}
