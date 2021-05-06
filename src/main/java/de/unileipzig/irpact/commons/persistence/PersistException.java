package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactException;

/**
 * @author Daniel Abitz
 */
public class PersistException extends IRPactException {

    public PersistException(String msg) {
        super(msg);
    }

    public PersistException(Throwable cause) {
        super(cause);
    }
}
