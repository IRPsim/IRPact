package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UnsupportedCellTypeException extends IRPactRuntimeException {

    public UnsupportedCellTypeException() {
        super();
    }

    public UnsupportedCellTypeException(String msg) {
        super(msg);
    }

    public UnsupportedCellTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedCellTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
