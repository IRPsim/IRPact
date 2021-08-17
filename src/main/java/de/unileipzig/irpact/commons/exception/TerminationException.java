package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class TerminationException extends IRPactException {

    public TerminationException() {
        super();
    }

    public TerminationException(String msg) {
        super(msg);
    }

    public TerminationException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public TerminationException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public TerminationException(String pattern, Object... args) {
        super(pattern, args);
    }

    public TerminationException(Throwable cause) {
        super(cause);
    }

    public TerminationException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public TerminationException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public TerminationException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public TerminationException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
