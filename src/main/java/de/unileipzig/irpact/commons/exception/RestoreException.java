package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class RestoreException extends Exception {

    public RestoreException(String msg) {
        super(msg);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }
}
