package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UnsupportedCellTypeException extends ParsingException {

    public UnsupportedCellTypeException() {
        super();
    }

    public UnsupportedCellTypeException(String msg) {
        super(msg);
    }

    public UnsupportedCellTypeException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public UnsupportedCellTypeException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public UnsupportedCellTypeException(String pattern, Object... args) {
        super(pattern, args);
    }

    public UnsupportedCellTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedCellTypeException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public UnsupportedCellTypeException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public UnsupportedCellTypeException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public UnsupportedCellTypeException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }
}
