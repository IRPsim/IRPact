package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class VersionMismatchException extends IRPactException {

    public VersionMismatchException() {
        super();
    }

    public VersionMismatchException(String msg) {
        super(msg);
    }

    public VersionMismatchException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public VersionMismatchException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public VersionMismatchException(String pattern, Object... args) {
        super(pattern, args);
    }

    public VersionMismatchException(Throwable cause) {
        super(cause);
    }

    public VersionMismatchException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public VersionMismatchException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public VersionMismatchException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public VersionMismatchException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
