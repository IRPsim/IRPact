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
        setLevel(logger, level.toInt());
    }

    public static void setLevel(Logger logger, int level) {
        setLevel(logger, ch.qos.logback.classic.Level.toLevel(level));
    }
}
