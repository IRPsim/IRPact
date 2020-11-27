package de.unileipzig.irpact.core.time;

import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;

/**
 * @author Daniel Abitz
 */
public interface TimeModel {

    TimeMode getMode();

    Timestamp now();

    Timestamp plusMillis(long millis);

    Timestamp plusMillis(Timestamp ts, long millis);

    Timestamp plusDays(Timestamp ts, long days);

    Timestamp plusYears(Timestamp ts, long years);

    boolean isValid(long delayInMs);

    boolean isValid(Timestamp ts);
}
