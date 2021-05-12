package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException(Throwable t) {
        super(t);
    }
}
