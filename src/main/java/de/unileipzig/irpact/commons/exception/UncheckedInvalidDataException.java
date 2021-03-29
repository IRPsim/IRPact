package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedInvalidDataException extends IRPactRuntimeException {

    public UncheckedInvalidDataException(String msg) {
        super(new ParsingException(msg));
    }

    public UncheckedInvalidDataException(InvalidDataException cause) {
        super(cause);
    }

    @Override
    public synchronized InvalidDataException getCause() {
        return (InvalidDataException) super.getCause();
    }
}
