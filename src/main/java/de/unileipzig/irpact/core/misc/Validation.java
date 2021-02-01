package de.unileipzig.irpact.core.misc;

/**
 * @author Daniel Abitz
 */
public interface Validation {

    boolean isValid();

    void validate() throws ValidationException;
}
