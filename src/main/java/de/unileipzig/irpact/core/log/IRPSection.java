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
    SIMULATION_AGENT,

    /*
     *
     */
    SPECIFICATION_CONVERTER,

    /*
     *
     */
    JADEX_SYSTEM_OUT
    ;

    public static void addAllTo(SectionLoggingFilter filter) {
        for(IRPSection section: values()) {
            filter.add(section);
        }
    }

    public static void removeAllFrom(SectionLoggingFilter filter) {
        for(IRPSection section: values()) {
            filter.remove(section);
        }
    }
}
