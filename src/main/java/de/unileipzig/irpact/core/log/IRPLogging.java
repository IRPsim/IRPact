package de.unileipzig.irpact.core.log;

import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.util.log.IRPLogger;
import de.unileipzig.irptools.util.log.LoggingFilter;
import de.unileipzig.irptools.util.log.LoggingSection;

import java.nio.file.Path;

/**
 * Global logging of IRPact.
 *
 * @author Daniel Abitz
 */
public final class IRPLogging {

    private static final GlobalFilter FILTER = new GlobalFilter();

    private static IRPLogger clearLogger;

    private IRPLogging() {
    }

    public static void initConsole() {
        Logback.setupConsole();
    }

    public static void initFile(Path target) {
        Logback.setupFile(target);
    }

    public static void initConsoleAndFile(Path target) {
        Logback.setupConsoleAndFile(target);
    }

    public static IRPLogger getLogger(Class<?> c) {
        return IRPLogger.getLogger(FILTER, c);
    }

    public static IRPLogger getClearLogger() {
        if(clearLogger == null) {
            initClearLogger();
        }
        return clearLogger;
    }
    private static synchronized void initClearLogger() {
        if(clearLogger == null) {
            clearLogger = new IRPLogger(FILTER, Logback.getClearLogger());
        }
    }

    public static void setFilter(SectionLoggingFilter filter) {
        FILTER.setBacked(filter);
    }

    public static void removeFilter() {
        FILTER.removeBacked();
    }

    public static boolean hasFilter() {
        return FILTER.getBacked() != null;
    }

    public static SectionLoggingFilter getFilter() {
        return FILTER.getBacked();
    }

    public static void setLevel(IRPLevel level) {
        Logback.setLevel(level.toLogbackLevel());
    }

    /**
     * @author Daniel Abitz
     */
    private static final class GlobalFilter implements LoggingFilter {

        private SectionLoggingFilter backed;

        private GlobalFilter() {
        }

        private void setBacked(SectionLoggingFilter backed) {
            if(this.backed != null) {
                throw new IllegalArgumentException("filter aready set");
            }
            this.backed = backed;
        }

        private void removeBacked() {
            backed = null;
        }

        private SectionLoggingFilter getBacked() {
            return backed;
        }

        @Override
        public boolean doLogging() {
            return backed == null || backed.doLogging();
        }

        @Override
        public boolean doLogging(LoggingSection section) {
            return backed == null || backed.doLogging(section);
        }
    }
}
