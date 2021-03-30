package de.unileipzig.irpact.commons.time;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface Timestamp extends Comparable<Timestamp>, ChecksumComparable {

    ZonedDateTime getTime();

    String printSimple();

    String printPretty();

    String printComplex();

    default long getEpochMilli() {
        return getTime().toInstant().toEpochMilli();
    }

    default int getYear() {
        return getTime().getYear();
    }

    default boolean isBetween(Timestamp from, Timestamp to) {
        return isAfterOrEquals(from) && isBefore(to);
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
