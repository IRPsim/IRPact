package de.unileipzig.irpact.core.log;

import de.unileipzig.irptools.util.log.LoggingSection;

/**
 * @author Daniel Abitz
 */
public enum IRPSection implements LoggingSection {
    /*
     * Used for IRPTools.
     */
    TOOLS,

    /*
     * Used in the initialization process.
     * Set via input parameters.
     */
    INITIALIZATION_PARAMETER,
    INITIALIZATION_AGENT,
    INITIALIZATION_NETWORK,
    INITIALIZATION_PLATFORM,

    /*
     *
     */
    SIMULATION_LICECYCLE,

    /*
     *
     */
    SPECIFICATION_CONVERTER
    ;
}
