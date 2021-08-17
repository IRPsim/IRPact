package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import de.unileipzig.irpact.commons.logging.Logback;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class ConsoleFileLoggingSetup implements LoggingSetup {

    protected ConsoleAppender<ILoggingEvent> systemOutAppender;
    protected ConsoleAppender<ILoggingEvent> systemErrAppender;
    protected FileAppender<ILoggingEvent> fileAppender;

    public ConsoleFileLoggingSetup(
            ConsoleAppender<ILoggingEvent> systemOutAppender,
            ConsoleAppender<ILoggingEvent> systemErrAppender,
            FileAppender<ILoggingEvent> fileAppender) {
        this.systemOutAppender = systemOutAppender;
        this.systemErrAppender = systemErrAppender;
        this.fileAppender = fileAppender;
    }

    @Override
    public void setFile(Path path) {
        String file = path.toFile().toString();
        fileAppender.setFile(file);
    }

    @Override
    public void apply(Logger rootLogger, Logger resultLogger) {
        Logback.stopAppenders(rootLogger);
        Logback.stopAppenders(resultLogger);

        Logback.detachAllAppenders(rootLogger);
        Logback.detachAllAppenders(resultLogger);

        rootLogger.addAppender(systemOutAppender);
        rootLogger.addAppender(systemErrAppender);
        rootLogger.addAppender(fileAppender);

        resultLogger.addAppender(systemOutAppender);
        resultLogger.addAppender(systemErrAppender);
        resultLogger.addAppender(fileAppender);
    }
}
