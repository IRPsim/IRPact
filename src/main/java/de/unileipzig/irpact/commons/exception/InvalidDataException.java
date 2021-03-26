package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class InvalidDataException extends Exception {

    public InvalidDataException(String msg) {
        super(msg);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }
}
