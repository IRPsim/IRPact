package de.unileipzig.irpact.core.misc;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
public interface InitalizablePart extends ChecksumComparable {

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
    default void initialize() throws MissingDataException {
    }

    /**
     * Validates existence of all dependencies.
     *
     * @throws ValidationException something went wrong
     */
    default void preAgentCreationValidation() throws ValidationException {
    }

    /**
     * Called after all proxy agents are created.
     *
     * @throws MissingDataException something went wrong
     */
    default void postAgentCreation() throws MissingDataException {
    }

    /**
     * Validates existence of all dependencies.
     *
     * @throws ValidationException something went wrong
     */
    default void postAgentCreationValidation() throws ValidationException {
    }

    /**
     * Final setup before the simulation is started (simulation dependencies are now available).
     *
     * @throws MissingDataException something is missing
     */
    default void preSimulationStart() throws MissingDataException {
    }

    /**
     * Called after the simulation was finished.
     */
    default void postSimulation() {
    }
}
