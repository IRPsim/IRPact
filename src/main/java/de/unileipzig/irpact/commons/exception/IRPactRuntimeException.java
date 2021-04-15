package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class IRPactRuntimeException extends RuntimeException {

    protected boolean fatal = false;

    public IRPactRuntimeException() {
        super();
    }

    public IRPactRuntimeException(String msg) {
        super(msg);
    }

    public IRPactRuntimeException(Throwable cause) {
        super(cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
        checkFatal(cause);
    }

    private void checkFatal(Throwable cause) {
        if(cause instanceof IRPactRuntimeException) {
            if(((IRPactRuntimeException) cause).isFatal()) {
                setFatal();
            }
        }
        else if(cause instanceof IRPactException) {
            if(((IRPactException) cause).isFatal()) {
                setFatal();
            }
        }
    }

    public void setFatal() {
        this.fatal = true;
    }

    public boolean isFatal() {
        return fatal;
    }
}
