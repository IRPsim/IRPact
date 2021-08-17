package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.FilterReply;
import de.unileipzig.irpact.commons.logging.Logback;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class BasicLoggingController implements LoggingController {

    private enum WriteMode {
        NONE,
        CONSOLE,
        FILE,
        CONSOLE_FILE
    }

    private static final String CLEAR_PATTERN = "%msg%n";
    private static final String CLEAR_PATTERN_WITHOUT_BREAK = "%msg";
    private static final String PATTERN = "%d{HH:mm:ss.SSS} [%logger{0},%level,%thread] %msg%n";
    private static final String SYSTEMOUT_APPENDER_NAME = "LOG_SYSTEMOUT";
    private static final String SYSTEMERR_APPENDER_NAME = "LOG_SYSTEMERR";
    private static final String FILE_APPENDER_NAME = "LOG_FILE";
    private static final String RESULT_LOGGER_NAME = "RESULT";

    protected ch.qos.logback.classic.Logger rootLogger;
    protected ch.qos.logback.classic.Logger resultLogger;

    protected LevelFilter systemOutFilter;

    protected ConsoleAppender<ILoggingEvent> systemOutAppender;
    protected ConsoleAppender<ILoggingEvent> systemErrAppender;
    protected FileAppender<ILoggingEvent> fileAppender;

    protected WriteMode writeMode = WriteMode.NONE;
    protected boolean usesFile = false;

    public BasicLoggingController() {
    }

    public void init() {
        systemOutFilter = new LevelFilter();
        systemOutFilter.setLevel(Level.ERROR);
        systemOutFilter.setOnMatch(FilterReply.DENY);
        systemOutFilter.setOnMismatch(FilterReply.ACCEPT);
        systemOutFilter.start();

        systemOutAppender = Logback.createSystemOutAppender(SYSTEMOUT_APPENDER_NAME, PATTERN);
        systemErrAppender = Logback.createSystemErrAppender(SYSTEMERR_APPENDER_NAME, PATTERN);
        fileAppender = Logback.createFileAppender(FILE_APPENDER_NAME, PATTERN, null);

        rootLogger = Logback.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setAdditive(true);
        rootLogger.setLevel(Level.ALL);
        Logback.detachAllAppenders(rootLogger);

        resultLogger = Logback.getLogger(RESULT_LOGGER_NAME);
        resultLogger.setAdditive(false);
        resultLogger.setLevel(Level.ALL);
        Logback.detachAllAppenders(resultLogger);
    }

    //=========================
    // loggers
    //=========================

    @Override
    public Logger getLogger(Class<?> c) {
        return Logback.getLogger(c);
    }

    @Override
    public Logger getLogger(String name) {
        return Logback.getLogger(name);
    }

    @Override
    public Logger getResultLogger() {
        return resultLogger;
    }

    //=========================
    // settings
    //=========================

    @Override
    public void setRootLevel(IRPLevel level) {
        rootLogger.setLevel(level.toLogbackLevel());
    }

    @Override
    public void setResultLevel(IRPLevel level) {
        resultLogger.setLevel(level.toLogbackLevel());
    }

    @Override
    public boolean isWritingToFileAndNotConsole() {
        return writeMode == WriteMode.FILE;
    }

    @Override
    public void setPath(Path path) {
        if(usesFile) fileAppender.stop();
        if(path == null) {
            fileAppender.setFile(null);
        } else {
            String file = path.toFile().toString();
            if(usesFile) fileAppender.stop();
            fileAppender.setFile(file);
            if(usesFile) fileAppender.start();
        }
    }

    @Override
    public void setFilterError(boolean filterError) {
        systemOutAppender.stop();
        if(filterError) {
            systemOutAppender.addFilter(systemOutFilter);
        } else {
            systemOutAppender.clearAllFilters();
        }
        systemOutAppender.start();
    }

    //=========================
    // modes
    //=========================

    protected void changePattern(Appender<?> appender, String pattern) {
        appender.stop();
        Logback.changeLayoutPattern(appender, pattern);
        appender.start();
    }

    @Override
    public void enableClearMode() {
        changePattern(systemOutAppender, CLEAR_PATTERN);
        changePattern(systemErrAppender, CLEAR_PATTERN);
        changePattern(fileAppender, CLEAR_PATTERN);
    }

    @Override
    public void enableWriteMode() {
        changePattern(systemOutAppender, CLEAR_PATTERN_WITHOUT_BREAK);
        changePattern(systemErrAppender, CLEAR_PATTERN_WITHOUT_BREAK);
        changePattern(fileAppender, CLEAR_PATTERN_WITHOUT_BREAK);
    }

    @Override
    public void enableInformationMode() {
        changePattern(systemOutAppender, PATTERN);
        changePattern(systemErrAppender, PATTERN);
        changePattern(fileAppender, PATTERN);
    }

    //=========================
    // target
    //=========================

    protected void stopAndDetachAll() {
        Logback.stopAppenders(rootLogger);
        Logback.stopAppenders(resultLogger);

        Logback.detachAllAppenders(rootLogger);
        Logback.detachAllAppenders(resultLogger);
    }

    @Override
    public void stopWriting() {
        usesFile = false;
        writeMode = WriteMode.NONE;

        stopAndDetachAll();
    }

    @Override
    public void writeToConsole() {
        usesFile = false;
        writeMode = WriteMode.CONSOLE;

        stopAndDetachAll();

        Logback.atachAllAppenders(rootLogger, systemOutAppender, systemErrAppender);
        Logback.atachAllAppenders(resultLogger, systemOutAppender, systemErrAppender);

        Logback.startAppenders(rootLogger);
        Logback.startAppenders(resultLogger);
    }

    @Override
    public void writeToFile() {
        usesFile = true;
        writeMode = WriteMode.FILE;

        stopAndDetachAll();

        Logback.atachAllAppenders(rootLogger, fileAppender);
        Logback.atachAllAppenders(resultLogger, fileAppender);

        Logback.startAppenders(rootLogger);
        Logback.startAppenders(resultLogger);
    }

    @Override
    public void writeToConsoleAndFile() {
        usesFile = true;
        writeMode = WriteMode.CONSOLE_FILE;

        stopAndDetachAll();

        Logback.atachAllAppenders(rootLogger, systemOutAppender, systemErrAppender, fileAppender);
        Logback.atachAllAppenders(resultLogger, systemOutAppender, systemErrAppender, fileAppender);

        Logback.startAppenders(rootLogger);
        Logback.startAppenders(resultLogger);
    }
}
