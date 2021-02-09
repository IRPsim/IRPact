package de.unileipzig.irpact.core.log;

import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.util.log.IRPLogger;
import de.unileipzig.irptools.util.log.IRPLoggingType;
import de.unileipzig.irptools.util.log.LoggingFilter;

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

    public static IRPLogger getLogger(Class<?> c) {
        return IRPLogger.getLogger(FILTER, c);
    }

    public static void setFilter(LoggingFilter filter) {
        FILTER.setBacked(filter);
    }

    public static void setLogIfNoFilter(boolean logIfNoFilter) {
        FILTER.setLogIfNoFilter(logIfNoFilter);
    }

    /**
     * @author Daniel Abitz
     */
    private static class GlobalFilter implements LoggingFilter {

        private LoggingFilter backed;
        private boolean logIfNoFilter = true;

        private GlobalFilter() {
        }

        private void setLogIfNoFilter(boolean logIfNoFilter) {
            this.logIfNoFilter = logIfNoFilter;
        }

        private void setBacked(LoggingFilter backed) {
            this.backed = backed;
        }

        @Override
        public boolean doLogging(IRPLoggingType type, int level) {
            return backed == null
                    ? logIfNoFilter
                    : backed.doLogging(type, level);
        }
    }
}
