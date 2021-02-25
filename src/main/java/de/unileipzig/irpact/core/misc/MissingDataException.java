package de.unileipzig.irpact.core.misc;

/**
 * @author Daniel Abitz
 */
public class MissingDataException extends Exception {

    public MissingDataException(String msg) {
        super(msg);
    }

    public MissingDataException(Throwable cause) {
        super(cause);
    }

    public MissingDataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
