package de.unileipzig.irpact.commons.time;

import de.unileipzig.irpact.commons.IsEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Wandelt diskrete Zeitmomente (Ticks) in konkrete Datums- und Zeitangaben ({@link ZonedDateTime}) um.
 *
 * @author Daniel Abitz
 */
public final class TickConverter implements IsEquals {

    private int startYear;
    private long timePerTickInMs;
    private ZoneId zoneId;
    private double startTick;
    private ZonedDateTime startTime;
    private long startTimeMs;

    public TickConverter() {
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                timePerTickInMs,
                startTimeMs
        );
    }

    public TickConverter(int startYear, long timePerTickInMs) {
        this(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public TickConverter(int startYear, long timePerTickInMs, ZoneId zoneId) {
        init(startYear, timePerTickInMs, zoneId);
    }

    public void init(int startYear, long timePerTickInMs, ZoneId zoneId) {
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

    public ZonedDateTime getStartTime() {
        return startTime;
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
     * Wandelt den diskreten Zeitpunkt (tick) in einen Zeitstempel in MS um.
     *
     * @param tick Eingabetick
     * @return Zeitstempel in Millisekunden
     * @since 0.0
     */
    public long tickToTimestamp(double tick) {
        validate();
        double delta = tick - startTick;
        long deltaMs = (long) (delta * timePerTickInMs);
        return startTimeMs + deltaMs;
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

    public double ticksUntil(ZonedDateTime now, ZonedDateTime other) {
        long ms = now.until(other, ChronoUnit.MILLIS);
        return ms / (double) timePerTickInMs;
    }
}
