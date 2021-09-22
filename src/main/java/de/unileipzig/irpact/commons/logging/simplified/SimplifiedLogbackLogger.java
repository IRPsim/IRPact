package de.unileipzig.irpact.commons.logging.simplified;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import de.unileipzig.irpact.commons.logging.Logback;

/**
 * @author Daniel Abitz
 */
public abstract class SimplifiedLogbackLogger implements SimplifiedLogger {

    protected Logger logbackLogger;
    protected Level defaultLevel = Level.INFO;

    public SimplifiedLogbackLogger(String name) {
        logbackLogger = Logback.getContext().getLogger(name);
    }

    public SimplifiedLogbackLogger(Class<?> c) {
        logbackLogger = Logback.getContext().getLogger(c);
    }

    protected String getFQCN() {
        return Logger.FQCN;
    }

    public Logger getLogbackLogger() {
        return logbackLogger;
    }

    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public String getName() {
        return getLogbackLogger().getName();
    }

    @Override
    public void log(String msg) {
        handleLog(msg, null);
    }

    @Override
    public void log(String format, Object arg) {
        handleLog(format, new Object[] {arg});
    }

    @Override
    public void log(String format, Object arg1, Object arg2) {
        handleLog(format, new Object[] {arg1, arg2});
    }

    @Override
    public void log(String format, Object... args) {
        handleLog(format, args);
    }

    protected void handleLog(String msg, Object[] params) {
        LoggingEvent event = new LoggingEvent(getFQCN(), getLogbackLogger(), getDefaultLevel(), msg, null, params);
        event.setMarker(null);
        handleEvent(event);
    }

    protected abstract void handleEvent(LoggingEvent event);
}
