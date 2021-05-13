package de.unileipzig.irpact.start.utilities;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class UtilitiesCommandLineOptionsTest {

    @Test
    void testPrintHelp() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("-?");
        cl.parse();
    }

    @Test
    void testPrintVersion() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("-v");
        cl.parse();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testExecuted() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions();
        assertFalse(cl.wasExecuted());
        assertThrows(IllegalStateException.class, cl::isTestCl);
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.wasExecuted());
        assertDoesNotThrow(cl::isTestCl);
    }

    @Test
    void testIsTestClTrue() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--testCl");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertTrue(cl.isTestCl());
    }

    @Test
    void testIsTestClFalse() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions();
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertFalse(cl.isTestCl());
    }

    @Test
    void testMilieusNull() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions();
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertNull(cl.getMilieus());
    }

    @Test
    @Disabled
    void testMilieusEmpty() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--milieus");
        assertEquals(CommandLine.ExitCode.USAGE, cl.parse());
    }

    @Test
    void testMilieus1() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--milieus", "a");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(new String[]{"a"}, cl.getMilieus());
    }

    @Test
    void testMilieus2() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--milieus", "a", "b");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(new String[]{"a", "b"}, cl.getMilieus());
    }

    @Test
    void testMilieus2WithSep() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--milieus", "a,b");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(new String[]{"a", "b"}, cl.getMilieus());
    }

    @Test
    void testMilieus5() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--milieus", "a,b,c,d,e");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, cl.getMilieus());
    }

    @Test
    void testRInput() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--rinput", "a/b/c.txt");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b", "c.txt"), cl.getRInput());
    }

    @Test
    void testParam2Spec() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--param2spec", "a/b/c.txt", "x/y/z.txt");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(
                new Path[] {
                        Paths.get("a/b/", "c.txt"),
                        Paths.get("x/y/", "z.txt")
                },
                cl.getParam2specPaths()
        );
    }

    @Disabled
    @Test
    void testParam2SpecError1() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--param2spec", "a/b/c.txt");
        assertEquals(CommandLine.ExitCode.USAGE, cl.parse());
    }

    @Test
    void testParam2SpecWith3Args() {
        UtilitiesCommandLineOptions cl = new UtilitiesCommandLineOptions("--param2spec", "a", "b", "c");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertArrayEquals(
                new Path[] {
                        Paths.get("a"),
                        Paths.get("b")
                },
                cl.getParam2specPaths()
        );
    }
}