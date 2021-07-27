package de.unileipzig.irpact.core.logging;

import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public interface LoggingHelper {

    default IRPLogger getDefaultLogger() {
        throw new UnsupportedOperationException();
    }

    default boolean useDefaultSection() {
        return true;
    }

    default IRPSection getDefaultSection() {
        return null;
    }

    //=========================
    //trace
    //=========================

    default void trace(String msg) {
        IRPLogging.trace(getDefaultLogger(), getDefaultSection(), useDefaultSection(), msg);
    }

    default void trace(String format, Object arg) {
        IRPLogging.trace(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg);
    }

    default void trace(String format, Object arg1, Object arg2) {
        IRPLogging.trace(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg1, arg2);
    }
    default void trace(IRPSection section, String format, Object arg1, Object arg2) {
        getDefaultLogger().trace(section, format, arg1, arg2);
    }

    default void trace(String format, Object... args) {
        IRPLogging.trace(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, args);
    }

    //=========================
    //debug
    //=========================

    default void debug(String msg) {
        IRPLogging.debug(getDefaultLogger(), getDefaultSection(), useDefaultSection(), msg);
    }

    default void debug(String format, Object arg) {
        IRPLogging.debug(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg);
    }

    default void debug(String format, Object arg1, Object arg2) {
        IRPLogging.debug(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg1, arg2);
    }

    default void debug(String format, Object... args) {
        IRPLogging.debug(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, args);
    }

    //=========================
    //info
    //=========================

    default void info(String msg) {
        IRPLogging.info(getDefaultLogger(), getDefaultSection(), useDefaultSection(), msg);
    }

    default void info(String format, Object arg) {
        IRPLogging.info(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg);
    }

    default void info(String format, Object arg1, Object arg2) {
        IRPLogging.info(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg1, arg2);
    }

    default void info(String format, Object... args) {
        IRPLogging.info(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, args);
    }

    //=========================
    //warn
    //=========================

    default void warn(String msg) {
        IRPLogging.warn(getDefaultLogger(), getDefaultSection(), useDefaultSection(), msg);
    }

    default void warn(String format, Object arg) {
        IRPLogging.warn(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg);
    }

    default void warn(String format, Object arg1, Object arg2) {
        IRPLogging.warn(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg1, arg2);
    }

    default void warn(String format, Object... args) {
        IRPLogging.warn(getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, args);
    }

    //=========================
    //error
    //=========================

    default void error(String msg) {
        IRPLogging.error(getDefaultLogger(), msg);
    }

    default void error(String msg, Throwable cause) {
        IRPLogging.error(getDefaultLogger(), msg, cause);
    }

    default void error(Throwable cause) {
        IRPLogging.error(getDefaultLogger(), cause);
    }

    default void error(String format, Object arg) {
        IRPLogging.error(getDefaultLogger(), format, arg);
    }

    default void error(String format, Object arg1, Object arg2) {
        IRPLogging.error(getDefaultLogger(), format, arg1, arg2);
    }

    default void error(String format, Object... args) {
        IRPLogging.error(getDefaultLogger(), format, args);
    }

    //=========================
    //level
    //=========================

    default void log(Level level, String msg) {
        IRPLogging.log(level, getDefaultLogger(), getDefaultSection(), useDefaultSection(), msg);
    }

    default void log(Level level, String format, Object arg) {
        IRPLogging.log(level, getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg);
    }

    default void log(Level level, String format, Object arg1, Object arg2) {
        IRPLogging.log(level, getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, arg1, arg2);
    }

    default void log(Level level, String format, Object... args) {
        IRPLogging.log(level, getDefaultLogger(), getDefaultSection(), useDefaultSection(), format, args);
    }

    //=========================
    //result
    //=========================

    default IRPLogger getDefaultResultLogger() {
        throw new UnsupportedOperationException();
    }

    default boolean useDefaultResultSection() {
        return useDefaultSection();
    }

    default IRPSection getDefaultResultSection() {
        return getDefaultSection();
    }

    default void logResult(String msg) {
        if(useDefaultResultSection()) {
            getDefaultResultLogger().info(getDefaultResultSection(), msg);
        } else {
            getDefaultResultLogger().info(msg);
        }
    }

    default void logResult(String format, Object arg) {
        if(useDefaultResultSection()) {
            getDefaultResultLogger().info(getDefaultResultSection(), format, arg);
        } else {
            getDefaultResultLogger().info(format, arg);
        }
    }

    default void logResult(String format, Object arg1, Object arg2) {
        if(useDefaultResultSection()) {
            getDefaultResultLogger().info(getDefaultResultSection(), format, arg1, arg2);
        } else {
            getDefaultResultLogger().info(format, arg1, arg2);
        }
    }

    default void logResult(String format, Object... args) {
        if(useDefaultResultSection()) {
            getDefaultResultLogger().info(getDefaultResultSection(), format, args);
        } else {
            getDefaultResultLogger().info(format, args);
        }
    }
}
