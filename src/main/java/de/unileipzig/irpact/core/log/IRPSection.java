package de.unileipzig.irpact.core.log;

import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.LoggingSection;

/**
 * Logging sections used in IRPact.
 *
 * @author Daniel Abitz
 */
public enum IRPSection implements LoggingSection {
    /**
     * For logging in IRPTools.
     */
    TOOLS_CORE,
    /**
     * For logging in IRPTools.
     */
    TOOLS_DEFINITION,
    /**
     * For logging in IRPTools.
     */
    TOOLS_UTIL,

    /**
     * This option is used for default logging operations.
     */
    GENERAL,

    /**
     * Used throughout the initialization.
     */
    INITIALIZATION_PARAMETER,
    /**
     * Used in topologies.
     */
    INITIALIZATION_NETWORK,
    /**
     * Used during platform creation.
     */
    INITIALIZATION_PLATFORM,

    /**
     * Used in all life cycle operations.
     */
    SIMULATION_LIFECYCLE,
    /**
     * Used in agent operations during simulation.
     */
    SIMULATION_AGENT,
    /**
     * Used in process model during simulation.
     */
    SIMULATION_PROCESS,

    /*
     *
     */
    SPECIFICATION_CONVERTER,

    /**
     * Used to log intern jadex informations.
     */
    JADEX_SYSTEM_OUT,
    ;

    @Override
    public Class<IRPSection> getType() {
        return IRPSection.class;
    }

    public IRPSection orGeneral(boolean useGeneral) {
        return useGeneral ? GENERAL : this;
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
            filter.add(INITIALIZATION_PLATFORM);
            filter.add(INITIALIZATION_NETWORK);
        }
    }

    public static void addSimulation(boolean add, SectionLoggingFilter filter) {
        if(add) {
            filter.add(SIMULATION_LIFECYCLE);
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
