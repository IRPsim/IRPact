package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class ParsingException extends IRPactException {

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public UncheckedParsingException unchecked() {
        return new UncheckedParsingException(this);
    }
}
