package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class EdgeAlreadyExistsException extends RuntimeException {

    public EdgeAlreadyExistsException() {
        super();
    }

    public EdgeAlreadyExistsException(String msg) {
        super(msg);
    }
}
