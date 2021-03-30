package de.unileipzig.irpact.core.time;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.time.Month;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface TimeModel extends Nameable, InitalizablePart {

    /**
     * Special setup for time model.
     */
    void setupTimeModel();

    TimeMode getMode();

    Timestamp convert(ZonedDateTime zdt);

    int getCurrentYear();

    Timestamp now();

    int getFirstSimulationYear();

    int getLastSimulationYear();

    Timestamp startTime();

    Timestamp endTime();

    boolean isValid(long delay);

    boolean isValid(Timestamp ts);

    boolean endTimeReached();

    boolean hasYearChange();

    void performYearChange();

    //=========================
    //util
    //=========================

    Timestamp plusMillis(long millis);

    Timestamp plusMillis(Timestamp ts, long millis);

    Timestamp plusDays(Timestamp ts, long days);

    Timestamp plusYears(Timestamp ts, long years);

    Timestamp at(int year, Month month, int day);
}
