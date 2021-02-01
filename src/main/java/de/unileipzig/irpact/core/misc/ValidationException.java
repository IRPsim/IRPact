package de.unileipzig.irpact.core.misc;

/**
 * @author Daniel Abitz
 */
public class ValidationException extends Exception {

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
