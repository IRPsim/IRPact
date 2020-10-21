package de.unileipzig.irpact.v2.commons.time;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public final class ContinuousTimestamp implements Timestamp {

    private final long TIME_MS;
    private final ZonedDateTime TIME;

    public ContinuousTimestamp(long timeMs, ZonedDateTime time) {
        TIME_MS = timeMs;
        TIME = time;
    }

    public long getTimeMs() {
        return TIME_MS;
    }

    @Override
    public ZonedDateTime getTime() {
        return TIME;
    }

    @Override
    public int compareTo(Timestamp other) {
        return getTime().compareTo(other.getTime());
    }
}
