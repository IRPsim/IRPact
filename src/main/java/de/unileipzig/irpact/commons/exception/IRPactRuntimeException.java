package de.unileipzig.irpact.commons.exception;

import de.unileipzig.irpact.commons.util.StringUtil;

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

    public IRPactRuntimeException(String pattern, Object arg) {
        super(StringUtil.format(pattern, arg));
    }

    public IRPactRuntimeException(String pattern, Object arg1, Object arg2) {
        super(StringUtil.format(pattern, arg1, arg2));
    }

    public IRPactRuntimeException(String pattern, Object... args) {
        super(StringUtil.format(pattern, args));
    }

    public IRPactRuntimeException(Throwable cause) {
        super(cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException(Throwable cause, String msg) {
        super(msg, cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException(Throwable cause, String pattern, Object arg) {
        super(StringUtil.format(pattern, arg), cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(StringUtil.format(pattern, arg1, arg2), cause);
        checkFatal(cause);
    }

    public IRPactRuntimeException(Throwable cause, String pattern, Object... args) {
        super(StringUtil.format(pattern, args), cause);
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
