package de.unileipzig.irpact.commons.log;

import de.unileipzig.irptools.util.log.IRPLogger;
import de.unileipzig.irptools.util.log.LoggingSection;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class IRPLoggingMessage extends LoggingMessage {

    protected LoggingSection section;

    public IRPLoggingMessage(String message) {
        super(message);
    }

    public IRPLoggingMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public IRPLoggingMessage(String message, Object arg) {
        super(message, arg);
    }

    public IRPLoggingMessage(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }

    public IRPLoggingMessage(String message, Object... args) {
        super(message, args);
    }

    //=====

    public IRPLoggingMessage(LoggingSection section, String message) {
        super(message);
        this.section = section;
    }

    public IRPLoggingMessage(LoggingSection section, String message, Throwable cause) {
        super(message, cause);
        this.section = section;
    }

    public IRPLoggingMessage(LoggingSection section, String message, Object arg) {
        this(section, message, new Object[]{arg});
    }

    public IRPLoggingMessage(LoggingSection section, String message, Object arg1, Object arg2) {
        this(section, message, new Object[]{arg1, arg2});
    }

    public IRPLoggingMessage(LoggingSection section, String message, Object... args) {
        super(message, args);
        this.section = section;
    }

    //=====

    @Override
    public IRPLoggingMessage setLevel(Level level) {
        super.setLevel(level);
        return this;
    }

    public LoggingSection getSection() {
        return section;
    }

    public void log(IRPLogger logger) {
        doLog(logger, level);
    }

    public void log(IRPLogger logger, Level ifNoLevel) {
        doLog(logger, hasLevel() ? level : ifNoLevel);
    }

    protected void doLog(IRPLogger logger, Level level) {
        if(hasLevel()) {
            switch (level) {
                case ERROR:
                    error(logger);
                    break;
                case WARN:
                    warn(logger);
                    break;
                case INFO:
                    info(logger);
                    break;
                case DEBUG:
                    debug(logger);
                    break;
                case TRACE:
                    trace(logger);
                    break;
            }
        } else {
            debug(logger);
        }
    }

    public void trace(IRPLogger logger) {
        logIRP(
                logger::trace,
                logger::trace,
                logger::trace,
                logger::trace,
                logger::trace
        );
    }

    public void debug(IRPLogger logger) {
        logIRP(
                logger::debug,
                logger::debug,
                logger::debug,
                logger::debug,
                logger::debug
        );
    }

    public void info(IRPLogger logger) {
        logIRP(
                logger::info,
                logger::info,
                logger::info,
                logger::info,
                logger::info
        );
    }

    public void warn(IRPLogger logger) {
        logIRP(
                logger::warn,
                logger::warn,
                logger::warn,
                logger::warn,
                logger::warn
        );
    }

    public void error(IRPLogger logger) {
        log(
                logger::error,
                logger::error,
                logger::error,
                logger::error,
                logger::error
        );
    }

    protected void logIRP(
            IRPLogMessage logMessage,
            IRPLogWithArgument logWithArgument,
            IRPLogWithTwoArguments logWithTwoArguments,
            IRPLogWithrguments logWithrguments,
            LogWithError logWithError) {
        if(hasCause()) {
            logWithError.log(getMessage(), getCause());
        }
        else if(hasArguments()) {
            switch(getNumberOfArguments()) {
                case 1:
                    logWithArgument.log(getSection(), getMessage(), getFirstArgument());
                    break;

                case 2:
                    logWithTwoArguments.log(getSection(), getMessage(), getFirstArgument(), getSecondArgument());
                    break;

                default:
                    logWithrguments.log(getSection(), getMessage(), getArguments());
                    break;
            }
        }
        else {
            logMessage.log(getSection(), getMessage());
        }
    }

    /**
     * @author Daniel Abitz
     */
    private interface IRPLogMessage {

        void log(LoggingSection section, String msg);
    }

    /**
     * @author Daniel Abitz
     */
    protected interface IRPLogWithArgument {

        void log(LoggingSection section, String format, Object arg);
    }

    /**
     * @author Daniel Abitz
     */
    protected interface IRPLogWithTwoArguments {

        void log(LoggingSection section, String format, Object arg1, Object arg2);
    }

    /**
     * @author Daniel Abitz
     */
    protected interface IRPLogWithrguments {

        void log(LoggingSection section, String format, Object[] arguments);
    }
}
