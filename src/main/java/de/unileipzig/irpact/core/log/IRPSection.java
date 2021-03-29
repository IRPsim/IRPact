package de.unileipzig.irpact.core.log;

import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.LoggingSection;

/**
 * @author Daniel Abitz
 */
public enum IRPSection implements LoggingSection {
    /*
     * Used for IRPTools.
     */
    TOOLS_CORE,
    TOOLS_DEFINITION,
    TOOLS_UTIL,

    /*
     * Used in the initialization process.
     * Set via input parameters.
     */
    INITIALIZATION_PARAMETER,
    INITIALIZATION_AGENT,
    INITIALIZATION_NETWORK,
    INITIALIZATION_PLATFORM,

    /*
     * Simulation
     */
    SIMULATION_LICECYCLE,
    SIMULATION_AGENT,
    SIMULATION_PROCESS,

    /*
     *
     */
    SPECIFICATION_CONVERTER,

    /*
     * Outputspam
     */
    JADEX_SYSTEM_OUT,

    /*
     * Tag-stuff
     */
    TAG_GRAPH_UPDATE,
    TAG_RELATIVE_AGREEMENT,
    TAG_INTEREST_UPDATE,
    TAG_SHARE_NETWORK_LOCAL
    ;


    @Override
    public Class<IRPSection> getType() {
        return IRPSection.class;
    }

    public static void addAllNonToolsTo(SectionLoggingFilter filter) {
        addAllTo(filter);
        removeAllToolsFrom(filter);
    }

    public static void addAllNonToolsTo(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addAllNonToolsTo(filter);
        }
    }

    public static void addAllTo(SectionLoggingFilter filter) {
        for(IRPSection section: values()) {
            filter.add(section);
        }
    }

    public static void addAllTo(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addAllTo(filter);
        }
    }

    public static void removeAllFrom(SectionLoggingFilter filter) {
        for(IRPSection section: values()) {
            filter.remove(section);
        }
    }

    public static void addInitialization(boolean add, SectionLoggingFilter filter) {
        if(add) {
            filter.add(INITIALIZATION_PARAMETER);
            filter.add(INITIALIZATION_AGENT);
            filter.add(INITIALIZATION_PLATFORM);
            filter.add(INITIALIZATION_NETWORK);
        }
    }

    public static void addSimulation(boolean add, SectionLoggingFilter filter) {
        if(add) {
            filter.add(SIMULATION_LICECYCLE);
            filter.add(SIMULATION_AGENT);
            filter.add(SIMULATION_PROCESS);
        }
    }

    public static void addSectionsToTools() {
        IRPtools.setToolsSection(TOOLS_CORE);
        IRPtools.setDefinitionSection(TOOLS_DEFINITION);
        IRPtools.setUtilSection(TOOLS_UTIL);
    }

    public static void addAllToolsTo(SectionLoggingFilter filter) {
        filter.add(TOOLS_CORE);
        filter.add(TOOLS_DEFINITION);
        filter.add(TOOLS_UTIL);
    }

    public static void addAllToolsTo(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addAllToolsTo(filter);
        }
    }

    public static void removeAllToolsFrom(SectionLoggingFilter filter) {
        filter.remove(TOOLS_CORE);
        filter.remove(TOOLS_DEFINITION);
        filter.remove(TOOLS_UTIL);
    }
}
