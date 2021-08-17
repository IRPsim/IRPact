package de.unileipzig.irpact.commons.exception;

import de.unileipzig.irpact.commons.util.StringUtil;

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

    public IRPactException(String pattern, Object arg) {
        super(StringUtil.format(pattern, arg));
    }

    public IRPactException(String pattern, Object arg1, Object arg2) {
        super(StringUtil.format(pattern, arg1, arg2));
    }

    public IRPactException(String pattern, Object... args) {
        super(StringUtil.format(pattern, args));
    }

    public IRPactException(Throwable cause) {
        super(cause);
        checkFatal(cause);
    }

    public IRPactException(Throwable cause, String msg) {
        super(msg, cause);
        checkFatal(cause);
    }

    public IRPactException(Throwable cause, String pattern, Object arg) {
        super(StringUtil.format(pattern, arg), cause);
        checkFatal(cause);
    }

    public IRPactException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(StringUtil.format(pattern, arg1, arg2), cause);
        checkFatal(cause);
    }

    public IRPactException(Throwable cause, String pattern, Object... args) {
        super(StringUtil.format(pattern, args), cause);
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
