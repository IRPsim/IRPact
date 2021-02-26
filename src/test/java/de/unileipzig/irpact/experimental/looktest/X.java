package de.unileipzig.irpact.experimental.looktest;

import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class X {

    @Test
    void testIt() {
        System.out.println(InComplexAffinityEntry.thisClass().getName());
        System.out.println(InNameSplitAffinityEntry.thisClass().getName());
    }

    @Test
    void asd() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime now1 = now.plusDays(1);
        ZonedDateTime now2 = now.plusWeeks(1);
        ZonedDateTime now3 = now.plusYears(1);

        System.out.println(ChronoUnit.DAYS.between(now, now1));
        System.out.println(ChronoUnit.DAYS.between(now, now2));
        System.out.println(ChronoUnit.WEEKS.between(now, now3));
    }
}
