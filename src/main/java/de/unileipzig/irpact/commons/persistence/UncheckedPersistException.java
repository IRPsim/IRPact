package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;

/**
 * @author Daniel Abitz
 */
public class UncheckedPersistException extends IRPactRuntimeException {

    public UncheckedPersistException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized PersistException getCause() {
        return (PersistException) super.getCause();
    }
}
