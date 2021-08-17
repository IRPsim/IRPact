package de.unileipzig.irpact.commons.persistence;

/**
 * Markerinterface for persistable Objects in IRPact. Every Object must have an unique identifier.
 *
 * @author Daniel Abitz
 */
public interface Persistable {

    long getUID();

    String getUIDString();
}
