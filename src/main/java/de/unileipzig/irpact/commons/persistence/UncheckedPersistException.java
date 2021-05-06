package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public class UncheckedPersistException extends IRPactRuntimeException {

    public UncheckedPersistException(PersistException cause) {
        super(cause);
    }

    @Override
    public synchronized PersistException getCause() {
        return (PersistException) super.getCause();
    }
}
