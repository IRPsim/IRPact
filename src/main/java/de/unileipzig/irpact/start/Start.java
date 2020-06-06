package de.unileipzig.irpact.start;

import de.unileipzig.irpact.jadex.util.JadexRedirectPrintStream;
import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Daniel Abitz
 */
public class Start {

    private static void redirectSystem() {
        PrintStream out = System.out;
        PrintStream err = System.err;
        System.setOut(new JadexRedirectPrintStream(out, false));
        System.setErr(new JadexRedirectPrintStream(err, true));
    }

    public static void main(String[] args) throws IOException {
        redirectSystem();
        HardCodedAgentDemo.main(args);
    }
}
