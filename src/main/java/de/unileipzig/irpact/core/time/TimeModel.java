package de.unileipzig.irpact.core.time;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;

/**
 * @author Daniel Abitz
 */
public interface TimeModel extends Nameable, InitalizablePart {

    /**
     * Special setup for time model.
     */
    void setupTimeModel();

    boolean hasStarted();

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

    void performYearChange(LongConsumer lastYearTasks, LongConsumer newYearTasks);

    //=========================
    //util
    //=========================

    Timestamp plusMillis(long millis);

    Timestamp plusMillis(Timestamp ts, long millis);

    Timestamp plusDays(Timestamp ts, long days);

    Timestamp plusYears(Timestamp ts, long years);

    Timestamp atStartOfYear(int year);

    Timestamp atEndOfYear(int year);

    Timestamp atWeek(int year, long weeks);

    Timestamp atDay(int year, Month month, int day);
}
