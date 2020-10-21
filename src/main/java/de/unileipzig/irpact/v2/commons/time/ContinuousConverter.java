package de.unileipzig.irpact.v2.commons.time;

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

    private final int startYear;
    private final ZoneId zoneId;
    private long simulationstartTimeMs;
    private ZonedDateTime startTime;
    private long startTimeInMs;

    public ContinuousConverter(int startYear) {
        this(startYear, ZoneId.systemDefault());
    }

    public ContinuousConverter(int startYear, ZoneId zoneId) {
        this.startYear = startYear;
        this.zoneId = zoneId;
    }

    public long getSimulationstartTimeMs() {
        return simulationstartTimeMs;
    }

    public void setStart(long startTimeMs) {
        this.simulationstartTimeMs = startTimeMs;
        startTime = LocalDate.of(startYear, 1, 1)
                .atStartOfDay(zoneId);
        startTimeInMs = startTime.toInstant().toEpochMilli();
    }

    private void validate() {
        if(startTime == null) {
            throw new IllegalStateException("not started");
        }
    }

    public ZonedDateTime toTime(long timeMs) {
        validate();
        return startTime.plus(timeMs - simulationstartTimeMs, ChronoUnit.MILLIS);
    }

    public long fromTime(ZonedDateTime time) {
        validate();
        return time.toInstant().toEpochMilli() - startTimeInMs;
    }
}
