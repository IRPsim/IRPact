package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedParsingException extends UncheckedException {

    public UncheckedParsingException(String msg) {
        super(new ParsingException(msg));
    }

    public UncheckedParsingException(ParsingException cause) {
        super(cause);
    }

    @Override
    public synchronized ParsingException getCause() {
        return (ParsingException) super.getCause();
    }
}
