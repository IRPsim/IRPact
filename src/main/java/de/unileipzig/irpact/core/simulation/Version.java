package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public interface Version {

    boolean isMismatch(Version other);

    String print();
}
