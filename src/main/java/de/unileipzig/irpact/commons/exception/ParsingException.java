package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class ParsingException extends Exception {

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
