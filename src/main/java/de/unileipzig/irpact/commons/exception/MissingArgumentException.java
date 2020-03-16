package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class MissingArgumentException extends RuntimeException {

    public MissingArgumentException(String msg) {
        super(msg);
    }
}
