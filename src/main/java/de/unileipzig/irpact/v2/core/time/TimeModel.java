package de.unileipzig.irpact.v2.core.time;

import de.unileipzig.irpact.v2.commons.time.TimeMode;
import de.unileipzig.irpact.v2.commons.time.Timestamp;

/**
 * @author Daniel Abitz
 */
public interface TimeModel {

    TimeMode getMode();

    Timestamp now();

    //addTick? addTime?
}
