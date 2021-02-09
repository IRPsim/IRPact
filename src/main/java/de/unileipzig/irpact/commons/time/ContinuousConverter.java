package de.unileipzig.irpact.commons.time;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Wandelt diskrete Zeitmomente (Ticks) in konkrete Datums- und Zeitangaben ({@link ZonedDateTime}) um.
 *
 * @author Daniel Abitz
 */
public final class ContinuousConverter {

    private int startYear;
    private ZoneId zoneId;
    private long simulationStartTimeMs;
    private ZonedDateTime startTime;
    private long startTimeInMs;

    public ContinuousConverter() {
    }

    public ContinuousConverter(int startYear) {
        this(startYear, ZoneId.systemDefault());
    }

    public ContinuousConverter(int startYear, ZoneId zoneId) {
        init(startYear, zoneId);
    }

    public void init(int startYear, ZoneId zoneId) {
        this.startYear = startYear;
        this.zoneId = zoneId;
    }

    public long getSimulationStartTimeMs() {
        return simulationStartTimeMs;
    }

    public void setStart(long startTimeMs) {
        this.simulationStartTimeMs = startTimeMs;
        startTime = LocalDate.of(startYear, 1, 1)
                .atStartOfDay(zoneId);
        startTimeInMs = startTime.toInstant().toEpochMilli();
    }

    private void validate() {
        if(startTime == null) {
            throw new IllegalStateException("not started");
        }
    }

    public ZonedDateTime timeToDate(long timeMs) {
        validate();
        return startTime.plus(timeMs - simulationStartTimeMs, ChronoUnit.MILLIS);
    }

    public long dateToTime(ZonedDateTime time) {
        validate();
        return time.toInstant().toEpochMilli() - startTimeInMs + simulationStartTimeMs;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public long timeBetween(ZonedDateTime now, ZonedDateTime other) {
        return now.until(other, ChronoUnit.MILLIS);
    }
}
