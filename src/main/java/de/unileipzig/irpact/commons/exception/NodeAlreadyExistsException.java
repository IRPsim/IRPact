package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class NodeAlreadyExistsException extends RuntimeException {

    public NodeAlreadyExistsException() {
        super();
    }

    public NodeAlreadyExistsException(String msg) {
        super(msg);
    }
}
