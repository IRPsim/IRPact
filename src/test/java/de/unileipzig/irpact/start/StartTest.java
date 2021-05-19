package de.unileipzig.irpact.start;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class StartTest {

    @Test
    @Disabled
    void testPrintHelpVersion() {
        Start.main(new String[]{"-?", "-v"});
    }

    @Test
    @Disabled
    void fail0() {
        fail();
    }
}