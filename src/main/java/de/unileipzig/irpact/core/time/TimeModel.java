package de.unileipzig.irpact.core.time;

import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.Initialization;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface TimeModel extends Initialization {

    TimeMode getMode();

    Timestamp convert(ZonedDateTime zdt);

    int getYear();

    Timestamp now();

    Timestamp startTime();

    Timestamp endTime();

    Timestamp plusMillis(long millis);

    Timestamp plusMillis(Timestamp ts, long millis);

    Timestamp plusDays(Timestamp ts, long days);

    Timestamp plusYears(Timestamp ts, long years);

    boolean isValid(long delay);

    boolean isValid(Timestamp ts);
}
