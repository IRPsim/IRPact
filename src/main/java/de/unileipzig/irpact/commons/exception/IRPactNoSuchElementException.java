package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class IRPactNoSuchElementException extends IRPactRuntimeException {

    public IRPactNoSuchElementException() {
        super();
    }

    public IRPactNoSuchElementException(String msg) {
        super(msg);
    }

    public IRPactNoSuchElementException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public IRPactNoSuchElementException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public IRPactNoSuchElementException(String pattern, Object... args) {
        super(pattern, args);
    }

    public IRPactNoSuchElementException(Throwable cause) {
        super(cause);
    }

    public IRPactNoSuchElementException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public IRPactNoSuchElementException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public IRPactNoSuchElementException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public IRPactNoSuchElementException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
