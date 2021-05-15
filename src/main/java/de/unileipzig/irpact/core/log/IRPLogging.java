package de.unileipzig.irpact.core.log;

import ch.qos.logback.classic.Level;
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

    private static final InternalFilter FILTER = new InternalFilter();

    private static IRPLogger resultLogger;

    private IRPLogging() {
    }

    public static void enableLogging() {
        FILTER.enable();
    }

    public static void disableLogging() {
        FILTER.disable();
    }

    public static void initConsole() {
        Logback.initLogging();
        Logback.setupConsole();
        Logback.setLevel(Level.ALL);
    }

    public static void initFile(Path target) {
        Logback.initLogging();
        Logback.setupFile(target);
        Logback.setLevel(Level.ALL);
    }

    public static void initConsoleAndFile(Path target) {
        Logback.initLogging();
        Logback.setupConsoleAndFile(target);
        Logback.setLevel(Level.ALL);
    }

    public static IRPLogger getLogger(Class<?> c) {
        return IRPLogger.getLogger(FILTER, c);
    }

    public static IRPLogger getResultLogger() {
        if(resultLogger == null) {
            initResultLogger();
        }
        return resultLogger;
    }
    private static synchronized void initResultLogger() {
        if(resultLogger == null) {
            resultLogger = new IRPLogger(FILTER, Logback.getResultLogger());
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
        Logback.initLogging();
        Logback.setLevel(level.toLogbackLevel());
    }

    //=========================
    //helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    private static final class InternalFilter implements LoggingFilter {

        private SectionLoggingFilter backed;
        private boolean enabled = true;

        private InternalFilter() {
        }

        private void enable() {
            enabled = true;
        }

        private void disable() {
            enabled = false;
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
            return enabled && (backed == null || backed.doLogging());
        }

        @Override
        public boolean doLogging(LoggingSection section) {
            return enabled && (backed == null || backed.doLogging(section));
        }
    }

    //==================================================
    //some util stuff
    //==================================================

    //=========================
    //info
    //=========================

    public static void trace(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String msg) {
        if(useSection) {
            logger.trace(section, msg);
        } else {
            logger.trace(msg);
        }
    }

    public static void trace(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg) {
        if(useSection) {
            logger.trace(section, format, arg);
        } else {
            logger.trace(format, arg);
        }
    }

    public static void trace(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg1, Object arg2) {
        if(useSection) {
            logger.trace(section, format, arg1, arg2);
        } else {
            logger.trace(format, arg1, arg2);
        }
    }

    public static void trace(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object... args) {
        if(useSection) {
            logger.trace(section, format, args);
        } else {
            logger.trace(format, args);
        }
    }

    //=========================
    //debug
    //=========================

    public static void debug(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String msg) {
        if(useSection) {
            logger.debug(section, msg);
        } else {
            logger.debug(msg);
        }
    }

    public static void debug(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg) {
        if(useSection) {
            logger.debug(section, format, arg);
        } else {
            logger.debug(format, arg);
        }
    }

    public static void debug(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg1, Object arg2) {
        if(useSection) {
            logger.debug(section, format, arg1, arg2);
        } else {
            logger.debug(format, arg1, arg2);
        }
    }

    public static void debug(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object... args) {
        if(useSection) {
            logger.debug(section, format, args);
        } else {
            logger.debug(format, args);
        }
    }

    //=========================
    //info
    //=========================

    public static void info(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String msg) {
        if(useSection) {
            logger.info(section, msg);
        } else {
            logger.info(msg);
        }
    }

    public static void info(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg) {
        if(useSection) {
            logger.info(section, format, arg);
        } else {
            logger.info(format, arg);
        }
    }

    public static void info(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg1, Object arg2) {
        if(useSection) {
            logger.info(section, format, arg1, arg2);
        } else {
            logger.info(format, arg1, arg2);
        }
    }

    public static void info(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object... args) {
        if(useSection) {
            logger.info(section, format, args);
        } else {
            logger.info(format, args);
        }
    }

    //=========================
    //warn
    //=========================

    public static void warn(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String msg) {
        if(useSection) {
            logger.warn(section, msg);
        } else {
            logger.warn(msg);
        }
    }

    public static void warn(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg) {
        if(useSection) {
            logger.warn(section, format, arg);
        } else {
            logger.warn(format, arg);
        }
    }

    public static void warn(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg1, Object arg2) {
        if(useSection) {
            logger.warn(section, format, arg1, arg2);
        } else {
            logger.warn(format, arg1, arg2);
        }
    }

    public static void warn(
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object... args) {
        if(useSection) {
            logger.warn(section, format, args);
        } else {
            logger.warn(format, args);
        }
    }

    //=========================
    //error
    //=========================

    public static void error(
            IRPLogger logger,
            String msg) {
        logger.error(msg);
    }

    public static void error(
            IRPLogger logger,
            Throwable cause) {
        logger.error("", cause);
    }

    public static void error(
            IRPLogger logger,
            String msg, Throwable cause) {
        logger.error(msg, cause);
    }

    public static void error(
            IRPLogger logger,
            String format, Object arg) {
        logger.error(format, arg);
    }

    public static void error(
            IRPLogger logger,
            String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    public static void error(
            IRPLogger logger,
            String format, Object... args) {
        logger.error(format, args);
    }

    //=========================
    //level
    //=========================

    public static void log(
            org.slf4j.event.Level level,
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String msg) {
        switch (level) {
            case ERROR:
                error(logger, msg);
                break;

            case WARN:
                warn(logger, section, useSection, msg);
                break;

            case INFO:
                info(logger, section, useSection, msg);
                break;

            case DEBUG:
                debug(logger, section, useSection, msg);
                break;

            case TRACE:
                trace(logger, section, useSection, msg);
                break;
        }
    }

    public static void log(
            org.slf4j.event.Level level,
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg) {
        switch (level) {
            case ERROR:
                error(logger, format, arg);
                break;

            case WARN:
                warn(logger, section, useSection, format, arg);
                break;

            case INFO:
                info(logger, section, useSection, format, arg);
                break;

            case DEBUG:
                debug(logger, section, useSection, format, arg);
                break;

            case TRACE:
                trace(logger, section, useSection, format, arg);
                break;
        }
    }

    public static void log(
            org.slf4j.event.Level level,
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object arg1, Object arg2) {
        switch (level) {
            case ERROR:
                error(logger, format, arg1, arg2);
                break;

            case WARN:
                warn(logger, section, useSection, format, arg1, arg2);
                break;

            case INFO:
                info(logger, section, useSection, format, arg1, arg2);
                break;

            case DEBUG:
                debug(logger, section, useSection, format, arg1, arg2);
                break;

            case TRACE:
                trace(logger, section, useSection, format, arg1, arg2);
                break;
        }
    }

    public static void log(
            org.slf4j.event.Level level,
            IRPLogger logger,
            IRPSection section, boolean useSection,
            String format, Object... args) {
        switch (level) {
            case ERROR:
                error(logger, format, args);
                break;

            case WARN:
                warn(logger, section, useSection, format, args);
                break;

            case INFO:
                info(logger, section, useSection, format, args);
                break;

            case DEBUG:
                debug(logger, section, useSection, format, args);
                break;

            case TRACE:
                trace(logger, section, useSection, format, args);
                break;
        }
    }
}
