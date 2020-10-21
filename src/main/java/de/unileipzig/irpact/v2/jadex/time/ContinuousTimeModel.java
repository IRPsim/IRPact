package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.commons.time.*;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public class ContinuousTimeModel extends JadexTimeModel {

    protected ContinuousConverter converter;

    public void setConverter(ContinuousConverter converter) {
        this.converter = converter;
    }

    public ContinuousConverter getConverter() {
        return converter;
    }

    @Override
    public Timestamp now() {
        long timeMs = getClock().getTime();
        ZonedDateTime time = getConverter().toTime(timeMs);
        return new ContinuousTimestamp(timeMs, time);
    }
}
