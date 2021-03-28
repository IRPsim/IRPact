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
public final class Logback {

    //padding ja/nein?
    //private static final String PATTERN = "%d{HH:mm:ss.SSS} %-40([%logger{0},%level]) %msg%n";
    //ohne thread
    //private static final String PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level] %msg%n";
    //mit thread
    private static final String LOG_PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level,%thread] %msg%n";
    private static final String LOG_SYSTEMOUT = "LOG_SYSTEMOUT";
    private static final String LOG_SYSTEMERR = "LOG_SYSTEMERR";
    private static final String LOG_FILE = "LOG_FILE";

    private static final String CLEAR = "CLEAR";
    private static final String CLEAR_PATTERN = "%msg%n";
    private static final String CLEAR_SYSTEMOUT = "CLEAR_SYSTEMOUT";
    private static final String CLEAR_SYSTEMERR = "CLEAR_SYSTEMERR";
    private static final String CLEAR_FILE = "CLEAR_FILE";

    private static final List<LoggerSetup> SETUPS = new ArrayList<>();

    private static Logger rootLogger;
    private static Logger clearLogger;

    private Logback() {
    }

    public static Logger getRootLogger() {
        checkInit();
        return rootLogger;
    }

    public static Logger getClearLogger() {
        checkInit();
        return clearLogger;
    }

    public static LoggerContext getContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public static Logger getLogger(String name) {
        checkInit();
        return getContext().getLogger(name);
    }

    private static boolean setupConsoleCalled = false;
    public static void setupConsole() {
        checkInit();
        if(setupConsoleCalled) {
            return;
        }

        setupConsoleCalled = true;
        setupFileCalled = false;
        setupConsoleAndFileCalled = false;

        for(LoggerSetup setup: SETUPS) {
            setup.setupConsole();
        }
    }

    private static boolean setupFileCalled = false;
    public static void setupFile(Path target) {
        checkInit();
        if(setupFileCalled) {
            return;
        }

        setupFileCalled = true;
        setupConsoleCalled = false;
        setupConsoleAndFileCalled = false;

        for(LoggerSetup setup: SETUPS) {
            setup.setupFile(target);
        }
    }

    private static boolean setupConsoleAndFileCalled = false;
    public static void setupConsoleAndFile(Path target) {
        checkInit();
        if(setupConsoleAndFileCalled) {
            return;
        }

        setupConsoleAndFileCalled = true;
        setupConsoleCalled = false;
        setupFileCalled = false;

        for(LoggerSetup setup: SETUPS) {
            setup.setupConsoleAndFile(target);
        }
    }

    public static void setLevel(Level level) {
        checkInit();
        getRootLogger().setLevel(level);
        getClearLogger().setLevel(level);
    }

    private static boolean initCalled = false;
    public static void initLogging() {
        if(initCalled) {
            return;
        }

        initCalled = true;
        initRootLogger();
        initClearLogger();
        initSetups();
    }

    private static void checkInit() {
        if(!initCalled) {
            throw new IllegalStateException("not initalized");
        }
    }

    private static void initRootLogger() {
        rootLogger = getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setAdditive(true);
        rootLogger.setLevel(Level.ALL);
        detachAllAppenders(rootLogger);
    }

    private static void initClearLogger() {
        clearLogger = getLogger(CLEAR);
        clearLogger.setAdditive(false);
        clearLogger.setLevel(Level.ALL);
        detachAllAppenders(clearLogger);
    }

    private static void initSetups() {
        LoggerSetup rootSetup = new LoggerSetup(
                getRootLogger(),
                CollectionUtil.arrayListOf(getSystemOutAppender(), getSystemErrAppender()),
                CollectionUtil.arrayListOf(getFileAppender())
        );
        SETUPS.add(rootSetup);

        LoggerSetup clearSetup = new LoggerSetup(
                getClearLogger(),
                CollectionUtil.arrayListOf(getClearSystemOutAppender(), getClearSystemErrAppender()),
                CollectionUtil.arrayListOf(getClearFileAppender())
        );
        SETUPS.add(clearSetup);
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

    private static ConsoleAppender<ILoggingEvent> systemOutAppender;
    private static ConsoleAppender<ILoggingEvent> getSystemOutAppender() {
        if(systemOutAppender == null) {
            systemOutAppender = createSystemOutAppender(LOG_SYSTEMOUT, LOG_PATTERN);
        }
        return systemOutAppender;
    }

    private static ConsoleAppender<ILoggingEvent> systemErrAppender;
    private static ConsoleAppender<ILoggingEvent> getSystemErrAppender() {
        if(systemErrAppender == null) {
            systemErrAppender = createSystemErrAppender(LOG_SYSTEMERR, LOG_PATTERN);
        }
        return systemErrAppender;
    }

    private static FileAppender<ILoggingEvent> fileAppender;
    private static FileAppender<ILoggingEvent> getFileAppender() {
        if(fileAppender == null) {
            fileAppender = createFileAppender(LOG_FILE, LOG_PATTERN);
        }
        return fileAppender;
    }

    private static ConsoleAppender<ILoggingEvent> clearSystemOutAppender;
    private static ConsoleAppender<ILoggingEvent> getClearSystemOutAppender() {
        if(clearSystemOutAppender == null) {
            clearSystemOutAppender = createSystemOutAppender(CLEAR_SYSTEMOUT, CLEAR_PATTERN);
        }
        return clearSystemOutAppender;
    }

    private static ConsoleAppender<ILoggingEvent> clearClearSystemErrAppender;
    private static ConsoleAppender<ILoggingEvent> getClearSystemErrAppender() {
        if(clearClearSystemErrAppender == null) {
            clearClearSystemErrAppender = createSystemErrAppender(CLEAR_SYSTEMERR, CLEAR_PATTERN);
        }
        return clearClearSystemErrAppender;
    }

    private static FileAppender<ILoggingEvent> clearFileAppender;
    private static FileAppender<ILoggingEvent> getClearFileAppender() {
        if(clearFileAppender == null) {
            clearFileAppender = createFileAppender(CLEAR_FILE, CLEAR_PATTERN);
        }
        return clearFileAppender;
    }

    private static ConsoleAppender<ILoggingEvent> createSystemOutAppender(String name, String pattern) {
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
        filter.setOnMatch(FilterReply.DENY);
        filter.setOnMismatch(FilterReply.ACCEPT);
        filter.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(getContext());
        appender.setTarget("System.out");
        appender.addFilter(filter);
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
    private static final class LoggerSetup {

        private final Logger LOGGER;
        private final List<ConsoleAppender<ILoggingEvent>> CONSOLE_APPENDERS;
        private final List<FileAppender<ILoggingEvent>> FILE_APPENDERS;
        private final List<Appender<ILoggingEvent>> ALL_APPENDERS;

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

        private void reset() {
            detachAllAppenders(LOGGER);
            stopAppenders(ALL_APPENDERS);
        }

        private void setupConsole() {
            reset();
            startAppenders(CONSOLE_APPENDERS);
            addAppenders(CONSOLE_APPENDERS, LOGGER);
        }

        private void setupFile(Path target) {
            reset();
            setFile(FILE_APPENDERS, target);
            startAppenders(FILE_APPENDERS);
            addAppenders(FILE_APPENDERS, LOGGER);
        }

        private void setupConsoleAndFile(Path target) {
            reset();
            setFile(FILE_APPENDERS, target);
            startAppenders(ALL_APPENDERS);
            addAppenders(ALL_APPENDERS, LOGGER);
        }
    }
}
