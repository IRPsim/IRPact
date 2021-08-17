package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.develop.AddToPersist;
import jadex.bridge.service.annotation.Reference;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@AddToPersist("besser adden mit den werten")
@Reference(local = true, remote = true)
public final class BasicTimestamp implements JadexTimestamp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final ZonedDateTime TIME;
    private final double clockTick;
    private final double tick;

    private String cachedSimple;
    private String cachedPretty;
    private String cachedComplex;

    public BasicTimestamp(long epochMilli) {
        this(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()));
    }

    public BasicTimestamp(ZonedDateTime time) {
        this(time, Double.NaN, Double.NaN);
    }

    public BasicTimestamp(ZonedDateTime time, double clockTick, double tick) {
        TIME = Objects.requireNonNull(time, "time");
        this.clockTick = clockTick;
        this.tick = tick;
    }

    public boolean hasTick() {
        return !Double.isNaN(tick);
    }

    @Override
    public ZonedDateTime getTime() {
        return TIME;
    }

    @Override
    public double getClockTick() {
        return clockTick;
    }

    @Override
    public double getTick() {
        return tick;
    }

    @Override
    public int compareTo(Timestamp other) {
        return getTime().compareTo(other.getTime());
    }

    @Override
    public String printSimple() {
        if(cachedSimple == null) {
            if(hasTick()) {
                cachedSimple = "Tick(" + tick + ")";
            } else {
                cachedSimple = "Time(" + FORMATTER.format(TIME) + ")";
            }
        }
        return cachedSimple;
    }

    @Override
    public String printPretty() {
        if(cachedPretty == null) {
            if(hasTick()) {
                cachedPretty = "Tick(" + tick + " | "+ FORMATTER.format(TIME) + ")";
            } else {
                cachedPretty = "Time(" + FORMATTER.format(TIME) + ")";
            }
        }
        return cachedPretty;
    }

    @Override
    public String printComplex() {
        if(cachedComplex == null) {
            if(hasTick()) {
                cachedComplex = "Tick(" + tick + " [" + clockTick + ", " + FORMATTER.format(TIME) + "])";
            } else {
                cachedComplex = "Time(" + FORMATTER.format(TIME) + ")";
            }
        }
        return cachedComplex;
    }

    @Override
    public String toString() {
        return printComplex();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicTimestamp)) return false;
        BasicTimestamp that = (BasicTimestamp) o;
        return Objects.equals(TIME, that.TIME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TIME);
    }

    @Override
    public int getChecksum() {
        return hashCode();
    }
}
