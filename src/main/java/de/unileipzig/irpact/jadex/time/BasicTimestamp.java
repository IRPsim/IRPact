package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import jadex.bridge.service.annotation.Reference;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public final class BasicTimestamp implements JadexTimestamp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final ZonedDateTime TIME;
    private final double simulationTick;
    private final double normalizedTick;

    public BasicTimestamp(long epochMilli) {
        this(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()));
    }

    public BasicTimestamp(ZonedDateTime time) {
        this(time, Double.NaN, Double.NaN);
    }

    public BasicTimestamp(ZonedDateTime time, double simulationTick, double normalizedTick) {
        TIME = time;
        this.simulationTick = simulationTick;
        this.normalizedTick = normalizedTick;
    }

    @Override
    public ZonedDateTime getTime() {
        return TIME;
    }

    @Override
    public double getSimulationTick() {
        return simulationTick;
    }

    @Override
    public double getNormalizedTick() {
        return normalizedTick;
    }

    @Override
    public int compareTo(Timestamp other) {
        return getTime().compareTo(other.getTime());
    }

    @Override
    public String toString() {
        return  "TS"
                + "{"
                + FORMATTER.format(TIME)
                + "," + normalizedTick
                + "," + simulationTick
                + "}";
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
    public int getHashCode() {
        return hashCode();
    }
}
