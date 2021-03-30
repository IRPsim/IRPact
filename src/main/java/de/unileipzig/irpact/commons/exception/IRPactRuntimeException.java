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
    }

    public IRPactRuntimeException(IRPactException cause) {
        super(cause);
        if(cause.isFatal()) {
            setFatal();
        }
    }

    public IRPactRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public void setFatal() {
        this.fatal = true;
    }

    public boolean isFatal() {
        return fatal;
    }
}
