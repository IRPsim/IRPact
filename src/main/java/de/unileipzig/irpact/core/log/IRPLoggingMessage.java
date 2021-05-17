package de.unileipzig.irpact.core.log;

import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public final class IRPLoggingMessage implements LoggingMessage {

    /**
     * @author Daniel Abitz
     */
    protected enum Mode {
        MSG {
            @Override
            String format(IRPLoggingMessage msg) {
                return msg.text;
            }

            @Override
            void trace(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.trace(logger, msg.section, msg.useSection(), msg.text);
            }

            @Override
            void debug(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.debug(logger, msg.section, msg.useSection(), msg.text);
            }

            @Override
            void info(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.info(logger, msg.section, msg.useSection(), msg.text);
            }

            @Override
            void warn(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.warn(logger, msg.section, msg.useSection(), msg.text);
            }

            @Override
            void error(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text);
            }

            @Override
            void log(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.log(msg.level, logger, msg.section, msg.useSection(), msg.text);
            }
        },
        ARG1 {
            @Override
            String format(IRPLoggingMessage msg) {
                return StringUtil.format(msg.text, msg.arg1);
            }

            @Override
            void trace(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.trace(logger, msg.section, msg.useSection(), msg.text, msg.arg1);
            }

            @Override
            void debug(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.debug(logger, msg.section, msg.useSection(), msg.text, msg.arg1);
            }

            @Override
            void info(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.info(logger, msg.section, msg.useSection(), msg.text, msg.arg1);
            }

            @Override
            void warn(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.warn(logger, msg.section, msg.useSection(), msg.text, msg.arg1);
            }

            @Override
            void error(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.arg1);
            }

            @Override
            void log(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.log(msg.level, logger, msg.section, msg.useSection(), msg.text, msg.arg1);
            }
        },
        ARG2 {
            @Override
            String format(IRPLoggingMessage msg) {
                return StringUtil.format(msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void trace(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.trace(logger, msg.section, msg.useSection(), msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void debug(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.debug(logger, msg.section, msg.useSection(), msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void info(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.info(logger, msg.section, msg.useSection(), msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void warn(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.warn(logger, msg.section, msg.useSection(), msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void error(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.arg1, msg.arg2);
            }

            @Override
            void log(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.log(msg.level, logger, msg.section, msg.useSection(), msg.text, msg.arg1, msg.arg2);
            }
        },
        ARGN {
            @Override
            String format(IRPLoggingMessage msg) {
                return StringUtil.format(msg.text, msg.args);
            }

            @Override
            void trace(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.trace(logger, msg.section, msg.useSection(), msg.text, msg.args);
            }

            @Override
            void debug(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.debug(logger, msg.section, msg.useSection(), msg.text, msg.args);
            }

            @Override
            void info(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.info(logger, msg.section, msg.useSection(), msg.text, msg.args);
            }

            @Override
            void warn(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.warn(logger, msg.section, msg.useSection(), msg.text, msg.args);
            }

            @Override
            void error(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.args);
            }

            @Override
            void log(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.log(msg.level, logger, msg.section, msg.useSection(), msg.text, msg.args);
            }
        },
        CAUSE {
            @Override
            String format(IRPLoggingMessage msg) {
                return StringUtil.format(msg.text + " | " + msg.cause.getMessage());
            }

            @Override
            void trace(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }

            @Override
            void debug(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }

            @Override
            void info(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }

            @Override
            void warn(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }

            @Override
            void error(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }

            @Override
            void log(IRPLogger logger, IRPLoggingMessage msg) {
                IRPLogging.error(logger, msg.text, msg.cause);
            }
        };

        abstract String format(IRPLoggingMessage msg);

        abstract void trace(IRPLogger logger, IRPLoggingMessage msg);

        abstract void debug(IRPLogger logger, IRPLoggingMessage msg);

        abstract void info(IRPLogger logger, IRPLoggingMessage msg);

        abstract void warn(IRPLogger logger, IRPLoggingMessage msg);

        abstract void error(IRPLogger logger, IRPLoggingMessage msg);

        abstract void log(IRPLogger logger, IRPLoggingMessage msg);
    }

    protected Mode mode;
    protected String text;
    protected Object arg1;
    protected Object arg2;
    protected Object[] args;
    protected Throwable cause;
    protected Level level = Level.TRACE;
    protected IRPSection section;
    protected boolean disableSection = false;
    protected boolean autoDispose = true;

    public IRPLoggingMessage() {
    }

    public IRPLoggingMessage(String msg) {
        set(msg);
    }

    public IRPLoggingMessage(Throwable cause) {
        set(cause);
    }

    public IRPLoggingMessage(String msg, Throwable cause) {
        set(msg, cause);
    }

    public IRPLoggingMessage(String format, Object arg) {
        set(format, arg);
    }

    public IRPLoggingMessage(String format, Object arg1, Object arg2) {
        set(format, arg1, arg2);
    }

    public IRPLoggingMessage(String format, Object... args) {
        set(format, args);
    }

    public IRPLoggingMessage set(String msg) {
        this.mode = Mode.MSG;
        this.text = msg;
        return this;
    }

    public IRPLoggingMessage set(Throwable cause) {
        this.mode = Mode.CAUSE;
        this.text = "";
        this.cause = cause;
        return this;
    }

    public IRPLoggingMessage set(String msg, Throwable cause) {
        this.mode = Mode.CAUSE;
        this.text = msg;
        this.cause = cause;
        return this;
    }

    public IRPLoggingMessage set(String format, Object arg) {
        this.mode = Mode.ARG1;
        this.text = format;
        this.arg1 = arg;
        return this;
    }

    public IRPLoggingMessage set(String format, Object arg1, Object arg2) {
        this.mode = Mode.ARG2;
        this.text = format;
        this.arg1 = arg1;
        this.arg2 = arg2;
        return this;
    }

    public IRPLoggingMessage set(String format, Object... args) {
        this.mode = Mode.ARGN;
        this.text = format;
        this.args = args;
        return this;
    }

    public IRPLoggingMessage setLevel(Level level) {
        this.level = level;
        return this;
    }

    public IRPLoggingMessage setSection(IRPSection section) {
        this.section = section;
        return this;
    }

    public IRPLoggingMessage disableSection() {
        disableSection = true;
        return this;
    }

    public IRPLoggingMessage enableSection() {
        disableSection = false;
        return this;
    }

    public IRPLoggingMessage setAutoDispose(boolean autoDispose) {
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

    @Override
    public String format() {
        return mode.format(this);
    }

    private static IRPLogger cast(Logger logger) {
        if(logger.getClass() == IRPLogger.class) {
            return (IRPLogger) logger;
        } else {
            throw new IllegalArgumentException("requires IRPLogger");
        }
    }

    @Override
    public void trace(Logger logger) {
        trace(cast(logger));
    }

    public void trace(IRPLogger logger) {
        mode.trace(logger, this);
        tryDispose();
    }

    @Override
    public void debug(Logger logger) {
        debug(cast(logger));
    }

    public void debug(IRPLogger logger) {
        mode.debug(logger, this);
        tryDispose();
    }

    @Override
    public void info(Logger logger) {
        info(cast(logger));
    }

    public void info(IRPLogger logger) {
        mode.info(logger, this);
        tryDispose();
    }

    @Override
    public void warn(Logger logger) {
        warn(cast(logger));
    }

    public void warn(IRPLogger logger) {
        mode.warn(logger, this);
        tryDispose();
    }

    @Override
    public void error(Logger logger) {
        error(cast(logger));
    }

    public void error(IRPLogger logger) {
        mode.error(logger, this);
        tryDispose();
    }

    @Override
    public void log(Logger logger) {
        log(cast(logger));
    }

    public void log(IRPLogger logger) {
        mode.log(logger, this);
        tryDispose();
    }

    private void tryDispose() {
        if(autoDispose) {
            dispose();
        }
    }

    public void dispose() {
        mode = null;
        text = null;
        arg1 = null;
        arg2 = null;
        args = null;
        cause = null;
        level = null;
        section = null;
    }
}
