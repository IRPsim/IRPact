package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedInvalidDataException extends IRPactRuntimeException {

    public UncheckedInvalidDataException(InvalidDataException cause) {
        super(cause);
    }

    @Override
    public synchronized InvalidDataException getCause() {
        return (InvalidDataException) super.getCause();
    }
}
