package de.unileipzig.irpact.commons.awareness;

/**
 * @author Daniel Abitz
 */
public interface AwarenessSupplyScheme<T> {

    Awareness<T> derive();
}
