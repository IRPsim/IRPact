package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class IRPactIllegalArgumentException extends IRPactRuntimeException {

    public IRPactIllegalArgumentException() {
        super();
    }

    public IRPactIllegalArgumentException(String msg) {
        super(msg);
    }

    public IRPactIllegalArgumentException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public IRPactIllegalArgumentException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public IRPactIllegalArgumentException(String pattern, Object... args) {
        super(pattern, args);
    }

    public IRPactIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public IRPactIllegalArgumentException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public IRPactIllegalArgumentException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public IRPactIllegalArgumentException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public IRPactIllegalArgumentException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
