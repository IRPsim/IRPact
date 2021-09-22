package de.unileipzig.irpact.commons.logging.simplified;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * @author Daniel Abitz
 */
public abstract class SingleAppenderLogger<A extends Appender<ILoggingEvent>> extends SimplifiedLogbackLogger {

    protected A appender;

    public SingleAppenderLogger(String name, A appender) {
        super(name);
        this.appender = appender;
        autoStart();
    }

    public SingleAppenderLogger(Class<?> c, A appender) {
        super(c);
        this.appender = appender;
        autoStart();
    }

    public A getAppender() {
        return appender;
    }

    public LoggerContext getLoggerContext() {
        return getLogbackLogger().getLoggerContext();
    }

    protected void autoStart() {
        start();
    }

    @Override
    public boolean isStarted() {
        A appender = getAppender();
        return appender != null && appender.isStarted();
    }

    @Override
    public void start() {
        A appender = getAppender();
        if(appender != null) {
            appender.start();
        }
    }

    @Override
    public void stop() {
        A appender = getAppender();
        if(appender != null) {
            appender.stop();
        }
    }

    @Override
    protected void handleEvent(LoggingEvent event) {
        A appender = getAppender();
        if(appender != null) {
            appender.doAppend(event);
        }
    }
}
