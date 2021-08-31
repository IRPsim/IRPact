package de.unileipzig.irpact.commons.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.*;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.joran.spi.ConsoleTarget;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
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

    private Logback() {
    }

    //=========================
    //Logback util
    //=========================

    @SafeVarargs
    public static void atachAllAppenders(Logger logger, Appender<ILoggingEvent>... appenders) {
        for(Appender<ILoggingEvent> appender: appenders) {
            logger.addAppender(appender);
        }
    }

    public static void detachAllAppenders(Logger logger) {
        List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
        listAppenders(logger, appenders);
        for(Appender<ILoggingEvent> appender: appenders) {
            logger.detachAppender(appender);
        }
    }

    public static void startAppenders(Logger logger) {
        List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
        listAppenders(logger, appenders);
        startAppenders(appenders);
    }

    private static void startAppenders(Collection<? extends Appender<ILoggingEvent>> appenders) {
        for(Appender<ILoggingEvent> appender: appenders) {
            if(!appender.isStarted()) {
                appender.start();
            }
        }
    }

    public static void stopAppenders(Logger logger) {
        List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
        listAppenders(logger, appenders);
        stopAppenders(appenders);
    }

    private static void stopAppenders(Collection<? extends Appender<ILoggingEvent>> appenders) {
        for(Appender<ILoggingEvent> appender: appenders) {
            if(appender.isStarted()) {
                appender.stop();
            }
        }
    }

    protected static void listAppenders(Logger logger, Collection<Appender<ILoggingEvent>> target) {
        CollectionUtil.addAll(target, logger.iteratorForAppenders());
    }

    public static void changeLayoutPattern(Appender<?> appender, String newPattern) {
        if(appender instanceof OutputStreamAppender) {
            changeLayoutPattern(((OutputStreamAppender<?>) appender).getEncoder(), newPattern);
        } else {
            throw new IllegalArgumentException("appender has no encoder");
        }
    }

    protected static void changeLayoutPattern(Encoder<?> encoder, String newPattern) {
        if(encoder instanceof LayoutWrappingEncoder) {
            Logback.changeLayoutPattern((LayoutWrappingEncoder<?>) encoder, newPattern);
        } else {
            throw new IllegalArgumentException("encoder has no layout");
        }
    }

    protected static void changeLayoutPattern(LayoutWrappingEncoder<?> encoder, String newPattern) {
        Layout<?> layout = encoder.getLayout();
        if(layout instanceof PatternLayout) {
            changeLayoutPattern((PatternLayout) layout, newPattern);
        } else {
            throw new IllegalArgumentException("encoder has no PatternLayout");
        }
    }

    protected static void changeLayoutPattern(PatternLayout layout, String newPattern) {
        layout.stop();
        layout.setPattern(newPattern);
        layout.start();
    }

    public static Logger getLogger(String name) {
        return getContext().getLogger(name);
    }

    public static Logger getLogger(Class<?> c) {
        return getContext().getLogger(c);
    }

    public static LoggerContext getContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public static void printStatus() {
        StatusManager statusManager = getContext().getStatusManager();
        List<Status> list = statusManager.getCopyOfStatusList();
        System.out.println("status count: " + list.size());
        for(Status status: list) {
            System.out.println(status);
        }
    }

    public static ConsoleAppender<ILoggingEvent> createSystemOutAppender(String name, String pattern) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(getContext());
        layout.setPattern(pattern);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(getContext());
        encoder.setLayout(layout);
        encoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(getContext());
        appender.setTarget(ConsoleTarget.SystemOut.getName());
        appender.setName(name);
        appender.setEncoder(encoder);

        return appender;
    }

    public static ConsoleAppender<ILoggingEvent> createSystemErrAppender(String name, String pattern) {
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
        appender.setTarget(ConsoleTarget.SystemErr.getName());
        appender.addFilter(filter);
        appender.setName(name);
        appender.setEncoder(encoder);

        return appender;
    }

    public static FileAppender<ILoggingEvent> createFileAppender(String name, String pattern, Path target) {
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

        if(target != null) {
            String file = target.toFile().toString();
            appender.setFile(file);
        }

        return appender;
    }
}
