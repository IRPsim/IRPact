package de.unileipzig.irpact.core.log;

import de.unileipzig.irpact.commons.log.AbstractLoggingMessageCollection;
import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class IRPLoggingMessageCollection extends AbstractLoggingMessageCollection {

    protected String delimiter = "\n";
    protected Level level = Level.TRACE;
    protected IRPSection section;
    protected boolean disableSection = false;
    protected boolean doLazy = true;
    protected boolean autoDispose = true;

    public IRPLoggingMessageCollection() {
    }

    public IRPLoggingMessageCollection setLazy(boolean doLazy) {
        this.doLazy = doLazy;
        return this;
    }

    public IRPLoggingMessageCollection setLevel(Level level) {
        this.level = level;
        return this;
    }

    public IRPLoggingMessageCollection setSection(IRPSection section) {
        this.section = section;
        return this;
    }

    public IRPLoggingMessageCollection setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public IRPLoggingMessageCollection disableSection() {
        disableSection = true;
        return this;
    }

    public IRPLoggingMessageCollection enableSection() {
        disableSection = false;
        return this;
    }

    public IRPLoggingMessageCollection setAutoDispose(boolean autoDispose) {
        this.autoDispose = autoDispose;
        return this;
    }

    private boolean useSection() {
        if(disableSection) {
            return false;
        } else {
            return section != null;
        }
    }

    private static IRPLogger cast(Logger logger) {
        if(logger.getClass() == IRPLogger.class) {
            return (IRPLogger) logger;
        } else {
            throw new IllegalArgumentException("requires IRPLogger");
        }
    }

    @Override
    public AbstractLoggingMessageCollection append(LoggingMessage msg) {
        parts.add(msg);
        return this;
    }

    @Override
    public IRPLoggingMessageCollection append(String msg) {
        parts.add(new IRPLoggingMessage(msg));
        return this;
    }

    @Override
    public IRPLoggingMessageCollection append(String pattern, Object arg) {
        parts.add(new IRPLoggingMessage(pattern, arg));
        return this;
    }

    @Override
    public IRPLoggingMessageCollection append(String pattern, Object arg1, Object arg2) {
        parts.add(new IRPLoggingMessage(pattern, arg1, arg2));
        return this;
    }

    @Override
    public IRPLoggingMessageCollection append(String pattern, Object... args) {
        parts.add(new IRPLoggingMessage(pattern, args));
        return this;
    }

    @Override
    public String format() {
        return buildMessage(delimiter);
    }

    @Override
    public void trace(Logger logger) {
        trace(cast(logger));
    }
    public void trace(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.trace(logger, section, useSection(), "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.trace(logger, section, useSection(), buildMessage(delimiter));
        }
        tryDispose();
    }

    @Override
    public void debug(Logger logger) {
        debug(cast(logger));
    }
    public void debug(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.debug(logger, section, useSection(), "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.debug(logger, section, useSection(), buildMessage(delimiter));
        }
        tryDispose();
    }

    @Override
    public void info(Logger logger) {
        info(cast(logger));
    }
    public void info(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.info(logger, section, useSection(), "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.info(logger, section, useSection(), buildMessage(delimiter));
        }
        tryDispose();
    }

    @Override
    public void warn(Logger logger) {
        warn(cast(logger));
    }
    public void warn(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.warn(logger, section, useSection(), "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.warn(logger, section, useSection(), buildMessage(delimiter));
        }
        tryDispose();
    }

    @Override
    public void error(Logger logger) {
        error(cast(logger));
    }
    public void error(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.error(logger, "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.error(logger, buildMessage(delimiter));
        }
        tryDispose();
    }

    @Override
    public void log(Logger logger) {
        log(cast(logger));
    }
    public void log(IRPLogger logger) {
        if(doLazy) {
            IRPLogging.log(level, logger, section, useSection(), "{}", buildLazyMessage(delimiter));
        } else {
            IRPLogging.log(level, logger, section, useSection(), buildMessage(delimiter));
        }
        tryDispose();
    }

    private void tryDispose() {
        if(autoDispose) {
            dispose();
        }
    }

    public void dispose() {
        parts.clear();
        parts = null;
        level = null;
        section = null;
    }
}
