package de.unileipzig.irpact.commons.persistence;

/**
 * Markerinterface for persistable Objects in IRPact. Every Object must have an unique identifier.
 *
 * @author Daniel Abitz
 */
public interface Persistable {

    boolean setUID(UIDManager manager);

    boolean hasUID();

    long getUID();

    String getUIDString();

    //Diese Methode simuliert einen Speichervorgang stattfand.
    //Dient primaer dazu Rnd (>Random) neu zu initialisieren.
    default void simulateSaving() {
    }
}
