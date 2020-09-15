package de.unileipzig.irpact.jadex.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

/**
 * Leitet 'terminated event for' auf logger.trace um.
 *
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class JadexRedirectPrintStream extends PrintStream {

    private static final Logger logger = LoggerFactory.getLogger(JadexRedirectPrintStream.class);

    private Logger customLogger;

    public JadexRedirectPrintStream(PrintStream ps) {
        this(ps, null);
    }

    public JadexRedirectPrintStream(PrintStream ps, Logger logger) {
        super(ps);
        this.customLogger = logger == null
                ? JadexRedirectPrintStream.logger
                : logger;
    }

    @Override
    public void println(String str) {
        if(str != null && str.startsWith("terminated event for")) {
            customLogger.trace(str);
        } else {
            super.println(str);
        }
    }
}
