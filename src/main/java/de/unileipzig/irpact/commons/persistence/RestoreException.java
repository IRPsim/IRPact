package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;

/**
 * @author Daniel Abitz
 */
public class RestoreException extends IRPactException {

    public RestoreException() {
        super();
    }

    public RestoreException(String msg) {
        super(msg);
    }

    public RestoreException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public RestoreException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public RestoreException(String pattern, Object... args) {
        super(pattern, args);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }

    public RestoreException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public RestoreException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public RestoreException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public RestoreException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }

    @Override
    public UncheckedRestoreException unchecked() {
        return new UncheckedRestoreException(this);
    }
}
