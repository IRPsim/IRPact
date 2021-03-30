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
    }

    public IRPactException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IRPactRuntimeException unchecked() {
        return new IRPactRuntimeException(this);
    }

    public void setFatal() {
        this.fatal = true;
    }

    public boolean isFatal() {
        return fatal;
    }
}
