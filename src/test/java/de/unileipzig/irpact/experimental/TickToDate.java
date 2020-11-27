package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.v2.commons.time.TickConverter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
class TickToDate {

    @Test
    void testIt() {
        int year = 2015;
        long timePerTickInMs = TimeUnit.DAYS.toMillis(1);
        TickConverter converter = new TickConverter(year, timePerTickInMs);
        converter.setStart(0);

        System.out.println(converter.tickToTime(0));
        System.out.println(converter.tickToTime(1));
        System.out.println(converter.tickToTime(10));
        System.out.println(converter.tickToTime(100));

        System.out.println(converter.timeToTick(converter.tickToTime(0)));
        System.out.println(converter.timeToTick(converter.tickToTime(1)));
        System.out.println(converter.timeToTick(converter.tickToTime(10)));
        System.out.println(converter.timeToTick(converter.tickToTime(100)));
        System.out.println(converter.timeToTick(converter.tickToTime(111)));

        ZonedDateTime day100 = converter.tickToTime(100);
        ZonedDateTime day200 = converter.tickToTime(200);
        ZonedDateTime day100add100 = converter.addTicks(day100, 100);
        System.out.println(day200);
        System.out.println(day100add100);
    }
}
