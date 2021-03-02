package de.unileipzig.irpact.commons.time;

import de.unileipzig.irpact.commons.IsEquals;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface Timestamp extends Comparable<Timestamp>, IsEquals {

    ZonedDateTime getTime();

    default long getEpochMilli() {
        return getTime().toInstant().toEpochMilli();
    }

    default boolean isBetween(Timestamp from, Timestamp to) {
        return isAfterOrEquals(from) && isBeforeOrEqual(to);
    }

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

    default boolean isNotEqual(Timestamp other) {
        return !isEqual(other);
    }
}
