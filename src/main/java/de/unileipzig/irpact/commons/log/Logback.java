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
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    private static final String PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level,%thread] %msg%n";
    private static final String SYSTEMOUT = "SYSTEMOUT";
    private static final String SYSTEMERR = "SYSTEMERR";
    private static final String FILE = "FILE";

    private static ConsoleAppender<ILoggingEvent> systemOutAppender;
    private static ConsoleAppender<ILoggingEvent> systemErrAppender;
    private static FileAppender<ILoggingEvent> fileAppender;

    private Logback() {
    }

    public static LoggerContext getContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public static Logger getLogger(String name) {
        return getContext().getLogger(name);
    }

    public static Logger getRootLogger() {
        return getLogger(Logger.ROOT_LOGGER_NAME);
    }

    private static void listAppenders(Logger logger, Collection<Appender<ILoggingEvent>> target) {
        Iterator<Appender<ILoggingEvent>> iter = logger.iteratorForAppenders();
        while(iter.hasNext()) {
            target.add(iter.next());
        }
    }

    private static void detachAllAppenders(Logger logger) {
        List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
        listAppenders(logger, appenders);
        for(Appender<ILoggingEvent> appender: appenders) {
            logger.detachAppender(appender);
        }
    }

    private static boolean setupSystemOutAndErrCalled = false;
    public static void setupSystemOutAndErr() {
        if(setupSystemOutAndErrCalled) {
            return;
        } else {
            setupSystemOutAndErrCalled = true;
            setupFileCalled = false;
            FileAppender<ILoggingEvent> fileAppender = getFileAppender();
            fileAppender.stop();
        }
        Logger root = getRootLogger();
        root.setLevel(Level.ALL);
        root.setAdditive(true);
        detachAllAppenders(root);
        ConsoleAppender<ILoggingEvent> systemOutAppender = getSystemOutAppender();
        ConsoleAppender<ILoggingEvent> systemErrAppender = getSystemErrAppender();
        systemOutAppender.start();
        systemErrAppender.start();
        root.addAppender(systemOutAppender);
        root.addAppender(systemErrAppender);
    }

    private static boolean setupFileCalled = false;
    public static void setupFile(Path target) {
        if(setupFileCalled) {
            return;
        } else {
            setupFileCalled = true;
            setupSystemOutAndErrCalled = false;
            ConsoleAppender<ILoggingEvent> systemOutAppender = getSystemOutAppender();
            ConsoleAppender<ILoggingEvent> systemErrAppender = getSystemErrAppender();
            systemOutAppender.stop();
            systemErrAppender.stop();
        }
        Logger root = getRootLogger();
        root.setLevel(Level.ALL);
        root.setAdditive(true);
        detachAllAppenders(root);
        FileAppender<ILoggingEvent> fileAppender = getFileAppender();
        fileAppender.setFile(target.toFile().toString());
        fileAppender.start();
        root.addAppender(fileAppender);
    }

    public static void setLevel(Level level) {
        getRootLogger().setLevel(level);
    }

    public static ConsoleAppender<ILoggingEvent> getSystemOutAppender() {
        if(systemOutAppender == null) {
            PatternLayout layout = new PatternLayout();
            layout.setContext(getContext());
            layout.setPattern(PATTERN);
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
            appender.setName(SYSTEMOUT);
            appender.setEncoder(encoder);
            systemOutAppender = appender;
        }
        return systemOutAppender;
    }

    public static ConsoleAppender<ILoggingEvent> getSystemErrAppender() {
        if(systemErrAppender == null) {
            PatternLayout layout = new PatternLayout();
            layout.setContext(getContext());
            layout.setPattern(PATTERN);
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
            appender.setName(SYSTEMERR);
            appender.setEncoder(encoder);
            systemErrAppender = appender;
        }
        return systemErrAppender;
    }

    public static FileAppender<ILoggingEvent> getFileAppender() {
        if(fileAppender == null) {
            PatternLayout layout = new PatternLayout();
            layout.setContext(getContext());
            layout.setPattern(PATTERN);
            layout.start();

            LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
            encoder.setContext(getContext());
            encoder.setLayout(layout);
            encoder.start();

            FileAppender<ILoggingEvent> appender = new FileAppender<>();
            appender.setContext(getContext());
            appender.setName(FILE);
            appender.setEncoder(encoder);
            fileAppender = appender;
        }
        return fileAppender;
    }
}
