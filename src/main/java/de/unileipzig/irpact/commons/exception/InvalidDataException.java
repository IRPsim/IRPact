package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class InvalidDataException extends IRPactException {

    public InvalidDataException(String msg) {
        super(msg);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    @Override
    public UncheckedInvalidDataException unchecked() {
        return new UncheckedInvalidDataException(this);
    }
}
