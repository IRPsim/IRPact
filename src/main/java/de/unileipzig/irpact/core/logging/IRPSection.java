package de.unileipzig.irpact.core.logging;

import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.LoggingSection;

import java.util.Arrays;
import java.util.List;

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
     * This option is used for utilities operations.
     */
    UTILITIES,

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

    /**
     * ...
     */
    SPECIFICATION_CONVERTER,

    /**
     * Used to log intern jadex informations.
     */
    JADEX_SYSTEM_OUT,

    //=====

    /**
     * Special logging section for debugging.
     */
    DEBUG
    ;

    @Override
    public Class<IRPSection> getType() {
        return IRPSection.class;
    }

    public IRPSection orGeneral(boolean useGeneral) {
        return useGeneral ? GENERAL : this;
    }

    protected static List<IRPSection> valueList;
    public static List<IRPSection> valuesList() {
        if(valueList == null) {
            valueList = Arrays.asList(values());
        }
        return valueList;
    }

    //=========================
    // special IRPtools
    //=========================

    public static void addSectionsToTools() {
        IRPtools.setToolsSection(TOOLS_CORE);
        IRPtools.setDefinitionSection(TOOLS_DEFINITION);
        IRPtools.setUtilSection(TOOLS_UTIL);
    }

    //=========================
    // add
    //=========================

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
        filter.addAll(valuesList());
    }
    public static void addAllTo(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addAllTo(filter);
        }
    }

    public static void addInitialization(SectionLoggingFilter filter) {
        filter.add(INITIALIZATION_PARAMETER);
        filter.add(INITIALIZATION_PLATFORM);
        filter.add(INITIALIZATION_NETWORK);
    }
    public static void addInitialization(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addInitialization(filter);
        }
    }

    public static void addSimulation(SectionLoggingFilter filter) {
        filter.add(SIMULATION_LIFECYCLE);
        filter.add(SIMULATION_AGENT);
        filter.add(SIMULATION_PROCESS);
    }
    public static void addSimulation(boolean add, SectionLoggingFilter filter) {
        if(add) {
            addSimulation(filter);
        }
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

    //=========================
    // remove
    //=========================

    public static void removeAllToolsFrom(SectionLoggingFilter filter) {
        filter.remove(TOOLS_CORE);
        filter.remove(TOOLS_DEFINITION);
        filter.remove(TOOLS_UTIL);
    }

    public static void removeAllFrom(SectionLoggingFilter filter) {
        filter.removeAll(valuesList());
    }

    public static void removeDevelop(SectionLoggingFilter filter) {
        filter.remove(DEBUG);
    }
}
