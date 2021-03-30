package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class InitializationException extends IRPactException {

    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
