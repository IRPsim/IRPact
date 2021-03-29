package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class IRPactRuntimeException extends RuntimeException {

    public IRPactRuntimeException() {
        super();
    }

    public IRPactRuntimeException(String msg) {
        super(msg);
    }

    public IRPactRuntimeException(Throwable cause) {
        super(cause);
    }

    public IRPactRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
