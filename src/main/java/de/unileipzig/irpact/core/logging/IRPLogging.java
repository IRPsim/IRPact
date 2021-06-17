package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import de.unileipzig.irptools.util.log.LoggingFilter;
import de.unileipzig.irptools.util.log.LoggingSection;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

/**
 * Global logging of IRPact.
 *
 * @author Daniel Abitz
 */
public final class IRPLogging {

    private static final String RESULT_PATTERN = "{}{}{}{}{}{}{}";
    public static final String RESULT_START = "===RESULT-START===";
    public static final String RESULT_END = "===RESULT-END===";

    private static final LoggingController CONTROLLER = newManager();

    private static LoggingController newManager() {
        BasicLoggingController manager = new BasicLoggingController();
        manager.init();
        manager.setRootLevel(IRPLevel.getDefault());
        manager.setResultLevel(IRPLevel.ALL);
        manager.setFilterError(true);
        manager.setPath(null);
        manager.writeToConsole();
        return manager;
    }

    private static final InternalFilter FILTER = new InternalFilter();

    public static String lineSeparator = "\n"; //aenderbar

    private static IRPLogger resultLogger;

    private IRPLogging() {
    }

    public static void initalize() {
        if(!hasFilter()) {
            setFilter(new SectionLoggingFilter());
        }
        IRPtools.setLoggingFilter(getFilter());
        writeToConsole();
        IRPSection.addSectionsToTools();
        IRPSection.addAllTo(getFilter());
    }

    public static void disableTools() {
        IRPSection.removeAllToolsFrom(getFilter());
    }

    public static void enableTools() {
        IRPSection.addAllToolsTo(getFilter());
    }

    public static LoggingController getController() {
        return CONTROLLER;
    }

    public static void setFilterError(boolean filterError) {
        CONTROLLER.setFilterError(filterError);
    }

    public static void writeToConsole() {
        CONTROLLER.writeToConsole();
    }

    public static void writeToFile(Path target) {
        CONTROLLER.setPath(target);
        CONTROLLER.writeToFile();
    }

    public static void writeToConsoleAndFile(Path target) {
        CONTROLLER.setPath(target);
        CONTROLLER.writeToConsoleAndFile();
    }

    public static void setLevel(IRPLevel level) {
        CONTROLLER.setRootLevel(level);
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
            resultLogger = new IRPLogger(FILTER, CONTROLLER.getResultLogger());
        }
    }

    public static void enableLogging() {
        FILTER.enable();
    }

    public static void disableLogging() {
        FILTER.disable();
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
        SectionLoggingFilter filter = FILTER.getBacked();
        if(filter == null) {
            throw new NoSuchElementException();
        }
        return filter;
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
    //some logging stuff
    //==================================================

    //=========================
    //result
    //=========================

    public static void logResult(String resultTag, Object result) {
        logResult(getResultLogger(), resultTag, result);
    }

    public static void logResult(Logger logger, String resultTag, Object result) {
        logger.info(
                RESULT_PATTERN,
                resultTag, lineSeparator,
                RESULT_START, lineSeparator,
                result, lineSeparator,
                RESULT_END
        );
    }

    private static String buildTag(String tag, boolean start) {
        if(tag == null || StringUtil.isBlank(tag)) {
            tag = "RESULT";
        }
        return start
                ? "===START-" + tag + "==="
                : "===END-" + tag + "===";
    }

    public static void startResult(String tag, boolean autoLinebreak) {
        getResultLogger().info(buildTag(tag, true));
        if(autoLinebreak) {
            getController().enableClearMode();
        } else {
            getController().enableWriteMode();
        }
    }

    public static void resultWrite(String str) {
        getResultLogger().info(str);
    }

    public static void resultWrite(String format, Object arg) {
        getResultLogger().info(format, arg);
    }

    public static void resultWrite(String format, Object arg1, Object arg2) {
        getResultLogger().info(format, arg1, arg2);
    }

    public static void resultWrite(String format, Object... args) {
        getResultLogger().info(format, args);
    }

    public static void resultWriteln(String str) {
        resultWrite(str);
        resultNewLine();
    }

    public static void resultNewLine() {
        getResultLogger().info(StringUtil.lineSeparator());
    }

    public static void resultSeparator() {
        getResultLogger().info("=========================");
    }

    public static void resultWrite(Reader reader) throws UncheckedIOException {
        resultWrite(reader, 8192);
    }

    public static void resultWrite(Reader reader, int bufferSize) throws UncheckedIOException {
        try {
            resultWrite0(reader, bufferSize);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void resultWrite0(Reader reader, int bufferSize) throws IOException {
        char[] cbuf = new char[bufferSize];
        int l;
        do {
            l = reader.read(cbuf);
            if(l > 0) {
                String temp = new String(cbuf, 0, l);
                getResultLogger().info(temp);
            }
        } while(l != -1);
    }

    public static void finishResult(String tag) {
        getController().enableInformationMode();
        getResultLogger().info(buildTag(tag, false));
    }

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
