package de.unileipzig.irpact.commons.persistence;

/**
 * @author Daniel Abitz
 */
public class PersistenceException extends Exception {

    public PersistenceException(String msg) {
        super(msg);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
