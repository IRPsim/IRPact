package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@ToDevelop
class TickConverterTest {

    @Test
    void testStarted() {
        int year = 2015;
        long timePerTickInMs = TimeUnit.DAYS.toMillis(1);
        TickConverter converter = new TickConverter(year, timePerTickInMs);

        assertThrows(IllegalStateException.class, () -> converter.tickToTime(0.0));
        converter.setStart(0);
        assertDoesNotThrow(() -> converter.tickToTime(0.0));
    }

    @Test
    void testTickToTime() {
        int year = 2015;
        long timePerTickInMs = TimeUnit.DAYS.toMillis(1);
        TickConverter converter = new TickConverter(year, timePerTickInMs);
        converter.setStart(0);

        assertEquals(
                LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.tickToTime(0.0)
        );
        assertEquals(
                LocalDate.of(2015, 1, 2).atStartOfDay(ZoneId.systemDefault()),
                converter.tickToTime(1.0)
        );
        assertEquals(
                LocalDate.of(2015, 2, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.tickToTime(31.0)
        );
    }

    @Test
    void testTimeToTicks() {
        int year = 2015;
        long timePerTickInMs = TimeUnit.DAYS.toMillis(2);
        TickConverter converter = new TickConverter(year, timePerTickInMs);
        converter.setStart(0);

        assertEquals(
                0.0,
                converter.timeToTick(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()))
        );
        assertEquals(
                0.5,
                converter.timeToTick(LocalDate.of(2015, 1, 2).atStartOfDay(ZoneId.systemDefault()))
        );
        assertEquals(
                31.0,
                converter.timeToTick(LocalDate.of(2015, 3, 4).atStartOfDay(ZoneId.systemDefault()))
        );
    }

    @Test
    void testas() {
        int year = 2017;
        long timePerTickInMs = TimeUnit.DAYS.toMillis(2);
        TickConverter converter = new TickConverter(year, timePerTickInMs);
        converter.setStart(0);

        assertEquals(
                0.0,
                converter.timestampToTick(LocalDate.of(2017, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        );
        assertEquals(
                0.5,
                converter.timestampToTick(LocalDate.of(2017, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        );
        assertEquals(
                31.0,
                converter.timestampToTick(LocalDate.of(2017, 3, 4).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        );
    }
}