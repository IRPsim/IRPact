package de.unileipzig.irpact.core.misc;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Initialization extends IsEquals {

    /**
     * Called before all proxy agents are created.
     *
     * @throws MissingDataException something went wrong
     */
    default void preAgentCreation() throws MissingDataException {
    }

    /**
     * Initalize the basic setup without dependencies.
     */
    default void initialize() {
    }

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
    default void validate() throws ValidationException {
    }

    /**
     * Called after all proxy agents are created.
     *
     * @throws MissingDataException something went wrong
     */
    default void postAgentCreation() throws MissingDataException {
    }

    /**
     * Final setup before the simulation is started (simulation dependencies are now available).
     *
     * @throws MissingDataException something is missing
     */
    default void preSimulationStart() throws MissingDataException {
    }
}
