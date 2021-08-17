package de.unileipzig.irpact.jadex.util;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Daniel Abitz
 */
public class JadexSystemOut extends PrintStream {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexSystemOut.class);

    private static final String PREFIX_1 = "terminated event for:";
    private static final String PREFIX_2 = "already called:";
    private static PrintStream defaultOut;
    private static JadexSystemOut jadexOut;

    public JadexSystemOut(OutputStream out) {
        super(out);
    }

    private static boolean doLog(String x) {
        return x.startsWith(PREFIX_1)
                || x.startsWith(PREFIX_2);
    }

    @Override
    public void println(String x) {
        if(x != null && doLog(x)) {
            LOGGER.trace(IRPSection.JADEX_SYSTEM_OUT, x);
        } else {
            super.println(x);
        }
    }

    private static void init() {
        if(defaultOut == null) {
            defaultOut = System.out;
            jadexOut = new JadexSystemOut(System.out);
        }
    }

    public static void redirect() {
        init();
        System.setOut(jadexOut);
    }

    public static void reset() {
        init();
        System.setOut(defaultOut);
    }
}
