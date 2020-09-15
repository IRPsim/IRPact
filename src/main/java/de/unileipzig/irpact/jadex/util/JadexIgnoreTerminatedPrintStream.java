package de.unileipzig.irpact.jadex.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Leitet 'terminated event for' auf logger.trace um.
 *
 * @author Daniel Abitz
 */
public class JadexIgnoreTerminatedPrintStream extends PrintStream {

    public JadexIgnoreTerminatedPrintStream(OutputStream out) {
        super(out);
    }

    @Override
    public void println(String str) {
        if(str == null || !str.startsWith("terminated event for")) {
            super.println(str);
        }
    }
}
