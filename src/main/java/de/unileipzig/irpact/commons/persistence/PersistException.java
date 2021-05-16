package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;

/**
 * @author Daniel Abitz
 */
public class PersistException extends IRPactException {

    public PersistException() {
        super();
    }

    public PersistException(String msg) {
        super(msg);
    }

    public PersistException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public PersistException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public PersistException(String pattern, Object... args) {
        super(pattern, args);
    }

    public PersistException(Throwable cause) {
        super(cause);
    }

    public PersistException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public PersistException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public PersistException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public PersistException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }

    @Override
    public UncheckedPersistException unchecked() {
        return new UncheckedPersistException(this);
    }
}
