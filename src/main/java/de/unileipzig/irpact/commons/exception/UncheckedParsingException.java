package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedParsingException extends IRPactRuntimeException {

    public UncheckedParsingException(ParsingException cause) {
        super(cause);
    }

    @Override
    public synchronized ParsingException getCause() {
        return (ParsingException) super.getCause();
    }
}
