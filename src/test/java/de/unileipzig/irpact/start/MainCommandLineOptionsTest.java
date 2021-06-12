package de.unileipzig.irpact.start;

import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class MainCommandLineOptionsTest {

    @Test
    @Disabled
    void testPrintHelp() {
        MainCommandLineOptions cl = new MainCommandLineOptions("-v?");
        cl.parse();
        cl.printVersionAndHelp();
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

    @Test
    void testOutputPath() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "-o", "a/b/c.txt");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b/c.txt"), cl.getOutputPath());
    }

    @Test
    void testOutputDirBasedOnOutputPath() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "-o", "a/b/c.txt");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b"), cl.getOutputDir());
    }

    @Test
    void testOutputDir() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "--outputDir", "a/b");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b"), cl.getOutputDir());
    }

    @Test
    void testDownloadDirBasedOnOutputPath() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "-o", "a/b/c.txt");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b", IRPact.DOWNLOAD_DIR_NAME), cl.getDownloadDir());
    }

    @Test
    void testDownloadDirBasedOnOutputDir() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "--outputDir", "a/b");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b", IRPact.DOWNLOAD_DIR_NAME), cl.getDownloadDir());
    }

    @Test
    void testDownload() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "--downloadDir", "a/b/" + IRPact.DOWNLOAD_DIR_NAME);
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(Paths.get("a/b", IRPact.DOWNLOAD_DIR_NAME), cl.getDownloadDir());
    }

    @Test
    void testDefaultGnuPlotCommand() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(GnuPlotEngine.DEFAULT_COMMAND, cl.getGnuplotCommand());
    }

    @Test
    void testCustomGnuPlotCommand() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "--gnuplotCommand", "a/b/c");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals("a/b/c", cl.getGnuplotCommand());
        assertEquals(RscriptEngine.DEFAULT_COMMAND, cl.getRscriptCommand());
    }

    @Test
    void testDefaultRscriptCommand() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals(RscriptEngine.DEFAULT_COMMAND, cl.getRscriptCommand());
    }

    @Test
    void testCustomRscriptCommand() {
        MainCommandLineOptions cl = new MainCommandLineOptions("--testCl", "--rscriptCommand", "a/b/c");
        assertEquals(CommandLine.ExitCode.OK, cl.parse());
        assertEquals("a/b/c", cl.getRscriptCommand());
        assertEquals(GnuPlotEngine.DEFAULT_COMMAND, cl.getGnuplotCommand());
    }
}