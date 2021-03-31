package de.unileipzig.irpact.commons.log;

import de.unileipzig.irpact.commons.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class LoggingMessage {

    protected String message;
    protected Object[] args;
    protected Throwable cause;
    protected Level level;

    public LoggingMessage(String message) {
        this.message = message;
    }

    public LoggingMessage(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    public LoggingMessage(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public LoggingMessage(String message, Object arg) {
        this((Void) null, message, new Object[]{arg});
    }

    public LoggingMessage(String message, Object arg1, Object arg2) {
        this((Void) null, message, new Object[]{arg1, arg2});
    }

    public LoggingMessage(String message, Object... args) {
        this((Void) null, message, args);
    }

    protected LoggingMessage(@SuppressWarnings("unused") Void temp, String message, Object[] args) {
        this.message = message;
        this.args = args;
    }

    public boolean hasCause() {
        return cause != null;
    }

    public Throwable getCause() {
        return cause;
    }

    public boolean hasArguments() {
        return getNumberOfArguments() > 0;
    }

    public int getNumberOfArguments() {
        return args == null
                ? 0
                : args.length;
    }

    public Object getArgument(int index) {
        return args[index];
    }

    public Object getFirstArgument() {
        return getArgument(0);
    }

    public Object getSecondArgument() {
        return getArgument(1);
    }

    public Object[] getArguments() {
        return args;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasLevel() {
        return level != null;
    }

    public String format() {
        if(hasArguments()) {
            switch(getNumberOfArguments()) {
                case 1:
                    return StringUtil.format(getMessage(), getFirstArgument());

                case 2:
                    return StringUtil.format(getMessage(), getFirstArgument(), getSecondArgument());

                default:
                    return StringUtil.format(getMessage(), getArguments());
            }
        }
        else {
            return getMessage();
        }
    }

    public void log(Logger logger, Level ifNoLevel) {
        doLog(logger, hasLevel() ? level : ifNoLevel);
    }

    public void log(Logger logger) {
        doLog(logger, level);
    }

    private void doLog(Logger logger, Level level) {
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

    public void trace(Logger logger) {
        log(
                logger::trace,
                logger::trace,
                logger::trace,
                logger::trace,
                logger::trace
        );
    }

    public void debug(Logger logger) {
        log(
                logger::debug,
                logger::debug,
                logger::debug,
                logger::debug,
                logger::debug
        );
    }

    public void info(Logger logger) {
        log(
                logger::info,
                logger::info,
                logger::info,
                logger::info,
                logger::info
        );
    }

    public void warn(Logger logger) {
        log(
                logger::warn,
                logger::warn,
                logger::warn,
                logger::warn,
                logger::warn
        );
    }

    public void error(Logger logger) {
        log(
                logger::error,
                logger::error,
                logger::error,
                logger::error,
                logger::error
        );
    }

    private void log(
            LogMessage logMessage,
            LogWithArgument logWithArgument,
            LogWithTwoArguments logWithTwoArguments,
            LogWithrguments logWithrguments,
            LogWithError logWithError) {
        if(hasCause()) {
            logWithError.log(getMessage(), getCause());
        }
        else if(hasArguments()) {
            switch(getNumberOfArguments()) {
                case 1:
                    logWithArgument.log(getMessage(), getFirstArgument());
                    break;

                case 2:
                    logWithTwoArguments.log(getMessage(), getFirstArgument(), getSecondArgument());
                    break;

                default:
                    logWithrguments.log(getMessage(), getArguments());
                    break;
            }
        }
        else {
            logMessage.log(getMessage());
        }
    }

    /**
     * @author Daniel Abitz
     */
    private interface LogWithError {

        void log(String msg, Throwable cause);
    }

    /**
     * @author Daniel Abitz
     */
    private interface LogMessage {

        void log(String msg);
    }

    /**
     * @author Daniel Abitz
     */
    private interface LogWithArgument {

        void log(String format, Object arg);
    }

    /**
     * @author Daniel Abitz
     */
    private interface LogWithTwoArguments {

        void log(String format, Object arg1, Object arg2);
    }

    /**
     * @author Daniel Abitz
     */
    private interface LogWithrguments {

        void log(String format, Object[] arguments);
    }
}
