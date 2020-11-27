package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import jadex.bridge.service.annotation.Reference;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public final class BasicTimestamp implements JadexTimestamp {

    private final ZonedDateTime TIME;

    public BasicTimestamp(ZonedDateTime time) {
        TIME = time;
    }

    @Override
    public ZonedDateTime getTime() {
        return TIME;
    }

    @Override
    public int compareTo(Timestamp other) {
        return getTime().compareTo(other.getTime());
    }

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(TIME);
    }
}
