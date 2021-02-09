package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class KeyAlreadyExistsException extends RuntimeException {

    public KeyAlreadyExistsException(String msg) {
        super(msg);
    }
}
