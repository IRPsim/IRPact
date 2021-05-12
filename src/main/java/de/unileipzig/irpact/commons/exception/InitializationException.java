package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class InitializationException extends IRPactException {

    public InitializationException() {
        super();
    }

    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public InitializationException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public InitializationException(String pattern, Object... args) {
        super(pattern, args);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public InitializationException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public InitializationException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public InitializationException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
