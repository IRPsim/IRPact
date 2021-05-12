package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class InvalidDataException extends IRPactException {

    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String msg) {
        super(msg);
    }

    public InvalidDataException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public InvalidDataException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public InvalidDataException(String pattern, Object... args) {
        super(pattern, args);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public InvalidDataException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public InvalidDataException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public InvalidDataException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }

    @Override
    public UncheckedInvalidDataException unchecked() {
        return new UncheckedInvalidDataException(this);
    }
}
