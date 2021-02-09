package de.unileipzig.irpact.core.misc;

/**
 * @author Daniel Abitz
 */
public interface Initialization {

    /**
     * Initalize the basic setup without dependencies.
     */
    void initialize();

    /**
     * Validates the object.
     *
     * @return true: is valid
     */
    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    /**
     * Validates existence of all dependencies.
     *
     * @throws ValidationException something went wrong
     */
    void validate() throws ValidationException;
}
