package de.unileipzig.irpact.commons.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Daniel Abitz
 */
public class ConsoleUtil {

    private static final PrintStream DUMMY = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) {
        }
    });

    private static PrintStream out;

    public static synchronized void disable() {
        if(out == null) {
            out = System.out;
            System.setOut(DUMMY);
        }
    }

    public static synchronized void enable() {
        if(out != null) {
            System.setOut(out);
            out = null;
        }
    }
}
