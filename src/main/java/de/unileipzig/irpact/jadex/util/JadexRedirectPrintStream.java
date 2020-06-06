package de.unileipzig.irpact.jadex.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

/**
 * Leitet 'terminated event for' auf logger.trace um.
 *
 * @author Daniel Abitz
 */
public class JadexRedirectPrintStream extends PrintStream {

    private static final Logger logger = LoggerFactory.getLogger(JadexRedirectPrintStream.class);

    private final Logger customLogger;
    private final boolean err;

    public JadexRedirectPrintStream(PrintStream ps, boolean err) {
        this(ps, err, logger);
    }

    public JadexRedirectPrintStream(PrintStream ps, boolean err, Logger logger) {
        super(ps);
        this.err = err;
        this.customLogger = logger;
    }

    @Override
    public void println(String str) {
        if(str != null && str.startsWith("terminated event for")) {
            if(err) {
                customLogger.error(str);
            } else {
                customLogger.trace(str);
            }
        } else {
            super.println(str);
        }
    }
}
