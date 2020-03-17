package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public enum EntityType {
    OTHER, //speziell, wird als null genutzt oder bei map-teilmengen
    //agenten
    AGENT,
    SPATIAL_AGENT,
    INFORMATION_AGENT,
    SPATIAL_INFORMATION_AGENT,
    COMPANY_AGENT,
    CONSUMER_AGENT,
    POLICY_AGENT,
    POINT_OF_SALE_AGENT,
    SIMULATION_AGENT,
    //agentengruppen
    AGENT_GROUP,
    CONSUMER_AGENT_GROUP,
    //produckte
    PRODUCT,
    //producktgruppen
    PRODUCT_GROUP
}
