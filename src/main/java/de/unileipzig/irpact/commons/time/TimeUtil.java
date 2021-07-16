package de.unileipzig.irpact.commons.time;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Daniel Abitz
 */
public final class TimeUtil {

    private static ZoneId zone = ZoneId.systemDefault();

    private TimeUtil() {
    }

    public static String printNowWithoutMs() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String print(ZonedDateTime zdt) {
        return zdt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static void setZone(ZoneId zone) {
        TimeUtil.zone = zone;
    }

    public static ZoneId getZone() {
        return zone;
    }

    public static ZonedDateTime msToTime(long epochMilli) {
        return ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(epochMilli),
                getZone()
        );
    }

    public static long timeToMs(ZonedDateTime time) {
        return time.toInstant().toEpochMilli();
    }

    public static ZonedDateTime startOfYear(int year) {
        return of(year, Month.JANUARY, 1);
    }

    public static ZonedDateTime endOfYear(int year) {
        ZonedDateTime zdt = startOfYear(year + 1);
        return zdt.minusNanos(1);
    }

    public static ZonedDateTime of(int year, long weeks) {
        ZonedDateTime zdt = startOfYear(year);
        return zdt.plusWeeks(weeks);
    }

    public static ZonedDateTime of(int year, Month month, int day) {
        return LocalDate.of(year, month, day)
                .atStartOfDay()
                .atZone(getZone());
    }
}
