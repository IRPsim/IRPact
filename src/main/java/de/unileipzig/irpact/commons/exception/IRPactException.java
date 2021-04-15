package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class IRPactException extends Exception {

    protected boolean fatal = false;

    public IRPactException() {
        super();
    }

    public IRPactException(String msg) {
        super(msg);
    }

    public IRPactException(Throwable cause) {
        super(cause);
        checkFatal(cause);
    }

    public IRPactException(String msg, Throwable cause) {
        super(msg, cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException unchecked() {
        return new IRPactRuntimeException(this);
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
