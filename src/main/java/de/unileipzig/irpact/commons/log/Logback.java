package de.unileipzig.irpact.commons.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.FilterReply;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("SameParameterValue")
public final class Logback {

    //padding ja/nein?
    //private static final String PATTERN = "%d{HH:mm:ss.SSS} %-40([%logger{0},%level]) %msg%n";
    //ohne thread
    //private static final String PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level] %msg%n";
    //mit thread
    private static final String PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level,%thread] %msg%n";
    private static final String SYSTEMOUT_APPENDER_NAME = "LOG_SYSTEMOUT";
    private static final String SYSTEMERR_APPENDER_NAME = "LOG_SYSTEMERR";
    private static final String FILE_APPENDER_NAME = "LOG_FILE";
    private static final String RESULT_LOGGER_NAME = "RESULT";

    private static final List<LoggerSetup> SETUPS = new ArrayList<>();
    private static LoggerSetup setupWithError;
    private static LoggerSetup setupWithoutError;

    private static Logger rootLogger;
    private static Logger resultLogger;

    private static Path lastTarget;

    private static LoggerMode mode = LoggerMode.NONE;
    private static boolean doFilterError = false;

    private Logback() {
    }

    public static Logger getRootLogger() {
        checkInit();
        return rootLogger;
    }

    public static Logger getResultLogger() {
        checkInit();
        return resultLogger;
    }

    public static LoggerContext getContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public static Logger getLogger(String name) {
        checkInit();
        return getContext().getLogger(name);
    }

    public static void setupConsole() {
        checkInit();
        if(mode == LoggerMode.CONSOLE) {
            return;
        }
        mode = LoggerMode.CONSOLE;

        for(LoggerSetup setup: SETUPS) {
            setup.setupConsole();
        }
    }

    public static void setupFile(Path target) {
        checkInit();
        if(mode == LoggerMode.FILE) {
            return;
        }
        mode = LoggerMode.FILE;
        lastTarget = target;

        for(LoggerSetup setup: SETUPS) {
            setup.setupFile(target);
        }
    }

    public static void setupConsoleAndFile(Path target) {
        checkInit();
        if(mode == LoggerMode.CONSOLE_FILE) {
            return;
        }
        mode = LoggerMode.CONSOLE_FILE;
        lastTarget = target;

        for(LoggerSetup setup: SETUPS) {
            setup.setupConsoleAndFile(target);
        }
    }

    public static void filterError(boolean doFilter) {
        checkInit();
        checkMode();

        if(doFilterError == doFilter) {
            return;
        }
        doFilterError = doFilter;

        if(doFilter) {
            setupWithError.disable();
            setupWithoutError.enable();
        } else {
            setupWithError.enable();
            setupWithoutError.disable();
        }

        LoggerMode currentMode = mode;
        mode = LoggerMode.NONE;

        switch (currentMode) {
            case CONSOLE:
                setupConsole();
                break;

            case FILE:
                setupFile(lastTarget);
                break;

            case CONSOLE_FILE:
                setupConsoleAndFile(lastTarget);
                break;
        }
    }

    public static void setLevel(Level level) {
        checkInit();
        getRootLogger().setLevel(level);
        getResultLogger().setLevel(level);
    }

    private static boolean initCalled = false;
    public static void initLogging() {
        if(initCalled) {
            return;
        }

        initCalled = true;
        initRootLogger();
        initResultLogger();
        initSetups();
    }

    private static void checkInit() {
        if(!initCalled) {
            throw new IllegalStateException("not initalized");
        }
    }

    private static void checkMode() {
        if(mode == LoggerMode.NONE) {
            throw new IllegalStateException("no mode initalized");
        }
    }

    private static void initRootLogger() {
        rootLogger = getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setAdditive(true);
        rootLogger.setLevel(Level.ALL);
        detachAllAppenders(rootLogger);
    }

    private static void initResultLogger() {
        resultLogger = getLogger(RESULT_LOGGER_NAME);
        resultLogger.setAdditive(false);
        resultLogger.setLevel(Level.ALL);
        detachAllAppenders(resultLogger);
    }

    private static void initSetups() {
        setupWithoutError = new LoggerSetup(
                getRootLogger(),
                CollectionUtil.arrayListOf(getSystemOutAppenderWithoutError(), getSystemErrAppender()),
                CollectionUtil.arrayListOf(getFileAppender())
        );
        setupWithoutError.disable();
        SETUPS.add(setupWithoutError);

        setupWithError = new LoggerSetup(
                getRootLogger(),
                CollectionUtil.arrayListOf(getSystemOutAppenderWithError(), getSystemErrAppender()),
                CollectionUtil.arrayListOf(getFileAppender())
        );
        setupWithError.enable();
        SETUPS.add(setupWithError);

        LoggerSetup setupForResult = new LoggerSetup(
                getResultLogger(),
                CollectionUtil.arrayListOf(getSystemOutAppenderWithError(), getSystemErrAppender()),
                CollectionUtil.arrayListOf(getFileAppender())
        );
        setupForResult.enable();
        SETUPS.add(setupForResult);
    }

    private static void setFile(Collection<? extends FileAppender<ILoggingEvent>> appenders, Path path) {
        String file = path.toFile().toString();
        for(FileAppender<ILoggingEvent> appender: appenders) {
            appender.setFile(file);
        }
    }

    private static void stopAppenders(Collection<? extends Appender<ILoggingEvent>> appenders) {
        for(Appender<ILoggingEvent> appender: appenders) {
            if(appender.isStarted()) {
                appender.stop();
            }
        }
    }

    private static void startAppenders(Collection<? extends Appender<ILoggingEvent>> appenders) {
        for(Appender<ILoggingEvent> appender: appenders) {
            if(!appender.isStarted()) {
                appender.start();
            }
        }
    }

    private static void addAppenders(Collection<? extends Appender<ILoggingEvent>> appenders, Logger logger) {
        for(Appender<ILoggingEvent> appender: appenders) {
            logger.addAppender(appender);
        }
    }

    private static void listAppenders(Logger logger, Collection<Appender<ILoggingEvent>> target) {
        CollectionUtil.addAll(target, logger.iteratorForAppenders());
    }

    private static void detachAllAppenders(Logger logger) {
        List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
        listAppenders(logger, appenders);
        for(Appender<ILoggingEvent> appender: appenders) {
            logger.detachAppender(appender);
        }
    }

    private static ConsoleAppender<ILoggingEvent> systemOutAppenderWithoutError;
    private static ConsoleAppender<ILoggingEvent> getSystemOutAppenderWithoutError() {
        if(systemOutAppenderWithoutError == null) {
            systemOutAppenderWithoutError = createSystemOutAppender(SYSTEMOUT_APPENDER_NAME, PATTERN, true);
        }
        return systemOutAppenderWithoutError;
    }

    private static ConsoleAppender<ILoggingEvent> systemOutAppenderWithError;
    private static ConsoleAppender<ILoggingEvent> getSystemOutAppenderWithError() {
        if(systemOutAppenderWithError == null) {
            systemOutAppenderWithError = createSystemOutAppender(SYSTEMOUT_APPENDER_NAME, PATTERN, false);
        }
        return systemOutAppenderWithError;
    }

    private static ConsoleAppender<ILoggingEvent> systemErrAppender;
    private static ConsoleAppender<ILoggingEvent> getSystemErrAppender() {
        if(systemErrAppender == null) {
            systemErrAppender = createSystemErrAppender(SYSTEMERR_APPENDER_NAME, PATTERN);
        }
        return systemErrAppender;
    }

    private static FileAppender<ILoggingEvent> fileAppender;
    private static FileAppender<ILoggingEvent> getFileAppender() {
        if(fileAppender == null) {
            fileAppender = createFileAppender(FILE_APPENDER_NAME, PATTERN);
        }
        return fileAppender;
    }

    private static ConsoleAppender<ILoggingEvent> createSystemOutAppender(String name, String pattern, boolean filterError) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(getContext());
        layout.setPattern(pattern);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(getContext());
        encoder.setLayout(layout);
        encoder.start();

        LevelFilter filter = null;
        if(filterError) {
            filter = new LevelFilter();
            filter.setLevel(Level.ERROR);
            filter.setOnMatch(FilterReply.DENY);
            filter.setOnMismatch(FilterReply.ACCEPT);
            filter.start();
        }

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(getContext());
        appender.setTarget("System.out");
        if(filterError) {
            appender.addFilter(filter);
        }
        appender.setName(name);
        appender.setEncoder(encoder);

        return appender;
    }

    private static ConsoleAppender<ILoggingEvent> createSystemErrAppender(String name, String pattern) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(getContext());
        layout.setPattern(pattern);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(getContext());
        encoder.setLayout(layout);
        encoder.start();

        LevelFilter filter = new LevelFilter();
        filter.setLevel(Level.ERROR);
        filter.setOnMatch(FilterReply.ACCEPT);
        filter.setOnMismatch(FilterReply.DENY);
        filter.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(getContext());
        appender.setTarget("System.err");
        appender.addFilter(filter);
        appender.setName(name);
        appender.setEncoder(encoder);

        return appender;
    }

    private static FileAppender<ILoggingEvent> createFileAppender(String name, String pattern) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(getContext());
        layout.setPattern(pattern);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(getContext());
        encoder.setLayout(layout);
        encoder.start();

        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(getContext());
        appender.setName(name);
        appender.setEncoder(encoder);

        return appender;
    }

    /**
     * @author Daniel Abitz
     */
    private enum LoggerMode {
        NONE,
        CONSOLE,
        FILE,
        CONSOLE_FILE
    }

    /**
     * @author Daniel Abitz
     */
    private static final class LoggerSetup {

        private final Logger LOGGER;
        private final List<ConsoleAppender<ILoggingEvent>> CONSOLE_APPENDERS;
        private final List<FileAppender<ILoggingEvent>> FILE_APPENDERS;
        private final List<Appender<ILoggingEvent>> ALL_APPENDERS;

        private boolean disabled = true;

        private LoggerSetup(
                Logger logger,
                List<ConsoleAppender<ILoggingEvent>> consoleAppenders,
                List<FileAppender<ILoggingEvent>> fileAppenders) {
            LOGGER = logger;
            CONSOLE_APPENDERS = consoleAppenders;
            FILE_APPENDERS = fileAppenders;
            ALL_APPENDERS = new ArrayList<>();
            ALL_APPENDERS.addAll(CONSOLE_APPENDERS);
            ALL_APPENDERS.addAll(FILE_APPENDERS);
        }

        private void enable() {
            disabled = false;
        }

        private void disable() {
            disabled = true;
        }

        private boolean isDisabled() {
            return disabled;
        }

        private void reset() {
            if(isDisabled()) return;
            detachAllAppenders(LOGGER);
            stopAppenders(ALL_APPENDERS);
        }

        private void setupConsole() {
            if(isDisabled()) return;
            reset();
            startAppenders(CONSOLE_APPENDERS);
            addAppenders(CONSOLE_APPENDERS, LOGGER);
        }

        private void setupFile(Path target) {
            if(isDisabled()) return;
            reset();
            setFile(FILE_APPENDERS, target);
            startAppenders(FILE_APPENDERS);
            addAppenders(FILE_APPENDERS, LOGGER);
        }

        private void setupConsoleAndFile(Path target) {
            if(isDisabled()) return;
            reset();
            setFile(FILE_APPENDERS, target);
            startAppenders(ALL_APPENDERS);
            addAppenders(ALL_APPENDERS, LOGGER);
        }
    }
}
