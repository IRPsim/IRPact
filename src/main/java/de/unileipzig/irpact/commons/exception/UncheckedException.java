package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException(Throwable t) {
        super(t);
    }

    public UncheckedException(String msg, Throwable t) {
        super(msg, t);
    }
}
