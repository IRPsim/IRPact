package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public class UncheckedPersistException extends IRPactRuntimeException {

    public UncheckedPersistException() {
        super();
    }

    public UncheckedPersistException(String msg) {
        super(msg);
    }

    public UncheckedPersistException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public UncheckedPersistException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public UncheckedPersistException(String pattern, Object... args) {
        super(pattern, args);
    }

    public UncheckedPersistException(Throwable cause) {
        super(cause);
    }

    public UncheckedPersistException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public UncheckedPersistException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public UncheckedPersistException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public UncheckedPersistException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }

    @Override
    public synchronized PersistException getCause() {
        return (PersistException) super.getCause();
    }
}
