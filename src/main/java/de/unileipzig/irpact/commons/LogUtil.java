package de.unileipzig.irpact.commons;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public final class LogUtil {

    private LogUtil() {
    }

    public static boolean disableAppender(Logger logger, String appenderName) {
        ch.qos.logback.classic.Logger lLogger = (ch.qos.logback.classic.Logger) logger;
        return lLogger.detachAppender(appenderName);
    }

    public static void setLevel(Logger logger, ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger lLogger = (ch.qos.logback.classic.Logger) logger;
        lLogger.setLevel(level);
    }

    public static void setLevel(Logger logger, Level level) {
        setLevel(logger, slj4LevelToLogbackLevel(level));
    }

    private static ch.qos.logback.classic.Level slj4LevelToLogbackLevel(Level level) {
        switch (level) {
            case ERROR:
                return ch.qos.logback.classic.Level.ERROR;
            case WARN:
                return ch.qos.logback.classic.Level.WARN;
            case INFO:
                return ch.qos.logback.classic.Level.INFO;
            case DEBUG:
                return ch.qos.logback.classic.Level.DEBUG;
            case TRACE:
                return ch.qos.logback.classic.Level.TRACE;
            default:
                return ch.qos.logback.classic.Level.OFF;
        }
    }
}
