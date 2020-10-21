package de.unileipzig.irpact.v2.commons.time;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public final class TickTimestamp implements Timestamp {

    private final double TICK;
    private final ZonedDateTime ZDT;

    public TickTimestamp(double tick, ZonedDateTime zdt) {
        TICK = tick;
        ZDT = zdt;
    }

    public double getTick() {
        return TICK;
    }

    @Override
    public ZonedDateTime getTime() {
        return ZDT;
    }

    @Override
    public int compareTo(Timestamp other) {
        return getTime().compareTo(other.getTime());
    }
}
