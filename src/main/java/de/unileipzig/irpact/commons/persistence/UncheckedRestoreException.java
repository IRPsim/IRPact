package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.IRPactRuntimeException;

/**
 * @author Daniel Abitz
 */
public class UncheckedRestoreException extends IRPactRuntimeException {

    public UncheckedRestoreException(RestoreException cause) {
        super(cause);
    }

    @Override
    public synchronized RestoreException getCause() {
        return (RestoreException) super.getCause();
    }
}
