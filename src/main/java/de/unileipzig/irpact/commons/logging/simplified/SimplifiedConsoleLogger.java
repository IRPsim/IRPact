package de.unileipzig.irpact.commons.logging.simplified;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import de.unileipzig.irpact.commons.logging.Logback;

/**
 * @author Daniel Abitz
 */
public class SimplifiedConsoleLogger extends SingleAppenderLogger<ConsoleAppender<ILoggingEvent>> {

    public SimplifiedConsoleLogger(String name, ConsoleAppender<ILoggingEvent> appender) {
        super(name, appender);
    }

    public SimplifiedConsoleLogger(Class<?> c, ConsoleAppender<ILoggingEvent> appender) {
        super(c, appender);
    }

    public static ConsoleAppender<ILoggingEvent> create(String name, String pattern) {
        return Logback.createSystemOutAppender(name, pattern);
    }
}
