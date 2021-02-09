package de.unileipzig.irpact.experimental.time;

import de.unileipzig.irpact.jadex.time.ContinuousTimeModel;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TimeTime {

    @Test
    void testStuff() {
        DiscreteTimeModel dtm = new DiscreteTimeModel();
        dtm.setStartYear(2015, 1000 * 60 * 60 * 24);
        dtm.setStartTick(123);
        dtm.setEndTime(dtm.plusYears(dtm.startTime(), 1));

        System.out.println(dtm.startTime());
        System.out.println("-> " + dtm.getConverter().timeToTick(dtm.startTime().getTime()));
        System.out.println(dtm.endTime());
        System.out.println("-> " + dtm.getConverter().timeToTick(dtm.endTime().getTime()));
        System.out.println(dtm.getConverter().ticksUntil(dtm.startTime().getTime(), dtm.endTime().getTime()));
    }

    @Test
    void testContinous() {
        ContinuousTimeModel ctm = new ContinuousTimeModel();
        ctm.setStartYear(2015);
        ctm.setStartTime(0L);
        ctm.setEndTime(ctm.plusYears(ctm.startTime(), 1));

        System.out.println(ctm.startTime());
        System.out.println(ctm.endTime());
        System.out.println(ctm.getConverter().timeBetween(ctm.startTime().getTime(), ctm.endTime().getTime()));
    }
}
