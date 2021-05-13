package de.unileipzig.irpact.start;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class MainCommandLineOptionsTest {

    @Test
    @Disabled
    void testPrintHelp() {
        MainCommandLineOptions cl = new MainCommandLineOptions("-?");
        cl.parse();
    }

    @Test
    @Disabled
    void testPrintVersion() {
        MainCommandLineOptions cl = new MainCommandLineOptions("-v");
        cl.parse();
    }

    @Test
    void testFailWithoutInput() {
        MainCommandLineOptions cl = new MainCommandLineOptions();
        assertEquals(CommandLine.ExitCode.USAGE, cl.parse());
    }

    @Test
    void testCallUtilities() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.isCallUtilities());
    }

    @Test
    void testCallIRPtools() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--irptools");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.isCallIrptools());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testExecuted() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities");
        assertFalse(cl.wasExecuted());
        assertThrows(IllegalStateException.class, cl::isTestCl);
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.wasExecuted());
        assertDoesNotThrow(cl::isTestCl);
    }

    @Test
    void testIsTestClTrue() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities", "--testCl");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.isTestCl());
    }

    @Test
    void testIsTestClFalse() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertFalse(cl.isTestCl());
    }

    @Test
    void testMaxGamsNameLengthDefault() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(-1, cl.getMaxGamsNameLength());
    }

    @Test
    void testMaxGamsNameLength() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--utilities", "--maxGamsNameLength", "42");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(42, cl.getMaxGamsNameLength());
    }
}