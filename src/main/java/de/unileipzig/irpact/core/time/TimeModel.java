package de.unileipzig.irpact.core.time;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.Initialization;

import java.time.Month;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface TimeModel extends Nameable, Initialization {

    /**
     * Special setup for time model.
     */
    void setupTimeModel();

    void setupNextYear();

    TimeMode getMode();

    Timestamp convert(ZonedDateTime zdt);

    int getYear();

    Timestamp now();

    int getStartYear();

    int getEndYearInclusive();

    Timestamp startTime();

    Timestamp endTime();

    boolean isValid(long delay);

    boolean isValid(Timestamp ts);

    boolean endTimeReached();

    //=========================
    //util
    //=========================

    Timestamp plusMillis(long millis);

    Timestamp plusMillis(Timestamp ts, long millis);

    Timestamp plusDays(Timestamp ts, long days);

    Timestamp plusYears(Timestamp ts, long years);

    Timestamp at(int year, Month month, int day);
}
