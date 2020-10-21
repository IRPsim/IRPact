package de.unileipzig.irpact.v2.commons.time;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public interface Timestamp extends Comparable<Timestamp> {

    ZonedDateTime getTime();
}
