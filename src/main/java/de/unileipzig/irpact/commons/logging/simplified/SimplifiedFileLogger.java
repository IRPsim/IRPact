package de.unileipzig.irpact.commons.logging.simplified;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import de.unileipzig.irpact.commons.logging.Logback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class SimplifiedFileLogger extends SingleAppenderLogger<FileAppender<ILoggingEvent>> {

    public SimplifiedFileLogger(String name, FileAppender<ILoggingEvent> appender) {
        super(name, appender);
    }

    public SimplifiedFileLogger(Class<?> c, FileAppender<ILoggingEvent> appender) {
        super(c, appender);
    }

    public static FileAppender<ILoggingEvent> createNew(String name, String pattern, Path target) throws IOException {
        if(Files.exists(target)) {
            Files.delete(target);
        }
        return create(name, pattern, target);
    }

    public static FileAppender<ILoggingEvent> create(String name, String pattern, Path target) {
        return Logback.createFileAppender(name, pattern, target);
    }

    public Path getTarget() {
        String file = getAppender().getFile();
        return Paths.get(file);
    }
}
