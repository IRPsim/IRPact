package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class RestoreException extends IRPactException {

    public RestoreException(String msg) {
        super(msg);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }
}
