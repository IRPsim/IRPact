package de.unileipzig.irpact.experimental;

import java.io.PrintStream;

/**
 * Leitet 'terminated event for' auf logger.trace um.
 *
 * @author Daniel Abitz
 */
public class ExperimentalIgnorePrintStream extends PrintStream {

    public ExperimentalIgnorePrintStream(PrintStream ps) {
        super(ps);
    }

    public static void redirectSystemOut() {
        PrintStream out = System.out;
        System.setOut(new ExperimentalIgnorePrintStream(out));
    }

    @Override
    public void println(String str) {
        if(str == null || !str.startsWith("terminated event for")) {
            ((PrintStream) out).println(str);
        }
    }
}
