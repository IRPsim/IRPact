package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException(Exception e) {
        super(e);
    }

    public UncheckedException(String msg, Exception e) {
        super(msg, e);
    }
}
