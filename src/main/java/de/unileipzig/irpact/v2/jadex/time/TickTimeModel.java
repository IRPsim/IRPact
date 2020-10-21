package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.commons.time.TickConverter;
import de.unileipzig.irpact.v2.commons.time.TickTimestamp;
import de.unileipzig.irpact.v2.commons.time.Timestamp;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public class TickTimeModel extends JadexTimeModel {

    protected TickConverter converter;

    public void setConverter(TickConverter converter) {
        this.converter = converter;
    }

    public TickConverter getConverter() {
        return converter;
    }

    @Override
    public Timestamp now() {
        double tick = getClock().getTick();
        ZonedDateTime time = getConverter().tickToTime(tick);
        return new TickTimestamp(tick, time);
    }
}
