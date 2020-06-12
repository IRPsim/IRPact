package de.unileipzig.irpact.experimental.todev.time;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Wandelt diskrete Zeitmomente (Ticks) in konkrete Datums- und Zeitangaben ({@link ZonedDateTime}) um.
 *
 * @author Daniel Abitz
 */
@ToDevelop
public final class TickConverter {

    private final int startYear;
    private final long timePerTickInMs;
    private final ZoneId zoneId;
    private double startTick;
    private ZonedDateTime startTime;
    private long startTimeMs;

    public TickConverter(int startYear, long timePerTickInMs) {
        this(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public TickConverter(int startYear, long timePerTickInMs, ZoneId zoneId) {
        this.startYear = startYear;
        this.timePerTickInMs = timePerTickInMs;
        this.zoneId = zoneId;
    }

    public void setStart(double startTick) {
        this.startTick = startTick;
        startTime = LocalDate.of(startYear, 1, 1)
                .atStartOfDay(zoneId);
        startTimeMs = startTime.toInstant().toEpochMilli();
    }

    private void validate() {
        if(startTime == null) {
            throw new IllegalStateException("not started");
        }
    }

    /**
     * Wandelt den diskreten Zeitpunkt (tick) in ein Datum mit Uhrzeit um.
     *
     * @param tick Eingabetick
     * @return entsprechendes Datum mit Uhrzeit
     * @since 0.0
     */
    public ZonedDateTime tickToTime(double tick) {
        validate();
        double delta = tick - startTick;
        long deltaMs = (long) (delta * timePerTickInMs);
        return startTime.plus(deltaMs, ChronoUnit.MILLIS);
    }

    /**
     * Wandlet das Datum in den entsprechenden Tick um.
     *
     * @param time Eingabedatum
     * @return entsprechende Tick
     * @since 0.0
     */
    public double timeToTick(ZonedDateTime time) {
        validate();
        long timeMs = time.toInstant().toEpochMilli();
        return timestampToTick(timeMs);
    }

    /**
     * Wandelt den uebergebenden Zeitstempel in einen Tick um.
     *
     * @param timestampMs Zeitstempel in ms
     * @return entsprechende Tick
     * @since 0.0
     */
    public double timestampToTick(long timestampMs) {
        validate();
        double delta = (timestampMs - startTimeMs) / (double) timePerTickInMs;
        return delta + startTick;
    }

    /**
     * Addiert eine Tickanzahl zu einem Datum.
     *
     * @param input Eingabedatum
     * @param tickDelta Anzahl der zu addierenden Ticks
     * @return resultierende Datum
     * @since 0.0
     */
    public ZonedDateTime addTicks(ZonedDateTime input, double tickDelta) {
        return tickToTime(timeToTick(input) + tickDelta);
    }
}
