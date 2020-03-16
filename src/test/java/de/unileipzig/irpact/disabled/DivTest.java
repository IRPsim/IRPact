package de.unileipzig.irpact.disabled;

import de.unileipzig.irpact.commons.distribution.ConstantDistribution;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
class DivTest {

    @Test
    void testStuff() {
        fail("disabled?");
    }

    @Test
    void name() {
        System.out.println(ConstantDistribution.NAME);
    }
}