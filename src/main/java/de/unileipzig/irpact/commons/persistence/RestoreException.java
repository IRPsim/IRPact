package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactException;

/**
 * @author Daniel Abitz
 */
public class RestoreException extends IRPactException {

    public RestoreException(String msg) {
        super(msg);
    }

    public RestoreException(Throwable cause) {
        super(cause);
    }
}
