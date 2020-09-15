package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;
import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@ToDevelop
class ContinuousConverterTest {

    @Test
    void testStarted() {
        int year = 2015;
        ContinuousConverter converter = new ContinuousConverter(year);

        assertThrows(IllegalStateException.class, () -> converter.toTime(0));
        converter.setStart(0);
        assertDoesNotThrow(() -> converter.toTime(0));
    }

    @Test
    void testToTime() {
        int year = 2015;
        ContinuousConverter converter = new ContinuousConverter(year);
        converter.setStart(0);

        assertEquals(
                LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(TimeUnit.DAYS.toMillis(0))
        );
        assertEquals(
                LocalDate.of(2015, 1, 2).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(TimeUnit.DAYS.toMillis(1))
        );
        assertEquals(
                LocalDate.of(2015, 2, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(TimeUnit.DAYS.toMillis(31))
        );
    }

    @Test
    void testToTimeWithStart() {
        int year = 2015;
        ContinuousConverter converter = new ContinuousConverter(year);
        converter.setStart(12345678L);

        assertEquals(
                LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(12345678L + TimeUnit.DAYS.toMillis(0))
        );
        assertEquals(
                LocalDate.of(2015, 1, 2).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(12345678L + TimeUnit.DAYS.toMillis(1))
        );
        assertEquals(
                LocalDate.of(2015, 2, 1).atStartOfDay(ZoneId.systemDefault()),
                converter.toTime(12345678L + TimeUnit.DAYS.toMillis(31))
        );
    }

    @Test
    void testFromTime() {
        int year = 2015;
        ContinuousConverter converter = new ContinuousConverter(year);
        converter.setStart(0);

        assertEquals(
                TimeUnit.DAYS.toMillis(0),
                converter.fromTime(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()))
        );
        assertEquals(
                TimeUnit.DAYS.toMillis(1),
                converter.fromTime(LocalDate.of(2015, 1, 2).atStartOfDay(ZoneId.systemDefault()))
        );
        assertEquals(
                TimeUnit.DAYS.toMillis(31),
                converter.fromTime(LocalDate.of(2015, 2, 1).atStartOfDay(ZoneId.systemDefault()))
        );
    }
}