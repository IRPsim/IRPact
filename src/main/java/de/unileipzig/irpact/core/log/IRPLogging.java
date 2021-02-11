package de.unileipzig.irpact.core.log;

import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.util.log.IRPLogger;
import de.unileipzig.irptools.util.log.IRPLoggingType;
import de.unileipzig.irptools.util.log.LoggingFilter;

import java.nio.file.Path;

/**
 * Global logging of IRPact.
 *
 * @author Daniel Abitz
 */
public final class IRPLogging {

    private static final GlobalFilter FILTER = new GlobalFilter();

    private IRPLogging() {
    }

    public static void initConsole() {
        Logback.setupSystemOutAndErr();
    }

    public static void initFile(Path target) {
        Logback.setupFile(target);
    }

    public static IRPLogger getLogger(Class<?> c) {
        return IRPLogger.getLogger(FILTER, c);
    }

    public static void setFilter(LoggingFilter filter) {
        FILTER.setBacked(filter);
    }

    /**
     * @author Daniel Abitz
     */
    private static class GlobalFilter implements LoggingFilter {

        private LoggingFilter backed;

        private GlobalFilter() {
        }

        private void setBacked(LoggingFilter backed) {
            this.backed = backed;
        }

        @Override
        public boolean doLogging(IRPLoggingType type, int level) {
            return backed == null || backed.doLogging(type, level);
        }
    }
}
