package de.unileipzig.irpact.commons;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author Daniel Abitz
 */
public final class TimeUtil {

    public static final DateTimeFormatter FILE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();
    public static final DateTimeFormatter FILE_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .toFormatter();
    public static final DateTimeFormatter FILE_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(FILE_DATE_FORMATTER)
            .appendLiteral('_')
            .append(FILE_TIME_FORMATTER)
            .toFormatter();

    private TimeUtil() {
    }
}
